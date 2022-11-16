/*
 Navicat Premium Data Transfer

 Source Server         : 桐庐MES_MySQL
 Source Server Type    : MySQL
 Source Server Version : 80025
 Source Host           : 47.115.203.105:33061
 Source Schema         : mes3

 Target Server Type    : MySQL
 Target Server Version : 80025
 File Encoding         : 65001

 Date: 24/09/2021 15:46:00
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for fac_info_resource_group_relation
-- ----------------------------
DROP TABLE IF EXISTS `fac_resource_group_relation`;
CREATE TABLE `fac_resource_group_relation`
(
    `id`                  bigint       NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `code`                varchar(255) NOT NULL COMMENT '工厂资源编码',
    `resource_group_id`   bigint       NOT NULL COMMENT '资源组id',
    `resource_group_name` varchar(255) NOT NULL COMMENT '资源组名称',
    `tenant_id`           varchar(64)  NOT NULL COMMENT '租户id',
    `create_time`         datetime(3)  NULL     DEFAULT NULL COMMENT '创建时间',
    `create_username`     varchar(255) NULL     DEFAULT NULL COMMENT '创建用户名',
    `update_time`         datetime(3)  NULL     DEFAULT NULL COMMENT '更新时间',
    `update_username`     varchar(255) NULL     DEFAULT NULL COMMENT '更新用户名',
    `is_deleted`          datetime(3)  NOT NULL DEFAULT '1970-01-01 00:00:00.000' COMMENT '逻辑删除位，‘1970-01-01 00:00:00’为未删除，其余为删除',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_index` (`code`, `resource_group_id`, `is_deleted`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '工厂资源-资源组关系表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of fac_info_resource_group_relation
-- ----------------------------

-- ----------------------------
-- Table structure for base_fac_resource_group
-- ----------------------------
DROP TABLE IF EXISTS `base_factory_resource_group`;
CREATE TABLE `base_factory_resource_group`
(
    `id`              bigint       NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `name`            varchar(255) NOT NULL COMMENT '组名称',
    `description`     text         NULL COMMENT '组描述',
    `extra`           json COMMENT '其他信息',
    `is_deleted`      datetime(3)  NOT NULL DEFAULT '1970-01-01 00:00:00.000' COMMENT '逻辑删除位，‘1970-01-01 00:00:00’为未删除，其余为删除',
    `tenant_id`       varchar(64)  NOT NULL COMMENT '租户id',
    `create_time`     datetime(3)  NULL     DEFAULT NULL COMMENT '创建时间',
    `create_username` varchar(64)  NULL     DEFAULT NULL COMMENT '创建人',
    `update_time`     datetime(3)  NULL     DEFAULT NULL COMMENT '最后更新时间',
    `update_username` varchar(64)  NULL     DEFAULT NULL COMMENT '最后更新人',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `IU_name_tenant_deleted` (`name`, `is_deleted`, `tenant_id`) USING BTREE COMMENT '同一租户下，处于未被逻辑删除状态的资源组，名字要唯一'
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci COMMENT = '资源组表'
  ROW_FORMAT = Dynamic;

INSERT INTO mes3.base_factory_resource_group
(id, name, description, is_deleted, tenant_id, create_time, create_username, update_time, update_username)
VALUES (1, 'rg1', 'zilig的第1个测试', '1970-01-01 00:00:00', '58a0e82e-0ea2-4046-85c9-9e0e4b19ab21', '2021-09-22
09:55:34.630000000', 'anonymous', '2021-09-22 09:55:34.631000000', 'anonymous');
