package com.syztb_idea_gsf.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data // 省略方法
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true) // 对应字段的setter方法调用后，会返回当前对象。
@TableName("sys_user")
public class SysUser implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 昵称，默认是随机字符
     */
    private String name;
    /**
     * 密码，加密存储
     */
    private String password;

    /**
     * 角色 (为 Admin 时有创建管理者权限)
     */
    private String admin;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" , timezone = "GMT+08:00")
    private LocalDateTime createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" , timezone = "GMT+08:00")
    private LocalDateTime updateTime;
}
