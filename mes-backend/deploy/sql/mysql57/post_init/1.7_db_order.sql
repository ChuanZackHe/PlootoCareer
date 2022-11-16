SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

use mes3;

DROP TABLE IF EXISTS mes3.order_delivery_detail;
CREATE TABLE mes3.order_delivery_detail
(
    `id`              bigint       NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `document_id`     varchar(255) NOT NULL COMMENT '交付单总单编码',
    `material_name`   varchar(64)  NOT NULL COMMENT '物料名称',
    `order_detail_id` bigint       NOT NULL COMMENT '订单子单id',
    `material_id`     bigint       NOT NULL COMMENT '物料id',
    `count`           double       NOT NULL COMMENT '需求数量',
    `status`          varchar(64)  NOT NULL comment '状态',
    `description`     text         NULL COMMENT '描述',
    `create_time`     datetime(3)  NULL     DEFAULT NULL COMMENT '创建时间',
    `create_username` varchar(255) NULL     DEFAULT NULL COMMENT '创建用户名',
    `update_time`     datetime(3)  NULL     DEFAULT NULL COMMENT '更新时间',
    `update_username` varchar(255) NULL     DEFAULT NULL COMMENT '更新用户名',
    `tenant_id`       varchar(64)  NOT NULL COMMENT '租户id',
    `is_deleted`      datetime(3)  NOT NULL DEFAULT '1970-01-01 00:00:00.000' COMMENT '逻辑删除位，‘1970-01-01 00:00:00’为未删除，其余为删除',
    `user_define_info` json COMMENT '用户自定义备注信息，key:val格式的json',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
    comment '交付子单表';

DROP TABLE IF EXISTS mes3.order_delivery_summary;
CREATE TABLE mes3.order_delivery_summary
(
    `id`                 bigint       NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `document_id`        varchar(255) NOT NULL COMMENT '交付单总单编码',
    `status`             varchar(64)  NOT NULL COMMENT '状态',
    `delivery_date`      datetime(3)  NOT NULL COMMENT '交付日期',
    `order_summary_code` varchar(255) NOT NULL COMMENT '订单总单编码',
    `description`        text         NULL COMMENT '描述',
    `create_time`        datetime(3)  NULL     DEFAULT NULL COMMENT '创建时间',
    `create_username`    varchar(255) NULL     DEFAULT NULL COMMENT '创建用户名',
    `update_time`        datetime(3)  NULL     DEFAULT NULL COMMENT '更新时间',
    `update_username`    varchar(255) NULL     DEFAULT NULL COMMENT '更新用户名',
    `tenant_id`          varchar(64)  NOT NULL COMMENT '租户id',
    `is_deleted`         datetime(3)  NOT NULL DEFAULT '1970-01-01 00:00:00.000' COMMENT '逻辑删除位，‘1970-01-01 00:00:00’为未删除，其余为删除',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_index` (`document_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
    comment '交付总单表';

DROP TABLE IF EXISTS mes3.order_detail;
CREATE TABLE mes3.order_detail
(
    `id`              bigint       NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `document_id`     varchar(255) NOT NULL COMMENT '订单总单编码',
    `material_id`     bigint       NOT NULL COMMENT '物料id',
    `material_name`   varchar(64)  NOT NULL COMMENT '物料名称',
    `price`           double       NULL COMMENT '价格',
    `count`           double       NOT NULL COMMENT '物料数量',
    `status`          varchar(64)  NOT NULL COMMENT '状态',
    `allocated_count` double       NOT NULL COMMENT '已规划数量',
    `description`     text         NULL COMMENT '描述',
    `reason`          varchar(255) NULL COMMENT '关单原因',
    `create_time`     datetime(3)  NULL     DEFAULT NULL COMMENT '创建时间',
    `create_username` varchar(255) NULL     DEFAULT NULL COMMENT '创建用户名',
    `update_time`     datetime(3)  NULL     DEFAULT NULL COMMENT '更新时间',
    `update_username` varchar(255) NULL     DEFAULT NULL COMMENT '更新用户名',
    `tenant_id`       varchar(64)  NOT NULL COMMENT '租户id',
    `is_deleted`      datetime(3)  NOT NULL DEFAULT '1970-01-01 00:00:00.000' COMMENT '逻辑删除位，‘1970-01-01 00:00:00’为未删除，其余为删除',
    `user_define_info` json COMMENT '用户自定义备注信息，key:val格式的json',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
    comment '订单子单表';

DROP TABLE IF EXISTS mes3.order_summary;
CREATE TABLE mes3.order_summary
(
    `id`              bigint       NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `document_id`     varchar(255) NOT NULL COMMENT '订单总单编码',
    `customer_id`     bigint       NOT NULL COMMENT '客户id',
    `customer_name`   varchar(64)  NOT NULL COMMENT '客户名称',
    `price`           double       NULL COMMENT '价格',
    `closing_date`    datetime(3)  NOT NULL COMMENT '截止日期',
    `status`          varchar(64)  NOT NULL COMMENT '状态',
    `description`     text         NULL COMMENT '额外信息',
    `create_time`     datetime(3)  NULL     DEFAULT NULL COMMENT '创建时间',
    `create_username` varchar(255) NULL     DEFAULT NULL COMMENT '创建用户名',
    `update_time`     datetime(3)  NULL     DEFAULT NULL COMMENT '更新时间',
    `update_username` varchar(255) NULL     DEFAULT NULL COMMENT '更新用户名',
    `tenant_id`       varchar(64)  NOT NULL COMMENT '租户id',
    `is_deleted`      datetime(3)  NOT NULL DEFAULT '1970-01-01 00:00:00.000' COMMENT '逻辑删除位，‘1970-01-01 00:00:00’为未删除，其余为删除',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_index` (`document_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
    comment '订单总单表';
