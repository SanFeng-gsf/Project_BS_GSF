package com.syztb_idea_gsf.utils;

import com.syztb_idea_gsf.dto.UserDTO;

/**
 * 保存用户信息
 * 保证线程安全
 */
public class UserHolder {

    private static final ThreadLocal<UserDTO> tl = new ThreadLocal<>();

    public static void saveUser(UserDTO user){
        tl.set(user);
    }

    public static UserDTO getUser(){
        return tl.get();
    }

    public static void removeUser(){
        tl.remove();
    }
}
