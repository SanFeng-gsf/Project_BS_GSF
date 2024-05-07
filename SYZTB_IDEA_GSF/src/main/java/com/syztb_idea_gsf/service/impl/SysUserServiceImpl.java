package com.syztb_idea_gsf.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.syztb_idea_gsf.dto.Result;
import com.syztb_idea_gsf.entity.SysUser;
import com.syztb_idea_gsf.mapper.SysUserMapper;
import com.syztb_idea_gsf.service.ISysService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.syztb_idea_gsf.utils.RedisConstants.*;

@Slf4j
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Result login(SysUser sysUser) {
        String name = sysUser.getName();
        String password = sysUser.getPassword();
        List<SysUser> list = baseMapper.selectByMap(Map.of("name", name, "password", password));
        if (list.size()>0) {
            String token = setToken(list.get(0));
            if (token == null){
                return Result.fail("获取 token 失败");
            } else {
                Map<String, String> map = Map.of("token", token,"name",name);
                return Result.ok("登入成功", map);
            }
        }
        return Result.fail("账号或密码错误");
    }

    /**
     * 设置 token
     */
    private String setToken(SysUser sysUser){
        String token = UUID.randomUUID().toString();
        Map<String, Object> map = BeanUtil.beanToMap(sysUser, new HashMap<>(),
                CopyOptions.create().setIgnoreNullValue(true)
                        .setFieldValueEditor((fieldName, fieldValue) -> {
                            if (fieldValue==null) {
                                fieldValue = "";
                            }
                            return fieldValue.toString();
                        }));
        String tokenKey = LOGIN_SYS_USER_KEY + token;
        stringRedisTemplate.opsForHash().putAll(tokenKey, map);
        // 设置 token 有效期
        boolean expire = Boolean.TRUE.equals(stringRedisTemplate.expire(tokenKey, 60L, TimeUnit.MINUTES));
        if(expire){
            return tokenKey;
        }
        return null;
    }

    public Result logout(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
//        String key = RedisConstants.LOGIN_USER_KEY + token;
        stringRedisTemplate.delete(token);
        return Result.ok("删除成功");
    }

    public Result insert(SysUser sysUser) {
        SysUser sqlSys = query().eq("name", sysUser.getName()).one();
        if (sqlSys == null) {
            save(sysUser);
            return Result.ok("注册成功");
        }
        return Result.fail("该账号已存在");
    }

    @Override
    public Result selectByName(String name) {
        SysUser sysUser = query().eq("name", name).one();
        return Result.ok("success",sysUser);
    }

    @Override
    public Result upDate(String name, String password) {
        SysUser sysUser = query().eq("name", name).one();
        sysUser.setPassword(password);
        boolean b = updateById(sysUser);
        if (b) {
            return Result.ok("更新成功");
        }
        return Result.fail("更新失败");
    }

    @Override
    public Result selectAll() {
        List<SysUser> list = baseMapper.selectByMap(Map.of());
        if (list.size() == 0) {
            return Result.fail("暂无管理员信息");
        }
        return Result.ok("success",list);
    }

    @Override
    public Result deleteById(Long id) {
        boolean b = removeById(id);
        if (b) {
            return Result.ok("删除成功");
        }
        return Result.fail("删除失败");
    }

    @Override
    public Result updateAdmin(String name) {
        SysUser sysUser = query().eq("name", name).one();
        boolean b = updateById(sysUser);
        if (b) {
            return Result.ok("设置成功");
        }
        return Result.fail("设置失败");
    }

}
