package com.syztb_idea_gsf.controller;

import com.syztb_idea_gsf.dto.Result;
import com.syztb_idea_gsf.entity.LoginFormDTO;
import com.syztb_idea_gsf.entity.User;
import com.syztb_idea_gsf.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private IUserService userService;

    /**
     * 发送手机验证码
     */
    @PostMapping("/code")
    public Result sendCode(@RequestParam("phone") String phone) {
        // TODO 发送短信验证码并保存验证码
        return userService.sendCode(phone);
    }

    /**
     * 登录功能
     * @param loginForm 登录参数，包含手机号、验证码；或者手机号、密码
     */
    @PostMapping("/login")
    public Result login(@RequestBody LoginFormDTO loginForm){
        return userService.login(loginForm);
    }

    /**
     * 退出
     */
    @PostMapping("/logout")
    public Result logout(HttpServletRequest request){
        // 未测试 可能存在问题
        return userService.logout(request);
    }

    /**
     * 创建新用户 (密码需加密再传入)
     */
    @PostMapping("/insert")
    public Result insert(@RequestBody User user){
        return userService.insert(user);
    }

    /**
     * 根据手机号查询用户基本信息
     */
    @PostMapping("/selectByPhone")
    public Result selectByPhone(@RequestBody String phone){
        return userService.selectByPhone(phone);
    }
}
