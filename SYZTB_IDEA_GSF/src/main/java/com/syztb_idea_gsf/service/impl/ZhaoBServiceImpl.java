package com.syztb_idea_gsf.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.json.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.syztb_idea_gsf.config.JSONLocalDateTime;
import com.syztb_idea_gsf.dto.Result;
import com.syztb_idea_gsf.entity.DTO;
import com.syztb_idea_gsf.entity.TouB;
import com.syztb_idea_gsf.entity.ZhaoB;
import com.syztb_idea_gsf.entity.ZhaoBUn;
import com.syztb_idea_gsf.mapper.TouBMapper;
import com.syztb_idea_gsf.mapper.ZhaoBMapper;
import com.syztb_idea_gsf.service.IZhaoBService;
import com.syztb_idea_gsf.utils.CacheClient;
import com.syztb_idea_gsf.utils.RedisData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.syztb_idea_gsf.utils.RedisConstants.*;
import static com.syztb_idea_gsf.utils.SystemConstants.DEFAULT_PAGE_SIZE;

@Slf4j
@Service
public class ZhaoBServiceImpl extends ServiceImpl<ZhaoBMapper, ZhaoB> implements IZhaoBService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private final JSONConfig jsonConfig = new JSONLocalDateTime().getJsonConfig();
    @Autowired
    private TouBMapper touBMapper;
    @Resource
    private CacheClient cacheClient;

    @Override
    public Result selectByName(String name) {
        // 缓存击穿 (逻辑过期)
        String key = CACHE_ZB_KEY + name;
        String cache = stringRedisTemplate.opsForValue().get(key);
        String lockKey = LOCK_ZB_KEY + name;
        if (cache == null) {
            // 一开始缓存没有，就查询数据库
            List<ZhaoB> list = this.baseMapper.selectByMap(Map.of("name", name));
            // List<ZhaoB> list = this.baseMapper.selectByName(name);
            if (list.size() == 0) {
                return Result.fail("该公司还未进行招标");
            }
            setCacheRebuildExecutor(null, list, key, lockKey);
            return Result.ok("查询成功", list);
        }
        // 命中 先把 json 反序列化为对象
        RedisData redisData = JSONUtil.toBean(cache, RedisData.class);
        List<ZhaoB> list = JSONUtil.toList((JSONArray) redisData.getData(), ZhaoB.class);
        LocalDateTime expireTime = redisData.getExpireTime();
        if (expireTime.isAfter(LocalDateTime.now())) {
            return Result.ok("查询成功", list);
        }
        setCacheRebuildExecutor(name, null, key, lockKey);
        return Result.ok("查询成功", list);
    }

    @Override
    public Result selectDetail(DTO dto) {
        List<Map<String, Object>> list = new ArrayList<>();
        ZhaoB zhaoB = cacheClient.queryWithLogicalExpire(CACHE_ZB_KEY, dto.getId(), ZhaoB.class,
                this::getById, 180L, TimeUnit.MINUTES);
        if (zhaoB == null) {
            return Result.fail("暂无该项目详情,请稍后刷新重试");
        }
        list.add(0, Map.of("zhaoB", zhaoB));
        String key = CACHE_TB_KEY + zhaoB.getProjectName();
        String cache = stringRedisTemplate.opsForValue().get(key);
        List<TouB> touBlist = null;
        if (cache != null) {
            RedisData redisData = JSONUtil.toBean(cache, RedisData.class);
            touBlist = JSONUtil.toList((JSONArray) redisData.getData(), TouB.class);
            LocalDateTime expireTime = redisData.getExpireTime();
            if (expireTime.isAfter(LocalDateTime.now())) {
                // 缓存未过期
                list.add(1, Map.of("expire", "Y"));
            } else {
                list.add(1, Map.of("expire", "N"));
            }
        }
        if (list.size() == 1) {
            // 前端需判断里面的值 -1 表示 (暂无人员参与投标)
            list.add(1, Map.of("touB", -1));
        } else if (list.get(1).get("expire").equals("Y")) {
            list.remove(1);
            list.add(1, Map.of("touB", touBlist));
            return Result.ok("查询成功", list);
        } else {
            list.remove(1);
            list.add(1, Map.of("touB", touBlist));
        }
        String lockKey = LOCK_TB_KEY + zhaoB.getProjectName();
        boolean newLock = tryLock(lockKey);
        if (newLock) {
            // 开启独立线程 实现缓存重建
            CACHE_REBUILD_EXECUTOR.submit(() -> {
                try {
                    List<TouB> sqltouBlist = touBMapper.selectByMap(Map.of("project_name", zhaoB.getProjectName(), "suo_name", zhaoB.getName()));
                    // 封装逻辑过期时间
                    RedisData redisData = new RedisData();
                    redisData.setData(sqltouBlist);
                    redisData.setExpireTime(LocalDateTime.now().plusSeconds(180L));
                    // 写入redis
                    stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(redisData,jsonConfig));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    unLock(lockKey);
                }
            });
        }
        return Result.ok("查询成功", list);
    }

    @Override
    public Result selectAll(Integer current) {
        String key = CACHE_ALL_ZB_KEY;
        String cache = stringRedisTemplate.opsForValue().get(key);
        if (cache == null) {
            Page<ZhaoB> page = query().page(new Page<>(current, DEFAULT_PAGE_SIZE));
            if (page == null) {
                return Result.fail("暂无招标信息");
            }
            setPage(key, page);
            return Result.ok("查询成功", page);
        }
        Page result = getPage(cache, key, current);
        return Result.ok("查询成功", result);
    }

    @Override
    public Result selectSuccess(Integer current) {
        String key = CACHE_SUCCESS_ZB_KEY;
        String cache = stringRedisTemplate.opsForValue().get(key);
        if (cache == null) {
            Page<ZhaoB> page = new Page<>(current, DEFAULT_PAGE_SIZE);
            QueryWrapper<ZhaoB> queryWrapper = new QueryWrapper<>();
            // end_time <= 当前时间
            queryWrapper.le("end_time",LocalDateTime.now());
            this.baseMapper.selectPage(page,queryWrapper);
            if (page.getTotal() == 0) {
                return Result.fail("暂无招标信息");
            }
            setPage(key, page);
            return Result.ok("查询成功", page);
        }
        Page result = getPage(cache, key, current);
        return Result.ok("查询成功", result);
    }

    @Override
    public Result selectUnSuccess(Integer current) {
        String key = CACHE_UN_SUCCESS_ZB_KEY;
        String cache = stringRedisTemplate.opsForValue().get(key);
        if (cache == null) {
            Page<ZhaoB> page = new Page<>(current, DEFAULT_PAGE_SIZE);
            QueryWrapper<ZhaoB> queryWrapper = new QueryWrapper<>();
            // end_time > 当前时间
            queryWrapper.gt("end_time",LocalDateTime.now());
            this.baseMapper.selectPage(page,queryWrapper);
            if (page.getTotal() == 0) {
                return Result.fail("暂无招标信息");
            }
            setPage(key, page);
            return Result.ok("查询成功", page);
        }
        Page result = getPage(cache, key, current);
        return Result.ok("查询成功", result);
    }

    // 分页缓存重建
    private void setPage(String key, Page page) {
        boolean newLock = tryLock(LOCK_ALL_ZB_KEY);
        if (newLock) {
            // 开启独立线程 实现缓存重建
            CACHE_REBUILD_EXECUTOR.submit(() -> {
                try {
                    RedisData redisData = new RedisData();
                    redisData.setData(page);
                    redisData.setExpireTime(LocalDateTime.now().plusSeconds(CACHE_SELECT_ZB_TTL));
                    // 写入redis
                    stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(redisData,jsonConfig));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    unLock(LOCK_ALL_ZB_KEY);
                }
            });
        }
    }

    private Page getPage(String cache, String key, Integer current) {
        RedisData redisData = JSONUtil.toBean(cache, RedisData.class);
        JSONObject data = (JSONObject)redisData.getData();
        Page result = JSONUtil.toBean(data, Page.class);
        LocalDateTime expireTime = redisData.getExpireTime();
        if (!expireTime.isAfter(LocalDateTime.now())) {
            Page<ZhaoB> page = new Page<>(current, DEFAULT_PAGE_SIZE);
            // 第六个字符是否是以 all 开头
            if (key.startsWith("all", 6)) {
                page = query().page(new Page<>(current, DEFAULT_PAGE_SIZE));
            } else if ((key.startsWith("success", 6))) {
                QueryWrapper<ZhaoB> queryWrapper = new QueryWrapper<>();
                queryWrapper.le("end_time",LocalDateTime.now());
                this.baseMapper.selectPage(page,queryWrapper);
            } else {
                QueryWrapper<ZhaoB> queryWrapper = new QueryWrapper<>();
                queryWrapper.gt("end_time",LocalDateTime.now());
                this.baseMapper.selectPage(page,queryWrapper);
            }
            setPage(key, page);
        }
        return result;
    }

    public Result updateClose(Integer id, int close) {
        ZhaoB zhaoB = query().eq("id", id).one();
        zhaoB.setClose(close);
        int result = baseMapper.updateById(zhaoB);
        if (result != 1) {
            return Result.fail("更新失败");
        }
        // 数据库更新 删除之前的相关缓存
        stringRedisTemplate.delete(CACHE_ALL_ZB_KEY);
        stringRedisTemplate.delete(CACHE_SUCCESS_ZB_KEY);
        stringRedisTemplate.delete(CACHE_UN_SUCCESS_ZB_KEY);
        String key = CACHE_ZB_KEY + zhaoB.getName();
        stringRedisTemplate.delete(key);
        stringRedisTemplate.delete(CACHE_ZB_KEY + zhaoB.getProjectName());
        return Result.ok("更新成功");
    }

    @Override
    public Result selectByProjectName(String projectName, Integer current) {
        String key = CACHE_ZB_KEY + projectName;
        String cache = stringRedisTemplate.opsForValue().get(key);
        Page<ZhaoB> page = new Page<>(current, DEFAULT_PAGE_SIZE);
        QueryWrapper<ZhaoB> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("project_name",projectName);
        if (cache == null) {
            page = baseMapper.selectPage(page, queryWrapper);
            if (page == null) {
                return Result.fail("暂无招标信息");
            }
            setPage(key, page);
            return Result.ok("查询成功", page);
        }
        RedisData redisData = JSONUtil.toBean(cache, RedisData.class);
        JSONObject data = (JSONObject)redisData.getData();
        Page result = JSONUtil.toBean(data, Page.class);
        LocalDateTime expireTime = redisData.getExpireTime();
        if (!expireTime.isAfter(LocalDateTime.now())) {
            // 过期
            page = baseMapper.selectPage(page, queryWrapper);
            if (page == null) {
                return Result.fail("暂无招标信息");
            }
            setPage(key, page);
        }
        return Result.ok("查询成功",result);
    }

    @Override
    public Result selectWeiGui(Integer current) {
        String key = CACHE_WEI_GUI_ZB_KEY;
        String cache = stringRedisTemplate.opsForValue().get(key);
        if (cache == null) {
            Page<ZhaoB> page = new Page<>(current, DEFAULT_PAGE_SIZE);
            QueryWrapper<ZhaoB> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("close",1);
            page = this.baseMapper.selectPage(page, queryWrapper);
            if (page.getTotal() == 0) {
                return Result.fail("暂无违规信息");
            }
            setPage(key, page);
            return Result.ok("查询成功", page);
        }
        Page result = getPage(cache, key, current);
        return Result.ok("查询成功", result);
    }

    @Override
    public Result delete(DTO dto) {
        ZhaoB sqlzhaoB = this.baseMapper.selectByNameAndProjectName(dto.getName(), dto.getProjectName());
        if (sqlzhaoB == null) {
            return Result.ok("删除成功");
        }
        boolean isDelete = removeById(sqlzhaoB);
        if (!isDelete) {
            return Result.fail("删除失败");
        }
        // 数据库删除 删除之前的相关缓存
        stringRedisTemplate.delete(CACHE_ALL_ZB_KEY);
        stringRedisTemplate.delete(CACHE_SUCCESS_ZB_KEY);
        stringRedisTemplate.delete(CACHE_UN_SUCCESS_ZB_KEY);
        String key = CACHE_ZB_KEY + dto.getName();
        stringRedisTemplate.delete(key);
        stringRedisTemplate.delete(CACHE_ZB_KEY + dto.getProjectName());
        return Result.ok("删除成功");
    }

    @Override
    public boolean isFinish(Integer id) {
        ZhaoB zhaoB = query().eq("id", id).one();
        return zhaoB.getEndTime().isBefore(LocalDateTime.now());
    }

    /**
     * 线程池
     */
    public static final ExecutorService CACHE_REBUILD_EXECUTOR = Executors.newFixedThreadPool(10);

    /**
     * 获取锁 (解决缓存击穿)
     */
    public boolean tryLock(String key) {
        // redis setNx 命令  (相当于设置互斥锁)
        Boolean flag = stringRedisTemplate.opsForValue().setIfAbsent(key, "1", 10, TimeUnit.MINUTES);
        return BooleanUtil.isTrue(flag);
    }

    /**
     * 删除锁
     */
    public void unLock(String key) {
        stringRedisTemplate.delete(key);
    }

    /**
     * 缓存重建
     */
    private void setCacheRebuildExecutor(String id, List<ZhaoB> list, String key, String lockKey) {
        boolean newLock = tryLock(lockKey);
        if (newLock) {
            // 开启独立线程 实现缓存重建
            CACHE_REBUILD_EXECUTOR.submit(() -> {
                try {
                    this.saveRedis(id, list, key, 90L);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    unLock(lockKey);
                }
            });
        }
    }

    /**
     * 逻辑过期 (单元测试)
     */
    public void saveRedis(String id, List<ZhaoB> list, String key, Long expireSeconds) {
        if (list == null) {
            if (id == null) {
                list = this.baseMapper.selectAll();
            } else {
                list = this.baseMapper.selectByMap(Map.of("name", id));
            }
        }
        // 封装逻辑过期时间
        RedisData redisData = new RedisData();
        redisData.setData(list);
        redisData.setExpireTime(LocalDateTime.now().plusSeconds(expireSeconds));
        // 写入redis
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(redisData,jsonConfig));
    }

}
