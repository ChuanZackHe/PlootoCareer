use mes3;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS mes3.file_record;
CREATE TABLE mes3.file_record
(
    `id`              bigint       NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `file_name`       varchar(512) NULL     DEFAULT NULL COMMENT '文件名称',
    `object_name`     varchar(512)       NOT NULL COMMENT 'oss对象名称',
    `bucket_name`     varchar(512) NOT NULL COMMENT '存储桶名',
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
