package com.syztb_idea_gsf.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.syztb_idea_gsf.dto.Result;
import com.syztb_idea_gsf.dto.UserDTO;
import com.syztb_idea_gsf.entity.LoginFormDTO;
import com.syztb_idea_gsf.entity.User;
import com.syztb_idea_gsf.mapper.UserMapper;
import com.syztb_idea_gsf.service.IUserService;
import com.syztb_idea_gsf.utils.CacheClient;
import com.syztb_idea_gsf.utils.RegexUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.syztb_idea_gsf.utils.RedisConstants.*;
import static com.syztb_idea_gsf.utils.SystemConstants.USER_NICK_NAME_PREFIX;

@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private CacheClient cacheClient;

    @Override
    public Result sendCode(String phone) {
        // 校验手机号  是否是无效的
        if (RegexUtils.isPhoneInvalid(phone)) {
            return Result.fail("手机号无效");
        }
        // 生成随机数字验证码
        String code = RandomUtil.randomNumbers(6);
        String key = LOGIN_CODE_KEY + phone;
        // 将验证码保存到redis中
        stringRedisTemplate.opsForValue().set(key, code, LOGIN_CODE_TTL, TimeUnit.MINUTES);
        // 先使用日志显示
        log.debug("发送短信验证码成功,验证码：{}", code);
        return Result.ok("验证码发送成功");
    }

    @Override
    public Result login(LoginFormDTO loginForm) {
        String phone = loginForm.getPhone();
        String name = loginForm.getName();
        String password = loginForm.getPassword();
        String code = loginForm.getCode();
        if (code != null && password == null) {
            String catheCode = stringRedisTemplate.opsForValue().get(LOGIN_CODE_KEY + phone);
            if (catheCode == null || !catheCode.equals(code)) {
                return Result.fail("验证码错误");
            }
        }
        if (code == null && password != null) {
            String cathePassword = stringRedisTemplate.opsForValue().get(LOGIN_PASSWORD_KEY + name);
            User user = query().eq("name", name).one();
            User user1 = new User();
            String token;
            String id;
            if(cathePassword!=null){
                if(!password.equals(cathePassword)){
                    return Result.fail("密码错误");
                }
                token = setToken(user);
                if(token==null){
                    return Result.fail("获取 token 失败");
                }
                id = user.getId().toString();
                Map<String, String> map = Map.of("token", token,"userId",id);
                return Result.ok("登入成功",map);
            }else {
                if (user != null) {
                    if(!password.equals(user.getPassword())){
                        return Result.fail("密码错误");
                    }
                    token = setToken(user);
                    id = user.getId().toString();
                } else {
                    // 该用户为新用户，重新创建 手机号密码登入
                    user1.setName(name);
                    user1.setPassword(password);
                    save(user1);
                    token = setToken(user1);
                    id = user1.getId().toString();
                }
                if(token==null){
                    return  Result.fail("获取 token 失败");
                }
                // 将密码存入缓存中
                stringRedisTemplate.opsForValue().set(LOGIN_PASSWORD_KEY + name,password,LOGIN_PASSWORD_TTL,TimeUnit.MINUTES);
                Map<String, String> map = Map.of("token", token,"userId",id);
                return Result.ok("登入成功",map);
            }
        }
        User user = query().eq("phone", phone).one();
        if (user == null) {
            // 该用户为新用户，重新创建 手机号验证码登入
            User user1 = new User();
            user1.setPhone(phone);
            save(user1);
            user = user1;
        }
        // 设置 token 作为登入令牌
        // setToken(user);
        if(setToken(user)==null){
            return  Result.fail("获取 token 失败");
        }
        Map<String, String> map = Map.of("token", Objects.requireNonNull(setToken(user)),"userId",user.getId().toString());
        return Result.ok("登入成功",map);
    }

    @Override
    public Result loginByName(String name, String password) {
        // 先将前端加密后的密码解密 再与缓存中的密码比较
        // ？
        String key = LOGIN_NAME_PASSWORD_KEY + name;
        String cathePassword = stringRedisTemplate.opsForValue().get(key);
        User user = new User();
        if(cathePassword==null){
            user = query().eq("name", name).one();
            if(user==null){
                // 该用户为新用户，重新创建 名称密码登入
                User user1 = new User();
                user1.setPhone(name);
                user1.setPassword(password);
                save(user1);
                user = user1;
            }
            cathePassword = user.getPassword();
            if(!password.equals(cathePassword)){
                return Result.fail("密码错误");
            }
            stringRedisTemplate.opsForValue().set(key,password,LOGIN_NAME_PASSWORD_TTL,TimeUnit.MINUTES);
        }else {
            if(!password.equals(cathePassword)){
                return Result.fail("密码错误");
            }
        }
        // 设置 token 作为登入令牌
        // setToken(user);
        // 将 token 传回前端
        Map<String, String> map = Map.of("token", Objects.requireNonNull(setToken(user)),"userId",user.getId().toString());
        return Result.ok("登入成功",map);
    }

    /**
     * 设置 token
     */
    private String setToken(User user){
        if (user.getId() == null) {
            return null;
        }
        String token = UUID.randomUUID().toString();
        String tokenKey = LOGIN_USER_KEY + token;
        stringRedisTemplate.opsForHash().put(tokenKey,"id",user.getId().toString());
        // 设置 token 有效期
        boolean expire = Boolean.TRUE.equals(stringRedisTemplate.expire(tokenKey, LOGIN_USER_TTL, TimeUnit.MINUTES));
        if(expire){
            return tokenKey;
        }
        return null;
    }

    @Override
    public Result logout(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
//        String key = RedisConstants.LOGIN_USER_KEY + token;
        stringRedisTemplate.delete(token);
        return Result.ok("删除成功");
    }

    @Override
    public Result selectByPhone(String phone) {
        User user = cacheClient.queryWithLogicalExpire(CACHE_USER_KEY,phone,User.class,
                s->query().eq("phone",phone).one(),CACHE_USER_TTL,TimeUnit.MINUTES);
        return Result.ok("查询成功",user);
    }

    @Override
    public Result insert(User user) {
        String phone = user.getPhone();
        if (RegexUtils.isPhoneInvalid(phone)) {
            return Result.fail("手机号无效");
        }
        User sqluser = query().eq("phone",phone).one();
        if(sqluser!=null){
            return Result.fail("该手机号已被绑定，请更换新的手机号");
        }
        String name = user.getName();
        User user1 = query().eq("name", name).one();
        if(user1!=null){
            return Result.fail("该公司已注册");
        }
        if(user.getNickName()==null){
            user.setNickName(USER_NICK_NAME_PREFIX + RandomUtil.randomString(10));
        }
        // Mysql 数据库创建用户
        save(user);
        return Result.ok("注册成功");
    }

    @Override
    public Result updateMe(User user) {
        // 注意密码处理 (还未处理)
        // ？
        Long id = user.getId();
        User sqluser = query().eq("id", id).one();
        String sqlPhone = sqluser.getPhone();
        String sqlName = sqluser.getName();
        // 判断 手机号与公司名称是否修改
        if(!user.getPhone().equals(sqlPhone)){
            if (RegexUtils.isPhoneInvalid(sqlPhone)) {
                return Result.fail("手机号无效");
            }
            User user1 = query().eq("phone", user.getPhone()).one();
            if(user1!=null){
                return Result.fail("该手机号已被绑定，请更换新的手机号");
            }
        }
        if(!user.getName().equals(sqlName)){
            User user1 = query().eq("name", user.getName()).one();
            if(user1!=null){
                return Result.fail("该公司已注册，请更换公司名称");
            }
        }
        saveOrUpdate(user);
        String key = CACHE_USER_KEY + sqlPhone;
        stringRedisTemplate.delete(key);
        key = LOGIN_NAME_PASSWORD_KEY + sqlName;
        stringRedisTemplate.delete(key);
        return Result.ok("更新成功");
    }

    @Override
    public Result selectById(Long id) {
        User user = query().eq("id", id).one();
        return Result.ok("success",user);
    }

    @Override
    public Result getUser(String name) {
        List<User> user;
        if (name == null) {
            user = baseMapper.selectByMap(Map.of());
        } else {
            user = baseMapper.selectByMap(Map.of("name",name));
        }
        if(user.size()>0){
            return Result.ok("success",user);
        }
        return Result.fail("未查到该公司");
    }

    @Override
    public Result updateUser(Integer id, int ban) {
        User user = query().eq("id", id).one();
        user.setBan(ban);
        boolean result = updateById(user);
        if (!result) {
            return Result.fail("更新失败");
        }
        stringRedisTemplate.delete(CACHE_USER_KEY + user.getPhone());
        stringRedisTemplate.delete(LOGIN_NAME_PASSWORD_KEY + user.getName());
        return Result.ok("更新成功");
    }

}
