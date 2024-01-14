package com.syztb_idea_gsf.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 投标信息
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true) // 对应字段的setter方法调用后，会返回当前对象。
@TableName("tou_b")
public class TouB implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 投标公司联系电话
     */
    private String phone;

    /**
     * 投标公司名称
     */
    private String name;

    /**
     * 投标公司法定代表人
     */
    private String peopleName;

    /**
     * 所投公司的名称
     */
    private String suoName;

    /**
     * 投标项目名称
     */
    private String projectName;

    /**
     * 投标公司注册资金
     */
    private int money;

    /**
     * 投标公司成立时长
     */
    private int year;

    /**
     * 投标公司竞标成功并完成的项目个数
     */
    private int projectNumber;

    /**
     * 投标公司目前规模 (人数)
     */
    private int number;

    /**
     * 投标价格
     */
    private int price;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}
