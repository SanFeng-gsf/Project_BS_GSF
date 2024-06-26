package com.syztb_idea_gsf.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 招标信息
 */
@Data // 省略方法
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true) // 对应字段的setter方法调用后，会返回当前对象。
@TableName("zhao_b")
public class ZhaoB implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 招标公司联系电话
     */
    private String phone;

    /**
     * 招标公司名称
     */
    private String name;

    /**
     * 招标公司法定代表人
     */
    private String peopleName;

    /**
     * 招标项目名称
     */
    private String projectName;

    /**
     * 招标项目说明
     */
    private String ex;

    /**
     * 招标最高价格
     */
    private int price;

    /**
     * 是否临时禁止招标项目 默认 0(不禁用) 1(禁用)
     */
    private int close;

    /**
     * 本项目招标截止时间
     * @JsonFormat 将前端json里面的时间转为对应的 时间类型
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" , timezone = "GMT+08:00")
    private LocalDateTime endTime;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" , timezone = "GMT+08:00")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" , timezone = "GMT+08:00")
    private LocalDateTime updateTime;
}
