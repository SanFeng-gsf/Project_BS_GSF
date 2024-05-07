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
@TableName("tb_user")
public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 密码，加密存储
     */
    private String password;

    /**
     * 昵称，默认是随机字符
     */
    private String nickName;

    /**
     * 公司名称
     */
    private String name;

    /**
     * 公司法定代表人
     */
    private String peopleName;

    /**
     * 用户头像
     */
    private String icon;

    // 注册资金
    private int money;

    // 成立时间
    private int year;

    // 竞标成功项目
    private int projectNumber;

    // 公司规模(人数)
    private int number;

    // 公司地址
    private String address;

    // 是否禁用账户 0 不禁止  1 禁止
    private int ban;

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
