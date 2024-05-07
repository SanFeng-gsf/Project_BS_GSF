package com.syztb_idea_gsf.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 举报信息 获取前端数据
 */
@Data // 省略方法
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true) // 对应字段的setter方法调用后，会返回当前对象。
@TableName("ju_bao")
public class JuBaoSql {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    // 举报原因标识字段
    private String why;

    // 举报说明
    private String sm;

    // 您的公司名称
    private String myName;

    // 举报公司名称
    private String name;

    // 举报项目
    private String projectName;

    // 其他原因
    private String resource;

    // 举报文件名称
    private String fileName;

    // 是否匿名
    private boolean hide;

    // 创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" , timezone = "GMT+08:00")
    private LocalDateTime createTime;
}
