package com.syztb_idea_gsf.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 举报文件表
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true) // 对应字段的setter方法调用后，会返回当前对象。
@TableName("file")
public class MyFile {
    @TableId(value = "id", type = IdType.AUTO)
    private int id;
    private String name;
    private String type;
    private long size;
    private String url;
    @TableField("is_delete")
    private boolean isDelete;
    private boolean enable;
    private String md5;
}
