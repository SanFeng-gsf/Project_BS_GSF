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
    public Result sendCode(@RequestBody LoginFormDTO loginForm) {
        // TODO 发送短信验证码并保存验证码
        return userService.sendCode(loginForm.getPhone());
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
     * 公司名称登入 (忘记手机号，可以重新绑定新的手机号)  (需前端先进行判断是否为公司登入)
     * 规定一个手机号只绑定一家公司
     */
    @PostMapping("/loginByName")
    public Result loginByName(@RequestBody LoginFormDTO loginForm){
        return userService.loginByName(loginForm.getName(),loginForm.getPassword());
    }

    /**
     * 退出
     */
    @PostMapping("/logout")
    public Result logout(HttpServletRequest request){
        // 可能存在一点小问题 有了前端再测试
        // 请求头 Header 里面 设置 authorization
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
     * 根据手机号查询用户基本信息 (前端获取登入者的手机号查看个人信息 (我的) )
     */
    @PostMapping("/selectByPhone")
    public Result selectByPhone(@RequestBody LoginFormDTO loginForm){
        return userService.selectByPhone(loginForm.getPhone());
    }

    /**
     * 更新个人信息 (需要根据 id )
     */
    @PostMapping("/updateMe")
    public Result updateMe(@RequestBody User user){
        return userService.updateMe(user);
    }

}
