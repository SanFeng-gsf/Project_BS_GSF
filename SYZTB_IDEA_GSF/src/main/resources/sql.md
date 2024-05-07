# 管理员信息
CREATE TABLE `sys_user`  (
`id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
`password` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '密码，加密存储',
`name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '账号',
`admin` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '角色',
`create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
`update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
PRIMARY KEY (`id`) USING BTREE

) ENGINE = InnoDB AUTO_INCREMENT = 1010 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;

# 用户信息
CREATE TABLE `tb_user`  (
`id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
`phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '手机号码',
`password` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '密码，加密存储',
`nick_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '昵称，默认是用户id',
`name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '公司名称',
`people_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '公司法定代表人',
`icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '人物头像',
`money` int(20) COMMENT '公司注册资金',
`year` int(20) COMMENT '公司成立时长',
`project_number` int(20) COMMENT '公司竞标成功并完成的项目个数',
`number` int(20) COMMENT '投标公司目前规模 (人数)',
`address` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '公司地址',
`ban` int(20) DEFAULT 0 COMMENT '是否禁用账户',
`create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
`update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
PRIMARY KEY (`id`) USING BTREE,
UNIQUE INDEX `uniqe_key_phone`(`phone`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1010 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;

# 招标信息
CREATE TABLE `zhao_b`  (
`id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
`phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '招标公司联系电话',
`name` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '公司名称',
`people_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '公司法定代表人',
`project_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '招标项目名称',
`ex` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '招标信息说明',
`price` int(20) NOT NULL COMMENT '招标最高价格',
`close` int(20) DEFAULT 0 COMMENT '禁止招标',
`end_time` timestamp NOT NULL COMMENT '招标截止时间',
`create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
`update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
PRIMARY KEY (`id`) USING BTREE

) ENGINE = InnoDB AUTO_INCREMENT = 1010 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;

# 暂未通过的招标信息
CREATE TABLE `zhao_b_un`  (
`id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
`phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '招标公司联系电话',
`name` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '公司名称',
`people_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '公司法定代表人',
`project_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '招标项目名称',
`ex` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '招标信息说明',
`price` int(20) NOT NULL COMMENT '招标最高价格',
`pass` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '审核状态',
`end_time` timestamp NOT NULL COMMENT '招标截止时间',
`create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
`update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
PRIMARY KEY (`id`) USING BTREE

) ENGINE = InnoDB AUTO_INCREMENT = 1010 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;

# 投标信息
CREATE TABLE `tou_b`  (
`id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
`phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '投标公司联系电话',
`name` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '公司名称',
`people_name` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '投标公司法定代表人',
`suo_name` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '所投公司名称',
`project_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '投标项目名称',
`money` int(20) COMMENT '投标公司注册资金',
`year` int(20) COMMENT '投标公司成立时长',
`project_number` int(20) COMMENT '投标公司竞标成功并完成的项目个数',
`number` int(20) COMMENT '投标公司目前规模 (人数)',
`price` int(20) NOT NULL COMMENT '投标价格',
`success` boolean DEFAULT false,
`create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
`update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1010 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;

# 举报信息
CREATE TABLE `ju_bao`  (
`id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
`why` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '举报原因标识字段',
`sm` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '公司法定代表人',
`my_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '您的公司名称',
`name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '举报公司名称',
`project_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '举报项目名称',
`resource` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '其他原因',
`file_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '举报文件名称',
`hide` boolean DEFAULT false COMMENT '是否匿名',
`create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
PRIMARY KEY (`id`) USING BTREE

) ENGINE = InnoDB AUTO_INCREMENT = 1010 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;

# 举报原因表
CREATE TABLE `ju_why`  (
`id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
`why` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '举报原因标识字段',
`zj` boolean DEFAULT false COMMENT '公司相关信息作假',
`bs` boolean DEFAULT false COMMENT '公司招标信息不实',
`wf` boolean DEFAULT false COMMENT '公司招标违反公平公正原则',
`other` boolean DEFAULT false COMMENT '其他',
PRIMARY KEY (`id`) USING BTREE

) ENGINE = InnoDB AUTO_INCREMENT = 1010 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;

# 举报文件
CREATE TABLE `file`  (
`id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
`name` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '文件名称',
`type` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '文件类型',
`size` bigint(0) NULL DEFAULT NULL COMMENT '大小',
`url` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '文件路径',
`is_delete` tinyint(1) NULL DEFAULT NULL COMMENT '是否删除',
`enable` tinyint(1) NULL DEFAULT NULL COMMENT '是否启用',
`md5` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'md5值',
PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '系统文件表' ROW_FORMAT = Dynamic;

# 用户头像文件
CREATE TABLE `icon_file`  (
`id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
`name` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '文件名称',
`size` bigint(0) NULL DEFAULT NULL COMMENT '大小',
`url` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '文件路径',
`md5` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'md5值',
PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '系统文件表' ROW_FORMAT = Dynamic;

use bs;
drop table tb_user;
drop table zhao_b;
drop table zhao_b_un;
drop table tou_b;
drop table ju_bao;
drop table sys_user;

select id,phone,name,people_name,project_name,ex,price,create_time,update_time
from zhao_b where name ='sss';

update zhao_b set close = false where name = 'sanf' and project_name = 'sss';