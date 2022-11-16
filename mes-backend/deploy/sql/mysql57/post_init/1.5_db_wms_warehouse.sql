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

 Date: 24/09/2021 17:44:59
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

use mes3;
-- ----------------------------
-- Table structure for wms_warehouse_info
-- ----------------------------
DROP TABLE IF EXISTS `wms_warhouse_info`;
DROP TABLE IF EXISTS `wms_warehouse_info`;
CREATE TABLE `wms_warehouse_info`
(
    `id`              bigint       NOT NULL AUTO_INCREMENT COMMENT '主键',
    `code`            varchar(32)  NOT NULL COMMENT '库位代码',
    `type`            varchar(32)  NOT NULL COMMENT '类型',
    `warehouse_type`  varchar(32)  NULL COMMENT '库位类型',
    `name`            varchar(255) NOT NULL COMMENT '名称',
    `parent_id`       bigint       NOT NULL DEFAULT 0 COMMENT '父节点id（根节点的父节点id=0）',
    `ancestor_id`     bigint       NOT NULL DEFAULT 0 COMMENT '根节点id（根节点的根节点id=0）',
    `description`     text         NULL COMMENT '描述',
    `location`        varchar(255) NULL COMMENT '库位位置',
    `param`           json         NULL COMMENT '资源参数描述（如长宽高等）',
    `x_rel`           int          NULL     DEFAULT NULL COMMENT '在界面上的相对坐标x',
    `y_rel`           int          NULL     DEFAULT NULL COMMENT '在界面上的相对坐标y',
    `state`           varchar(32)  NOT NULL DEFAULT 'ACTIVE' COMMENT '用户定义状态',
    `is_deleted`      datetime(3)  NOT NULL DEFAULT '1970-01-01 00:00:00.000' COMMENT '逻辑删除。未删除为1970-01-01 00:00:00',
    `tenant_id`       varchar(64)  NOT NULL COMMENT '租户id',
    `create_username` varchar(64)  NULL     DEFAULT NULL COMMENT '创建用户',
    `create_time`     datetime(3)  NULL     DEFAULT NULL COMMENT '创建时间',
    `update_username` varchar(64)  NULL     DEFAULT NULL COMMENT '更新用户',
    `update_time`     datetime(3)  NULL     DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `storage_code_idx_uq` (`code`, `is_deleted`, `tenant_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '仓储资源表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wms_warehouse_info
-- ----------------------------

DROP TABLE IF EXISTS `wms_stock_summary`;
CREATE TABLE `wms_stock_summary`
(
    `id`              bigint       NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `material_id`     bigint       NOT NULL COMMENT '物料id',
    `material_unit`   varchar(16)  NOT NULL COMMENT '物料基本单位',
    `material_name`   varchar(255) NOT NULL COMMENT '物料名称',
    `count`           double       NOT NULL COMMENT '库存数量',
    `create_time`     datetime(3)  NULL     DEFAULT NULL COMMENT '创建时间',
    `create_username` varchar(255) NULL     DEFAULT NULL COMMENT '创建用户名',
    `update_time`     datetime(3)  NULL     DEFAULT NULL COMMENT '更新时间',
    `update_username` varchar(255) NULL     DEFAULT NULL COMMENT '更新用户名',
    `tenant_id`       varchar(64)  NOT NULL COMMENT '租户id',
    `is_deleted`      datetime(3)  NOT NULL DEFAULT '1970-01-01 00:00:00.000' COMMENT '逻辑删除位，‘1970-01-01 00:00:00’为未删除，其余为删除',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_index` (`material_id`) USING BTREE,
    INDEX `NAME_INDEX` (`material_name`, `tenant_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '记录当前在库的实时库存概览'
  ROW_FORMAT = Dynamic;

DROP TABLE IF EXISTS `wms_stock_detail`;
CREATE TABLE `wms_stock_detail`
(
    `id`              bigint       NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `warehouse_code`  varchar(32) NOT NULL COMMENT '仓库code',
    `carrier_code`    varchar(32) NOT NULL COMMENT '货架code',
    `location_code`   varchar(32) NOT NULL COMMENT '库位code',
    `material_id`     bigint       NOT NULL COMMENT '物料id',
    `material_unit`   varchar(16)  NOT NULL COMMENT '物料基本单位',
    `material_name`   varchar(255) NOT NULL COMMENT '物料名称',
    `material_price`  double       NULL     DEFAULT NULL COMMENT '物料单位价格',
    `count`           double       NOT NULL COMMENT '库存数量',
    `batch_number`    varchar(64)  NULL     COMMENT '批次号',
    `create_time`     datetime(3)  NULL     DEFAULT NULL COMMENT '创建时间',
    `create_username` varchar(255) NULL     DEFAULT NULL COMMENT '创建用户名',
    `update_time`     datetime(3)  NULL     DEFAULT NULL COMMENT '更新时间',
    `update_username` varchar(255) NULL     DEFAULT NULL COMMENT '更新用户名',
    `tenant_id`       varchar(64)  NOT NULL COMMENT '租户id',
    `is_deleted`      datetime(3)  NOT NULL DEFAULT '1970-01-01 00:00:00.000' COMMENT '逻辑删除位，‘1970-01-01 00:00:00’为未删除，其余为删除',
    `description`   varchar(255) NULL DEFAULT NULL COMMENT '描述',
    `stock_in_date` datetime(3) NULL COMMENT '入库日期',
    `supplier` varchar(64) NULL COMMENT '供应商名称',
    `index_number` varchar(64) NULL COMMENT '编号',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_index` (`material_id`, `location_code`, `batch_number`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '记录当前在库的实时库存细则'
  ROW_FORMAT = Dynamic;


DROP TABLE IF EXISTS `wms_stock_operation_history`;
CREATE TABLE `wms_stock_operation_history`
(
    `id`              bigint       NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `warehouse_code`  varchar(255) NOT NULL COMMENT '仓库code',
    `carrier_code`    varchar(255) NOT NULL COMMENT '货架code',
    `location_code`   varchar(255) NOT NULL COMMENT '库位code',
    `material_id`     bigint       NOT NULL COMMENT '物料id',
    `material_name`   varchar(255) NOT NULL COMMENT '物料名称',
    `count`           double       NOT NULL COMMENT '操作数量',
    `document_id`     varchar(255) NULL     DEFAULT NULL COMMENT '单据号',
    `type`            varchar(32)  NOT NULL COMMENT '操作类型',
    `create_time`     datetime(3)  NULL     DEFAULT NULL COMMENT '创建时间',
    `create_username` varchar(255) NULL     DEFAULT NULL COMMENT '创建用户名',
    `update_time`     datetime(3)  NULL     DEFAULT NULL COMMENT '更新时间',
    `update_username` varchar(255) NULL     DEFAULT NULL COMMENT '更新用户名',
    `tenant_id`       varchar(64)  NOT NULL COMMENT '租户id',
    `is_deleted`      datetime(3)  NOT NULL DEFAULT '1970-01-01 00:00:00.000' COMMENT '逻辑删除位，‘1970-01-01 00:00:00’为未删除，其余为删除',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `NAME_INDEX` (`material_name`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '库存操作历史表'
  ROW_FORMAT = Dynamic;


DROP TABLE IF EXISTS `wms_stock_in_summary`;
CREATE TABLE `wms_stock_in_summary`
(
    `id`              bigint       NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `document_id`     varchar(255) NOT NULL COMMENT '单据id',
    `order_id`        varchar(255) NOT NULL COMMENT '订单或者外购单id',
    `supplier`        varchar(255) NULL     DEFAULT NULL COMMENT '供应商',
    `status`          varchar(32)  NULL     DEFAULT NULL COMMENT '入库总单状态（和子单状态联动）',
    `type`            varchar(32)  NOT NULL COMMENT '入库总单类型（生产入库、外购入库等）',
    `description`     text         NULL COMMENT '描述',
    `create_time`     datetime(3)  NULL     DEFAULT NULL COMMENT '创建时间',
    `create_username` varchar(255) NULL     DEFAULT NULL COMMENT '创建用户名',
    `update_time`     datetime(3)  NULL     DEFAULT NULL COMMENT '更新时间',
    `update_username` varchar(255) NULL     DEFAULT NULL COMMENT '更新用户名',
    `tenant_id`       varchar(64)  NOT NULL COMMENT '租户id',
    `is_deleted`      datetime(3)  NOT NULL DEFAULT '1970-01-01 00:00:00.000' COMMENT '逻辑删除位，‘1970-01-01 00:00:00’为未删除，其余为删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '入库总单'
  ROW_FORMAT
      = Dynamic;

DROP TABLE IF EXISTS `wms_stock_in_detail`;
CREATE TABLE `wms_stock_in_detail`
(
    `id`              bigint       NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `document_id`     varchar(255) NOT NULL COMMENT '入库总单编号',
    `supplier`        varchar(255) NULL     DEFAULT NULL COMMENT '供应商',
    `material_id`     bigint       NOT NULL COMMENT '物料id',
    `material_unit`   varchar(16)  NOT NULL COMMENT '物料基本单位',
    `material_name`   varchar(255) NOT NULL COMMENT '物料名称',
    `material_price`  double       NULL     DEFAULT NULL COMMENT '物料单位价格',
    `count`           double       NOT NULL COMMENT '库存数量',
    `real_count`      double       NULL     DEFAULT NULL COMMENT '实际入库存数量',
    `batch_number`    varchar(64)  NULL     DEFAULT NULL COMMENT '批次号',
    `status`          varchar(32)  NULL     DEFAULT NULL COMMENT '子单状态',
    `warehouse_code`  varchar(255) NULL     DEFAULT NULL COMMENT '仓库code',
    `carrier_code`    varchar(255) NULL     DEFAULT NULL COMMENT '货架code',
    `location_code`   varchar(255) NULL     DEFAULT NULL COMMENT '库位code',
    `description`     varchar(255) NULL     DEFAULT NULL COMMENT '异常的详细描述',
    `info`            text         NULL COMMENT '描述',
    `create_time`     datetime(3)  NULL     DEFAULT NULL COMMENT '创建时间',
    `create_username` varchar(255) NULL     DEFAULT NULL COMMENT '创建用户名',
    `update_time`     datetime(3)  NULL     DEFAULT NULL COMMENT '更新时间',
    `update_username` varchar(255) NULL     DEFAULT NULL COMMENT '更新用户名',
    `tenant_id`       varchar(64)  NOT NULL COMMENT '租户id',
    `is_deleted`      datetime(3)  NOT NULL DEFAULT '1970-01-01 00:00:00.000' COMMENT '逻辑删除位，‘1970-01-01 00:00:00’为未删除，其余为删除',
    `stock_in_date` datetime(3) NULL COMMENT '入库日期',
    `index_number` varchar(64) NULL COMMENT '编号',
    `stock_id` bigint NULL COMMENT '库存Id',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '入库子单'
  ROW_FORMAT
      = Dynamic;

DROP TABLE IF EXISTS `wms_stock_out_summary`;
CREATE TABLE `wms_stock_out_summary`
(
    `id`              bigint       NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `document_id`     varchar(255) NOT NULL COMMENT '单据id',
    `delivery_id`     varchar(255) NULL     DEFAULT NULL COMMENT '关联的交付单编号/生产单编号',
    `customer`        varchar(255) NULL COMMENT '订货人',
    `status`          varchar(32)  NULL     DEFAULT NULL COMMENT '出库总单状态（和子单状态联动）',
    `type`            varchar(32)  NOT NULL COMMENT '出库总单类型（领料出库、交付出库）',
    `description`     text         NULL COMMENT '描述',
    `create_time`     datetime(3)  NULL     DEFAULT NULL COMMENT '创建时间',
    `create_username` varchar(255) NULL     DEFAULT NULL COMMENT '创建用户名',
    `update_time`     datetime(3)  NULL     DEFAULT NULL COMMENT '更新时间',
    `update_username` varchar(255) NULL     DEFAULT NULL COMMENT '更新用户名',
    `tenant_id`       varchar(64)  NOT NULL COMMENT '租户id',
    `is_deleted`      datetime(3)  NOT NULL DEFAULT '1970-01-01 00:00:00.000' COMMENT '逻辑删除位，‘1970-01-01 00:00:00’为未删除，其余为删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '出库总单'
  ROW_FORMAT = Dynamic;

DROP TABLE IF EXISTS `wms_stock_out_detail`;
CREATE TABLE `wms_stock_out_detail`
(
    `id`              bigint       NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `document_id`     varchar(255) NOT NULL COMMENT '库位id',
    `customer`        varchar(255) NULL COMMENT '订货人',
    `material_id`     bigint       NOT NULL COMMENT '物料id',
    `material_unit`   varchar(16)  NOT NULL COMMENT '物料基本单位',
    `material_name`   varchar(255) NOT NULL COMMENT '物料名称',
    `material_price`  double       NULL     DEFAULT NULL COMMENT '物料单位价格',
    `count`           double       NOT NULL COMMENT '计划出库存数量',
    `real_count`      double       NULL     DEFAULT NULL COMMENT '实际出库存数量',
    `batch_number`    varchar(64)  NULL     DEFAULT NULL COMMENT '批次号',
    `status`          varchar(32)  NULL     DEFAULT NULL COMMENT '子单状态',
    `description`     varchar(255) NULL     DEFAULT NULL COMMENT '关单的详细描述',
    `info`            text         NULL COMMENT '描述',
    `warehouse_code`  varchar(255) NULL     DEFAULT NULL COMMENT '仓库code',
    `carrier_code`    varchar(255) NULL     DEFAULT NULL COMMENT '货架code',
    `location_code`   varchar(255) NULL     DEFAULT NULL COMMENT '库位code',
    `create_time`     datetime(3)  NULL     DEFAULT NULL COMMENT '创建时间',
    `create_username` varchar(255) NULL     DEFAULT NULL COMMENT '创建用户名',
    `update_time`     datetime(3)  NULL     DEFAULT NULL COMMENT '更新时间',
    `update_username` varchar(255) NULL     DEFAULT NULL COMMENT '更新用户名',
    `tenant_id`       varchar(64)  NOT NULL COMMENT '租户id',
    `is_deleted`      datetime(3)  NOT NULL DEFAULT '1970-01-01 00:00:00.000' COMMENT '逻辑删除位，‘1970-01-01 00:00:00’为未删除，其余为删除',
    `stock_id` bigint NULL COMMENT '绑定库存的ID',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '出库子单'
  ROW_FORMAT
      = Dynamic;

DROP TABLE IF EXISTS `wms_stock_inventory_detail`;
CREATE TABLE `wms_stock_inventory_detail` (
                                              `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
                                              `document_id` varchar(255)  NOT NULL COMMENT '库位id',
                                              `material_id` bigint(20) NOT NULL COMMENT '物料id',
                                              `material_name` varchar(255)  NOT NULL COMMENT '物料名称',
                                              `stock_id` bigint(20) NOT NULL COMMENT '库存记录ID',
                                              `batch_number` varchar(64)  NULL COMMENT '批次号',
                                              `location_name` varchar(32)  NOT NULL COMMENT '库位名称',
                                              `location_code` varchar(32)  NOT NULL COMMENT '库位code',
                                              `last_count` double NULL COMMENT '盘点之前的库存数量',
                                              `count` double NULL COMMENT '计划盘点数量',
                                              `description` varchar(255)  DEFAULT NULL COMMENT '详细描述',
                                              `status` varchar(32)  DEFAULT NULL COMMENT '子单状态',
                                              `create_time` datetime(3) DEFAULT NULL COMMENT '创建时间',
                                              `create_username` varchar(255)  DEFAULT NULL COMMENT '创建用户名',
                                              `update_time` datetime(3) DEFAULT NULL COMMENT '更新时间',
                                              `update_username` varchar(255)  DEFAULT NULL COMMENT '更新用户名',
                                              `tenant_id` varchar(64)  NOT NULL COMMENT '租户id',
                                              `is_deleted` datetime(3) NOT NULL DEFAULT '1970-01-01 00:00:00.000' COMMENT '逻辑删除位，‘1970-01-01 00:00:00’为未删除，其余为删除',
                                              PRIMARY KEY (`id`) USING BTREE,
                                              UNIQUE KEY `inventory_idx_uq` (`document_id`,`stock_id`,`is_deleted`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='调拨单子单';


DROP TABLE IF EXISTS `wms_stock_inventory_summary`;
CREATE TABLE `wms_stock_inventory_summary` (
                                               `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
                                               `document_id` varchar(255)  NOT NULL COMMENT '库位id',
                                               `warehouse_code` varchar(32)  NOT NULL COMMENT '源货位',
                                               `warehouse_name` varchar(32)  NOT NULL COMMENT '库位名称',
                                               `carrier_code` varchar(32)  NOT NULL COMMENT '货架code',
                                               `carrier_name` varchar(32)  NOT NULL COMMENT '货架名称',
                                               `description` varchar(255)  DEFAULT NULL COMMENT '详细描述',
                                               `status` varchar(32)  DEFAULT NULL COMMENT '状态',
                                               `create_time` datetime(3) DEFAULT NULL COMMENT '创建时间',
                                               `create_username` varchar(255)  DEFAULT NULL COMMENT '创建用户名',
                                               `update_time` datetime(3) DEFAULT NULL COMMENT '更新时间',
                                               `update_username` varchar(255)  DEFAULT NULL COMMENT '更新用户名',
                                               `tenant_id` varchar(64)  NOT NULL COMMENT '租户id',
                                               `is_deleted` datetime(3) NOT NULL DEFAULT '1970-01-01 00:00:00.000' COMMENT '逻辑删除位，‘1970-01-01 00:00:00’为未删除，其余为删除',
                                               PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='盘点单主单';
