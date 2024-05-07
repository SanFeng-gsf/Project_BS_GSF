package com.syztb_idea_gsf.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.syztb_idea_gsf.config.JSONLocalDateTime;
import com.syztb_idea_gsf.dto.Result;
import com.syztb_idea_gsf.entity.ZhaoB;
import com.syztb_idea_gsf.entity.ZhaoBUn;
import com.syztb_idea_gsf.mapper.ZhaoBUnMapper;
import com.syztb_idea_gsf.service.IZhaoBService;
import com.syztb_idea_gsf.service.IZhaoBUnService;
import com.syztb_idea_gsf.utils.RedisData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.syztb_idea_gsf.utils.RedisConstants.*;
import static com.syztb_idea_gsf.utils.SystemConstants.DEFAULT_PAGE_SIZE;

@Slf4j
@Service
public class ZhaoBUnServiceImpl extends ServiceImpl<ZhaoBUnMapper, ZhaoBUn> implements IZhaoBUnService {
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    private final JSONConfig jsonConfig = new JSONLocalDateTime().getJsonConfig();
    @Resource
    private IZhaoBService iZhaoBService;
    @Override
    public Result selectAll(Integer current) {
        Page<ZhaoBUn> page = query().page(new Page<>(current, 10));
        return Result.ok("查询成功", page);
    }

    @Override
    public Result updateCheck(Integer id, String pass) {
        ZhaoBUn zhaoBUn = this.baseMapper.selectById(id);
        zhaoBUn.setPass(pass);
        boolean isCheck = updateById(zhaoBUn);
        if (!isCheck) {
            return Result.fail("审核更新失败");
        }
        if(pass.equals("-1")) {
            return Result.ok("审核更新成功");
        }
        ZhaoB zhaoB = BeanUtil.copyProperties(zhaoBUn, ZhaoB.class);
        iZhaoBService.save(zhaoB);
        stringRedisTemplate.delete(CACHE_ALL_ZB_KEY);
        stringRedisTemplate.delete(CACHE_ZB_KEY);
        stringRedisTemplate.delete(CACHE_ALL_ZB_UN_KEY + zhaoB.getProjectName());
        return Result.ok("审核更新成功");
    }

    @Override
    public Result selectByProjectName(String projectName) {
        String key = CACHE_ALL_ZB_UN_KEY + projectName;
        String cache = stringRedisTemplate.opsForValue().get(key);
        String lockKey = LOCK_ALL_ZB_UN_KEY + projectName;
        List<ZhaoBUn> list;
        if (cache != null) {
            RedisData redisData = JSONUtil.toBean(cache, RedisData.class);
            list = JSONUtil.toList((JSONArray) redisData.getData(), ZhaoBUn.class);
            LocalDateTime expireTime = redisData.getExpireTime();
            if (!expireTime.isAfter(LocalDateTime.now())) {
                // 缓存过期 重建缓存
                try {
                    setCacheRebuildExecutor(key,projectName,lockKey,null);
                } catch (Exception e) {
                    return Result.fail("未查询到对应的招标信发布息");
                }
            }
            return Result.ok("success",list);
        }
        list = this.baseMapper.selectByMap(Map.of("project_name", projectName));
        if(list.size()>0){
            setCacheRebuildExecutor(key,null,lockKey,list);
        }
        return Result.ok("success",list);
    }

    @Override
    public boolean insertZhaoBUn(ZhaoBUn zhaoBUn) {
        boolean result = save(zhaoBUn);
        if(result){
            stringRedisTemplate.delete(CACHE_ALL_ZB_KEY);
            String key = CACHE_ZB_KEY + zhaoBUn.getName() + zhaoBUn.getProjectName();
            stringRedisTemplate.delete(key);
        }
        return result;
    }


    /**
     * 线程池
     */
    private static final ExecutorService CACHE_REBUILD_EXECUTOR = Executors.newFixedThreadPool(10);

    /**
     * 获取锁 (解决缓存击穿)
     */
    private boolean tryLock(String key) {
        // redis setNx 命令  (相当于设置互斥锁)
        Boolean flag = stringRedisTemplate.opsForValue().setIfAbsent(key, "1", 10, TimeUnit.MINUTES);
        return BooleanUtil.isTrue(flag);
    }

    /**
     * 删除锁
     */
    private void unLock(String key) {
        stringRedisTemplate.delete(key);
    }



    private void setCacheRebuildExecutor(String key, String projectName, String lockKey, List<ZhaoBUn> list) {
        boolean newLock = tryLock(lockKey);
        if (newLock) {
            // 开启独立线程 实现缓存重建
            CACHE_REBUILD_EXECUTOR.submit(() -> {
                try {
                    if(list == null){
                        List<ZhaoBUn> newList = baseMapper.selectByMap(Map.of("projectName", projectName));
                        if (newList.size() > 0) {
                            this.saveRedis(key, newList);
                        } else {
                            throw new RuntimeException("无对应的招标信发布息");
                        }
                    } else {
                        this.saveRedis(key, list);
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    unLock(lockKey);
                }
            });
        }
    }

    private void saveRedis(String key, List<ZhaoBUn> list){
        RedisData redisData = new RedisData();
        redisData.setData(list);
        redisData.setExpireTime(LocalDateTime.now().plusSeconds(120L));
        // 写入redis
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(redisData,jsonConfig));
    }
}
