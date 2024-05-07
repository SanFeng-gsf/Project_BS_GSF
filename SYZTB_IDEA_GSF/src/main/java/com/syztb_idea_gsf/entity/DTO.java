package com.syztb_idea_gsf.entity;

import lombok.Data;

@Data
public class DTO {

    private Long id;
    /**
     * 公司名称
     */
    private String name;

    /**
     * 所投公司名称
     */
    private String suoName;

    /**
     * 招标项目名称
     */
    private String projectName;

    /**
     * 是否临时禁止招标项目 默认false
     */
    private boolean close;

}
