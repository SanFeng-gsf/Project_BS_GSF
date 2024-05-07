package com.syztb_idea_gsf.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * 举报信息 获取前端数据
 */
@Data // 省略方法
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true) // 对应字段的setter方法调用后，会返回当前对象。
public class JuBao {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    // 举报说明
    private String sm;

    // 您的公司名称
    private String myName;

    // 举报公司名称
    private String name;

    // 举报项目
    private String projectName;

    // 举报原因
    private ArrayList<String> type;

    // 其他原因
    private String resource;

    // 举报文件名称
    private ArrayList<String> fileName;

    // 是否匿名
    private boolean hide;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" , timezone = "GMT+08:00")
    private LocalDateTime createTime;

}
