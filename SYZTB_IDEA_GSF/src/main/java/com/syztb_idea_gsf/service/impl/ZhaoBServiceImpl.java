package com.syztb_idea_gsf.service.impl;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.syztb_idea_gsf.dto.Result;
import com.syztb_idea_gsf.entity.TouB;
import com.syztb_idea_gsf.entity.ZhaoB;
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
        if (cache==null) {
            // 一开始缓存没有，就查询数据库
            List<ZhaoB> list = this.baseMapper.selectByMap(Map.of("name", name));
            // List<ZhaoB> list = this.baseMapper.selectByName(name);
            if (list.size()==0) {
                return Result.fail("该公司还未进行招标");
            }
            setCacheRebuildExecutor(null,list,key,lockKey);
            return Result.ok(list);
        }
        // 命中 先把 json 反序列化为对象
        RedisData redisData = JSONUtil.toBean(cache, RedisData.class);
        // Shop shop = JSONUtil.toBean((JSONObject) redisData.getData(),Shop.class);
        List<ZhaoB> list = JSONUtil.toList((JSONArray) redisData.getData(), ZhaoB.class);
        LocalDateTime expireTime = redisData.getExpireTime();
        if(expireTime.isAfter(LocalDateTime.now())){
            return Result.ok(list);
        }
        setCacheRebuildExecutor(name,null,key,lockKey);
        return Result.ok(list);
    }

    @Override
    public Result selectByProjectName(String projectName) {
        List<Map<String,Object>> list = new ArrayList<>();
        ZhaoB zhaoB = cacheClient.queryWithLogicalExpire(CACHE_ZB_KEY, projectName, ZhaoB.class,
                sql -> query().eq("project_name", projectName).one(), 180L, TimeUnit.MINUTES);
        if(zhaoB==null){
            return Result.fail("暂无该项目详情,请稍后刷新重试");
        }
        list.add(0,Map.of("zhaoB",zhaoB));
        String key = CACHE_TB_KEY + projectName;
        String cache = stringRedisTemplate.opsForValue().get(key);
        List<TouB> touBlist = null;
        if(cache!=null){
            RedisData redisData = JSONUtil.toBean(cache, RedisData.class);
            touBlist = JSONUtil.toList((JSONArray) redisData.getData(), TouB.class);
            LocalDateTime expireTime = redisData.getExpireTime();
            if(expireTime.isAfter(LocalDateTime.now())){
                // 缓存未过期
                list.add(1,Map.of("expire","Y"));
            }else {
                list.add(1,Map.of("expire","N"));
            }
        }
        if(list.size()==1){
            // 前端需判断里面的值 -1 表示 (暂无人员参与投标)
            list.add(1,Map.of("touB",-1));
        }else if(list.get(1).get("expire").equals("Y")){
            list.remove(1);
            list.add(1,Map.of("touB",touBlist));
            return Result.ok(list);
        }else {
            list.remove(1);
            list.add(1,Map.of("touB",touBlist));
        }
        String lockKey = LOCK_TB_KEY + projectName;
        boolean newLock = tryLock(lockKey);
        if (newLock) {
            // 开启独立线程 实现缓存重建
            CACHE_REBUILD_EXECUTOR.submit(() -> {
                try {
                    List<TouB> sqltouBlist = touBMapper.selectByMap(Map.of("project_name", projectName));
                    // 封装逻辑过期时间
                    RedisData redisData = new RedisData();
                    redisData.setData(sqltouBlist);
                    redisData.setExpireTime(LocalDateTime.now().plusSeconds(180L));
                    // 写入redis
                    stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(redisData));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    unLock(lockKey);
                }
            });
        }
        return Result.ok(list);
    }

    @Override
    public Result selectAll(Integer current) {
        String cache = stringRedisTemplate.opsForValue().get(CACHE_ALL_ZB_KEY);
        if(cache==null){
            Page<ZhaoB> page = query().page(new Page<>(current,DEFAULT_PAGE_SIZE));
            if(page==null){
                return Result.fail("暂无招标信息");
            }
            List<ZhaoB> list = page.getRecords();
            setCacheRebuildExecutor(null,list,CACHE_ALL_ZB_KEY,LOCK_ALL_ZB_KEY);
            return Result.ok(page);
        }
        RedisData redisData = JSONUtil.toBean(cache, RedisData.class);
        List<ZhaoB> list = JSONUtil.toList((JSONArray) redisData.getData(), ZhaoB.class);
        LocalDateTime expireTime = redisData.getExpireTime();
        if(!expireTime.isAfter(LocalDateTime.now())){
            setCacheRebuildExecutor(null,null,CACHE_ALL_ZB_KEY,LOCK_ALL_ZB_KEY);
        }
        // 分页
        Page<ZhaoB> page = new Page<>(current,DEFAULT_PAGE_SIZE);
        page.setRecords(list);
        return Result.ok(page);
    }

    @Override
    public Result insert(ZhaoB zhaoB) {
        String projectName = zhaoB.getProjectName();
        List<ZhaoB> zhao = this.baseMapper.selectByMap(Map.of("project_name", projectName));
        if(zhao.size()>0){
            return Result.fail("该招标项目已存在");
        }
        return Result.ok();
    }

    /**
     * 线程池
     */
    private static final ExecutorService CACHE_REBUILD_EXECUTOR = Executors.newFixedThreadPool(10);

    /**
     * 获取锁 (解决缓存击穿)
     */
    private boolean tryLock(String key){
        // redis setNx 命令  (相当于设置互斥锁)
        Boolean flag = stringRedisTemplate.opsForValue().setIfAbsent(key, "1", 10, TimeUnit.MINUTES);
        return BooleanUtil.isTrue(flag);
    }

    /**
     * 删除锁
     */
    private void unLock(String key){
        stringRedisTemplate.delete(key);
    }

    /**
     * 缓存重建
     */
    private void setCacheRebuildExecutor(String id,List<ZhaoB> list,String key,String lockKey){
        boolean newLock = tryLock(lockKey);
        if (newLock) {
            // 开启独立线程 实现缓存重建
            CACHE_REBUILD_EXECUTOR.submit(() -> {
                try {
                    this.saveRedis(id,list,key,180L);
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
    public void saveRedis(String id,List<ZhaoB> list,String key,Long expireSeconds){
        if(list==null){
            if(id==null){
                list = this.baseMapper.selectAll();
            }else {
                list = this.baseMapper.selectByMap(Map.of(id,id));
            }
        }
        // 封装逻辑过期时间
        RedisData redisData = new RedisData();
        redisData.setData(list);
        redisData.setExpireTime(LocalDateTime.now().plusSeconds(expireSeconds));
        // 写入redis
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(redisData));
    }

}
