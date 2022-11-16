/*
 Navicat Premium Data Transfer

 Source Server         : 桐庐MES_MySQL
 Source Server Type    : MySQL
 Source Server Version : 50735
 Source Host           : 8.130.8.30:35008
 Source Schema         : mes3

 Target Server Type    : MySQL
 Target Server Version : 50735
 File Encoding         : 65001

 Date: 11/08/2022 14:39:00
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for file_meta
-- ----------------------------
DROP TABLE IF EXISTS `file_meta`;
CREATE TABLE `file_meta`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `file_name` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '文件名称',
  `module` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '模块',
  `reference_id` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '索引的单据号或者是数据库ID',
  `create_time` datetime(3) NULL DEFAULT NULL COMMENT '创建时间',
  `create_username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建用户名',
  `update_time` datetime(3) NULL DEFAULT NULL COMMENT '更新时间',
  `update_username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '更新用户名',
  `tenant_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '租户id',
  `is_deleted` datetime(3) NOT NULL DEFAULT '1970-01-01 00:00:00.000' COMMENT '逻辑删除位，‘1970-01-01 00:00:00’为未删除，其余为删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `file_meta_idx`(`module`, `reference_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 27 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
