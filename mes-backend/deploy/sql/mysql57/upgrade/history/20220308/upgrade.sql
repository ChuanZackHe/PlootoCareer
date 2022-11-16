use mes3;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

alter table prd_task
    add column `assign_user_id` bigint default -1 COMMENT '绑定用户id';
update prd_task set `assign_user_id`=-1;

alter table prd_task
    add column `assign_user_real_name` varchar(50) DEFAULT 'NA' COMMENT '绑定用户真实姓名';

alter table prd_job
    add column `user_define_info` json COMMENT '用户自定义备注信息，key:val格式的json';
alter table prd_task
    add column `user_define_info` json COMMENT '用户自定义备注信息，key:val格式的json';

alter table craft_component
    add column `verify_role_id` bigint NULL DEFAULT NULL COMMENT '完成后二次检验角色id，默认为空';
alter table craft_flow_node
    add column `verify_role_id` bigint NULL DEFAULT NULL COMMENT '完成后二次检验角色id，默认为空';
alter table prd_task
    add column `verify_role_id` bigint NULL DEFAULT NULL COMMENT '完成后二次检验角色id，默认为空';


alter table prd_task
    add column `priority` bigint DEFAULT 0 COMMENT '优先级，数字越大优先级越高，默认为0，继承job的优先级';
update prd_task set priority=0;


alter table prd_job
    add column `priority` bigint DEFAULT 0 COMMENT '优先级，数字越大优先级越高，默认为0';
update prd_job set priority=0;

alter table prd_task
    add column `output_batch_num` varchar(64) DEFAULT NULL COMMENT '手动指定的输出成品批次号';
alter table prd_job
    add column `output_batch_num` varchar(64) DEFAULT NULL COMMENT '手动指定的输出成品批次号';
alter table sys_perm
    change column `module` `module` varchar(255) NULL COMMENT '模块类型';

# alter table wms_stock_detail
#     add column `lock_status` varchar(32) default "UNLOCKED" COMMENT '状态';

DROP TABLE IF EXISTS mes3.file_record;
CREATE TABLE mes3.file_record
(
    `id`              bigint       NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `file_name`       varchar(512) NULL     DEFAULT NULL COMMENT '文件名称',
    `object_name`     varchar(512)       NOT NULL COMMENT 'oss对象名称',
    `bucket_name`     varchar(512) NOT NULL COMMENT '需求数量',
    `module`          varchar(64)  NOT NULL COMMENT '模块',
    `reference_id`    varchar(512) NOT NULL comment '索引的单据号或者是数据库ID',
    `description`     text         NULL COMMENT '描述',
    `create_time`     datetime(3)  NULL     DEFAULT NULL COMMENT '创建时间',
    `create_username` varchar(255) NULL     DEFAULT NULL COMMENT '创建用户名',
    `update_time`     datetime(3)  NULL     DEFAULT NULL COMMENT '更新时间',
    `update_username` varchar(255) NULL     DEFAULT NULL COMMENT '更新用户名',
    `tenant_id`       varchar(64)  NOT NULL COMMENT '租户id',
    `is_deleted`      datetime(3)  NOT NULL DEFAULT '1970-01-01 00:00:00.000' COMMENT '逻辑删除位，‘1970-01-01 00:00:00’为未删除，其余为删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
    comment '通用文件记录表';

DROP TABLE IF EXISTS mes3.sys_tenant_info;
CREATE TABLE mes3.sys_tenant_info
(
    `id`              bigint       NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `tenant`          varchar(64)  NOT NULL COMMENT '租户id',
    `info`            json COMMENT '其他信息',
    `create_time`     datetime(3)  NULL     DEFAULT NULL COMMENT '创建时间',
    `create_username` varchar(255) NULL     DEFAULT NULL COMMENT '创建用户名',
    `update_time`     datetime(3)  NULL     DEFAULT NULL COMMENT '更新时间',
    `update_username` varchar(255) NULL     DEFAULT NULL COMMENT '更新用户名',
    `tenant_id`       varchar(64)  NOT NULL COMMENT '租户id',
    `is_deleted`      datetime(3)  NOT NULL DEFAULT '1970-01-01 00:00:00.000' COMMENT '逻辑删除位，‘1970-01-01 00:00:00’为未删除，其余为删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
    comment '租户额外信息表';

DROP TABLE IF EXISTS mes3.sys_user_info;
CREATE TABLE mes3.sys_user_info
(
    `id`              bigint       NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `user_id`         bigint   NOT NULL COMMENT '用户id',
    `info`            json COMMENT '其他信息',
    `create_time`     datetime(3)  NULL     DEFAULT NULL COMMENT '创建时间',
    `create_username` varchar(255) NULL     DEFAULT NULL COMMENT '创建用户名',
    `update_time`     datetime(3)  NULL     DEFAULT NULL COMMENT '更新时间',
    `update_username` varchar(255) NULL     DEFAULT NULL COMMENT '更新用户名',
    `tenant_id`       varchar(64)  NOT NULL COMMENT '租户id',
    `is_deleted`      datetime(3)  NOT NULL DEFAULT '1970-01-01 00:00:00.000' COMMENT '逻辑删除位，‘1970-01-01 00:00:00’为未删除，其余为删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
    comment '用户额外信息表';

DROP TABLE IF EXISTS mes3.sys_role_info;
CREATE TABLE mes3.sys_role_info
(
    `id`              bigint       NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `role_id`         bigint   NOT NULL COMMENT '角色id',
    `info`            json COMMENT '其他信息',
    `create_time`     datetime(3)  NULL     DEFAULT NULL COMMENT '创建时间',
    `create_username` varchar(255) NULL     DEFAULT NULL COMMENT '创建用户名',
    `update_time`     datetime(3)  NULL     DEFAULT NULL COMMENT '更新时间',
    `update_username` varchar(255) NULL     DEFAULT NULL COMMENT '更新用户名',
    `tenant_id`       varchar(64)  NOT NULL COMMENT '租户id',
    `is_deleted`      datetime(3)  NOT NULL DEFAULT '1970-01-01 00:00:00.000' COMMENT '逻辑删除位，‘1970-01-01 00:00:00’为未删除，其余为删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
    comment '角色额外信息表';

# 订单子单添加用户定义的信息
alter table order_detail
    add column `user_define_info` json COMMENT '用户自定义备注信息，key:val格式的json';
# 交付子单添加用户定义的信息
alter table order_delivery_detail
    add column `user_define_info` json COMMENT '用户自定义备注信息，key:val格式的json';

alter table order_summary
    change column `price` `price` double NULL COMMENT '价格';

alter table order_detail
    change column `price` `price` double NULL COMMENT '价格';

alter table wms_stock_out_summary
    change column `customer` `customer` varchar(255) NULL COMMENT '订货人';