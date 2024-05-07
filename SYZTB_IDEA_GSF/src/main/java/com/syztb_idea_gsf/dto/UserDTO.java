package com.syztb_idea_gsf.dto;

import lombok.Data;

@Data
public class UserDTO {
    /**
     * 显示的信息，防止隐私泄漏，缓解内存压力
     */
    private Long id;
    private String nickName;
    private String icon;

}
