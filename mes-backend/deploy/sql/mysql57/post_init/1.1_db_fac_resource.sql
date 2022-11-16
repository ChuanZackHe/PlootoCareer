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

 Date: 19/08/2021 11:23:24
*/

/**
  调试库为mes3，正式发布时修改
 */
use mes3;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for fac_resource_info
-- ----------------------------
DROP TABLE IF EXISTS `fac_resource_info`;
CREATE TABLE `fac_resource_info`
(
    `ID`              bigint                                                        NOT NULL AUTO_INCREMENT COMMENT '主键 long,AUTO_INCREMENT',
    `CODE`            varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '资源对应的CODE，要添加唯一索引；编码规则要不要考虑写在数据库里变成可配置的？',
    `TYPE`            int                                                           NOT NULL COMMENT '参考 FactoryResourceTypeEnum，需要持久化',
    `NAME`            varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '资源名称',
    `PARENT_CODE`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '表示从属关系。工厂填“EMPTY”，其他填父项CODE',
    `ANCESTOR_CODE`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '所属顶级实体代码',
    `STATE`           varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'ACTIVE' COMMENT '用户定义状态：启用；冻结；空闲；忙碌',
    `CAPABILITY`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL     DEFAULT NULL COMMENT '产能，预留',
    `DESC`            text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci         NULL COMMENT '描述String，预留',
    `TENANT_ID`       varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL COMMENT '租户id',
    `CREATE_TIME`     datetime(3)                                                   NULL     DEFAULT NULL COMMENT '创建时间',
    `CREATE_USERNAME` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL     DEFAULT NULL COMMENT '创建人',
    `UPDATE_TIME`     datetime(3)                                                   NULL     DEFAULT NULL COMMENT '最后更新时间',
    `UPDATE_USERNAME` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL     DEFAULT NULL COMMENT '最后更新人',
    `IS_DELETED`      datetime(3)                                                   NOT NULL DEFAULT '1970-01-01 00:00:00.000' COMMENT '逻辑删除：1970-01-01 00:00:00 表示启用，有值 表示删除',
    `Y_REL`           int                                                           NULL     DEFAULT NULL COMMENT '在工厂概况图中的y坐标',
    `X_REL`           int                                                           NULL     DEFAULT NULL COMMENT '在工厂概况图中的x坐标',
    PRIMARY KEY (`ID`) USING BTREE,
    UNIQUE INDEX `RESOURCE_CODE_IDX` (`CODE`, `IS_DELETED`, `TENANT_ID`) USING BTREE,
    INDEX `RESOURCE_PARENT_IDX` (`PARENT_CODE`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1222
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of fac_resource_info
-- ----------------------------

-- ----------------------------
-- Table structure for fac_resource_relation
-- ----------------------------
DROP TABLE IF EXISTS `fac_resource_relation`;
CREATE TABLE `fac_resource_relation`
(
    `ID`              bigint                                                        NOT NULL AUTO_INCREMENT COMMENT '主键 long, AUTO_INCREMENT',
    `PRE_CODE`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '后继资源代码',
    `AFTER_CODE`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '前驱资源代码',
    `TENANT_ID`       varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL COMMENT '租户id',
    `CREATE_TIME`     datetime(3)                                                   NULL     DEFAULT NULL COMMENT '创建时间',
    `CREATE_USERNAME` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL     DEFAULT NULL COMMENT '创建人',
    `UPDATE_TIME`     datetime(3)                                                   NULL     DEFAULT NULL COMMENT '最后更新时间',
    `UPDATE_USERNAME` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL     DEFAULT NULL COMMENT '最后更新人',
    `IS_DELETED`      datetime(3)                                                   NOT NULL DEFAULT '1970-01-01 00:00:00.000' COMMENT '逻辑删除：1970-01-01 00:00:00 表示启用，有值 表示删除',
    PRIMARY KEY (`ID`) USING BTREE,
    UNIQUE INDEX `RESOURCE_RELATION_IDX` (`PRE_CODE`, `AFTER_CODE`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 62
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '定义同级单位之间的先后顺序，用来显示资源之间的关系图'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of fac_resource_relation
-- ----------------------------

