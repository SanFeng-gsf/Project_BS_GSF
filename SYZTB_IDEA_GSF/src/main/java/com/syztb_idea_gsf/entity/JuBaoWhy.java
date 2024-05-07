package com.syztb_idea_gsf.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data // 省略方法
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true) // 对应字段的setter方法调用后，会返回当前对象。
@TableName("ju_why")
public class JuBaoWhy {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    // 举报原因标识字段
    private String why;

    // 公司相关信息作假
    private boolean zj;

    // 公司招标信息不实
    private boolean bs;

    // 公司招标违反公平公正原则
    private boolean wf;

    // 其他
    private boolean other;
}
