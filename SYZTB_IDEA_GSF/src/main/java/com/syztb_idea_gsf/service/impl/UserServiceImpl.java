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

//    @Autowired
//    private UserMapper userMapper;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

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
            User user = query().eq("phone", phone).one();
//            List<User> user = userMapper.selectByMap(Map.of("phone", phone));
            if (user == null) {
                // 该用户为新用户，重新创建 (前端需获取进行判断)
                return Result.ok(-1);
            }
            if(!password.equals(user.getPassword())){
                return Result.fail("密码错误");
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
        stringRedisTemplate.expire(tokenKey, LOGIN_USER_TTL, TimeUnit.MINUTES);
        return Result.ok();
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
        User user = query().eq("phone",phone).one();
        return Result.ok(user);
    }

    @Override
    public Result insert(User user) {
        String phone = user.getPhone();
        if (RegexUtils.isPhoneInvalid(phone)) {
            return Result.fail("手机号无效");
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

}
