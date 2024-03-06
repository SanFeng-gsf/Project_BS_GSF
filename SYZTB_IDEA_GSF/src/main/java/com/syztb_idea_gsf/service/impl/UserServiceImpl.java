package com.syztb_idea_gsf.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.BCrypt;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.syztb_idea_gsf.dto.Result;
import com.syztb_idea_gsf.dto.UserDTO;
import com.syztb_idea_gsf.entity.LoginFormDTO;
import com.syztb_idea_gsf.entity.User;
import com.syztb_idea_gsf.mapper.UserMapper;
import com.syztb_idea_gsf.service.IUserService;
import com.syztb_idea_gsf.utils.CacheClient;
import com.syztb_idea_gsf.utils.RedisConstants;
import com.syztb_idea_gsf.utils.RegexUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        return Result.ok();
    }

    @Override
    public Result login(LoginFormDTO loginForm) {
        String phone = loginForm.getPhone();
        if (RegexUtils.isPhoneInvalid(phone)) {
            return Result.fail("手机号无效");
        }
        String password = loginForm.getPassword();
        String code = loginForm.getCode();
        if (code != null && password == null) {
            String catheCode = stringRedisTemplate.opsForValue().get(LOGIN_CODE_KEY + phone);
            if (catheCode == null || !catheCode.equals(code)) {
                return Result.fail("验证码错误");
            }
        }
        if (code == null && password != null) {
            String cathePassword = stringRedisTemplate.opsForValue().get(LOGIN_PASSWORD_KEY + phone);
            if(cathePassword!=null){
                // 先将前端加密后的密码解密 再与缓存中的密码比较
                // ？
                if(!password.equals(cathePassword)){
                    return Result.fail("密码错误");
                }
            }else {
                User user = query().eq("phone", phone).one();
                // List<User> user = userMapper.selectByMap(Map.of("phone", phone));
                if (user == null) {
                    // 该用户为新用户，重新创建 (前端需获取进行判断)
                    return Result.ok(-1);
                }
                if(!password.equals(user.getPassword())){
                    return Result.fail("密码错误");
                }
                // 将密码存入缓存中
                stringRedisTemplate.opsForValue().set(LOGIN_PASSWORD_KEY + phone,password,LOGIN_PASSWORD_TTL,TimeUnit.MINUTES);
            }

            // bcrypt 加密 (不能解密) 注册时加密存入数据库
//            if(!BCrypt.checkpw(password,newPassword)){
//                return Result.fail("密码错误");
//            }

            // hutool 加密解密 (待完成)
//            byte[] key = SecureUtil.generateKey(SymmetricAlgorithm.AES.getValue()).getEncoded();
//            AES aes = SecureUtil.aes(key);
//            String encryptHex = aes.encryptHex(password);
//            String decryptStr = aes.decryptStr(password);

        }
        User user = query().eq("phone", phone).one();
        if (user == null) {
            // 该用户为新用户，重新创建 (前端需获取进行判断)
            return Result.ok(-1);
        }
        // 设置 token 作为登入令牌
        // setToken(user);
        return Result.ok(setToken(user));
    }

    @Override
    public Result loginByName(String name, String password) {
        // 先将前端加密后的密码解密 再与缓存中的密码比较
        // ？
        String key = LOGIN_NAME_PASSWORD_KEY + name;
        String cathePassword = stringRedisTemplate.opsForValue().get(key);
        User user = null;
        if(cathePassword==null){
            user = query().eq("name", name).one();
            if(user==null){
                // 该用户为新用户，重新创建 (前端需获取进行判断)
                return Result.ok(-1);
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
        return Result.ok(setToken(user));
    }

    /**
     * 设置 token
     */
    private String setToken(User user){
        // BeanUtil 糊涂里面的工具类, copy 复制信息
        // redis 保存 提前生成一个随机 token 作为登入令牌
        String token = UUID.randomUUID().toString();
        UserDTO userDTO = BeanUtil.copyProperties(user, UserDTO.class);
        Map<String, Object> userMap = BeanUtil.beanToMap(userDTO, new HashMap<>(),
                CopyOptions.create().setIgnoreNullValue(true)
                        .setFieldValueEditor((fieldName, fieldValue) -> fieldValue.toString()));
        String tokenKey = LOGIN_USER_KEY + token;
        stringRedisTemplate.opsForHash().putAll(tokenKey, userMap);
        // 设置 token 有效期
        Boolean expire = stringRedisTemplate.expire(tokenKey, LOGIN_USER_TTL, TimeUnit.MINUTES);
        if(expire){
            return tokenKey;
        }
        return null;
    }

    @Override
    public Result logout(HttpServletRequest request) {
        String token = request.getHeader("authorization");
        String key = RedisConstants.LOGIN_USER_KEY + token;
        stringRedisTemplate.delete(key);
        return Result.ok();
    }

    @Override
    public Result selectByPhone(String phone) {
        User user = cacheClient.queryWithLogicalExpire(CACHE_USER_KEY,phone,User.class,
                s->query().eq("phone",phone).one(),CACHE_USER_TTL,TimeUnit.MINUTES);
        return Result.ok(user);
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
        return Result.ok();
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
        return Result.ok();
    }

}
