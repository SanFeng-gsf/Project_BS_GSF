package com.syztb_idea_gsf.entity;

import lombok.Data;

@Data
public class LoginFormDTO {
    /**
     * 登入参数
     */
    private String phone;
    private String code;
    private String password;
    /**
     * 公司名称
     */
    private String name;
}
