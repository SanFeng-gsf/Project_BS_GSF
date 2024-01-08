package com.syztb_idea_gsf.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.syztb_idea_gsf.dto.Result;
import com.syztb_idea_gsf.entity.LoginFormDTO;
import com.syztb_idea_gsf.entity.User;

import javax.servlet.http.HttpServletRequest;


public interface IUserService extends IService<User> {

    Result sendCode(String phone);

    Result login(LoginFormDTO loginForm);

    Result loginByName(String name, String password);

    Result logout(HttpServletRequest request);

    Result selectByPhone(String phone);

    Result insert(User user);


}
