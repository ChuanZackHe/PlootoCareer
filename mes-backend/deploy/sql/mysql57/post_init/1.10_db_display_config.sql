use mes3;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS mes3.display_config;
CREATE TABLE mes3.display_config
(
    `id`              bigint       NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `table_name`      varchar(255) NOT NULL COMMENT '表名',
    `config`          json NULL COMMENT '数据表展示配置',
    `create_time`     datetime(3) NULL DEFAULT NULL COMMENT '创建时间',
    `create_username` varchar(255) NULL DEFAULT NULL COMMENT '创建用户名',
    `update_time`     datetime(3) NULL DEFAULT NULL COMMENT '更新时间',
    `update_username` varchar(255) NULL DEFAULT NULL COMMENT '更新用户名',
    `tenant_id`       varchar(64)  NOT NULL COMMENT '租户id',
    `is_deleted`      datetime(3) NOT NULL DEFAULT '1970-01-01 00:00:00.000' COMMENT '逻辑删除位，‘1970-01-01 00:00:00’为未删除，其余为删除',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_index` (`tenant_id`, `table_name`) USING BTREE
) ENGINE = InnoDB
    AUTO_INCREMENT = 1
	CHARACTER SET = utf8mb4
	COLLATE = utf8mb4_unicode_ci COMMENT = '记录不同租户的数据表展示配置'
	ROW_FORMAT = Dynamic;