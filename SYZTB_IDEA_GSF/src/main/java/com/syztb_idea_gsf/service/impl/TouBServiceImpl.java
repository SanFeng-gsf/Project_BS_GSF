package com.syztb_idea_gsf.service.impl;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.syztb_idea_gsf.dto.Result;
import com.syztb_idea_gsf.entity.DTO;
import com.syztb_idea_gsf.entity.TouB;
import com.syztb_idea_gsf.entity.ZhaoB;
import com.syztb_idea_gsf.mapper.TouBMapper;
import com.syztb_idea_gsf.mapper.ZhaoBMapper;
import com.syztb_idea_gsf.service.ITouBService;
import com.syztb_idea_gsf.utils.CacheClient;
import com.syztb_idea_gsf.utils.RedisData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.syztb_idea_gsf.utils.RedisConstants.*;

@Slf4j
@Service
public class TouBServiceImpl extends ServiceImpl<TouBMapper, TouB> implements ITouBService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private ZhaoBMapper zhaoBMapper;

    @Resource
    private CacheClient cacheClient;

    @Override
    public Result selectByName(String name) {
        // 缓存击穿 (逻辑过期)
        String key = CACHE_TB_KEY + name;
        String cache = stringRedisTemplate.opsForValue().get(key);
        String lockKey = LOCK_TB_KEY + name;
        if (cache==null) {
            // 一开始缓存没有，就查询数据库
            List<TouB> list = this.baseMapper.selectByMap(Map.of("name", name));
            // List<ZhaoB> list = this.baseMapper.selectByName(name);
            if (list.size()==0) {
                return Result.fail("该公司还未进行投标");
            }
            setCacheRebuildExecutor(null,list,key,lockKey);
            return Result.ok("查询成功",list);
        }
        // 命中 先把 json 反序列化为对象
        RedisData redisData = JSONUtil.toBean(cache, RedisData.class);
        // Shop shop = JSONUtil.toBean((JSONObject) redisData.getData(),Shop.class);
        List<TouB> list = JSONUtil.toList((JSONArray) redisData.getData(), TouB.class);
        LocalDateTime expireTime = redisData.getExpireTime();
        if(expireTime.isAfter(LocalDateTime.now())){
            return Result.ok("查询成功",list);
        }
        setCacheRebuildExecutor(name,null,key,lockKey);
        return Result.ok("查询成功",list);
    }

    @Override
    public Result selectDetail(DTO dto) {
        TouB touB = cacheClient.queryWithLogicalExpire(CACHE_TB_KEY, dto.getProjectName(), TouB.class,
                sql -> this.baseMapper.selectByNameAndSNameAndProjectName(dto.getName(),dto.getSuoName(),dto.getProjectName()),
                180L,TimeUnit.MINUTES);
        if(touB==null){
            return Result.fail("暂无投标信息");
        }
        return Result.ok("查询成功",touB);
    }

    @Override
    public Result insert(TouB touB) {
        TouB sqltouB = this.baseMapper.selectByNameAndSNameAndProjectName(touB.getName(),touB.getSuoName(),touB.getProjectName());
        if(sqltouB!=null){
            return Result.fail("已投标过该项目,请勿重复投标");
        }
        int result = isExpireAndClose(touB.getSuoName(),touB.getProjectName());
        if(result==0){
            return Result.fail("该招标项目不存在");
        }
        if(result==-1){
            return Result.fail("该招标项目已经截止");
        }
        if(result==-2){
            return Result.fail("该招标项目已经被暂停");
        }
        boolean isInsert = save(touB);
        if(!isInsert){
            return Result.fail("保存失败");
        }
        // 数据库新增 删除之前的相关缓存
        String key = CACHE_TB_KEY + touB.getName();
        stringRedisTemplate.delete(key);
        stringRedisTemplate.delete(CACHE_TB_KEY + touB.getProjectName());
        return Result.ok("投标成功");
    }

    @Override
    public Result delete(DTO dto) {
        TouB sqltouB = this.baseMapper.selectByNameAndSNameAndProjectName(dto.getName(),dto.getSuoName(),dto.getProjectName());
        if(sqltouB==null){
            return Result.ok("删除成功");
        }
        boolean isDelete = removeById(sqltouB);
        if(!isDelete){
            return Result.fail("删除失败");
        }
        // 数据库删除 删除之前的相关缓存
        String key = CACHE_TB_KEY + sqltouB.getProjectName();
        stringRedisTemplate.delete(key);
        key = CACHE_TB_KEY + sqltouB.getName();
        stringRedisTemplate.delete(key);
        return Result.ok("删除成功");
    }

    @Override
    public Result selectByN(DTO dto) {
        QueryWrapper<TouB> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("suo_name",dto.getSuoName());
        queryWrapper.eq("project_name",dto.getProjectName());
        List<TouB> touBList = this.baseMapper.selectList(queryWrapper);
        return Result.ok("success",touBList);
    }

    @Override
    public Result setSuccessById(Integer id) {
        if (this.baseMapper.setSuccessById(id)) {
            return Result.ok("success");
        }
        return Result.fail("fail");
    }

    /**
     * 判断招标项目是否过期 是否禁用
     */
    private int isExpireAndClose(String name,String projectName){
        ZhaoB zhaoB = zhaoBMapper.selectByNameAndProjectName(name, projectName);
        if(zhaoB==null){
            return 0;
        }
        LocalDateTime endTime = zhaoB.getEndTime();
        if(LocalDateTime.now().isAfter(endTime)){
            // 过期
            return -1;
        }
        if(zhaoB.getClose() == 1){
            // 禁用 暂停
            return -2;
        }
        return 1;
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
    private void setCacheRebuildExecutor(String id,List<TouB> list,String key,String lockKey){
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
    public void saveRedis(String id,List<TouB> list,String key,Long expireSeconds){
        if(list==null){
            list = this.baseMapper.selectByMap(Map.of(id,id));
        }
        // 封装逻辑过期时间
        RedisData redisData = new RedisData();
        redisData.setData(list);
        redisData.setExpireTime(LocalDateTime.now().plusSeconds(expireSeconds));
        // 写入redis
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(redisData));
    }
}
