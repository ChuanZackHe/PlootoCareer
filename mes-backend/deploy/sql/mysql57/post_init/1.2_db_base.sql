/**
  调试库为mes3，正式发布时修改
 */
use mes3;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

/**
  基础信息模块
 */

-- ----------------------------
-- 基础物料表
-- ----------------------------
DROP TABLE IF EXISTS `base_material`;
CREATE TABLE `base_material`
(
    `id`              bigint       NOT NULL AUTO_INCREMENT COMMENT '物料id',
    `code`            varchar(255) NOT NULL COMMENT '物料编码',
    `name`            varchar(255) NOT NULL COMMENT '物料名称',
    `unit`            varchar(32)  NOT NULL COMMENT '基本单位, 从base_unit表获取',
    `description`     text COMMENT '物料描述',
    `extra`           json COMMENT 'json额外信息',
    `model`           varchar(255)          DEFAULT NULL COMMENT '型号',
    `type`            varchar(32)  NOT NULL COMMENT '物料类型:RAW 原材料，WIP 半成品，PRD 成品',
    `can_buy`         boolean      NOT NULL DEFAULT FALSE COMMENT '是否支持外购',

    `state`           varchar(255) NOT NULL DEFAULT 'ACTIVE' COMMENT '用户定义状态：0表示启用，1表示冻结',

    `is_deleted`      datetime(3)  NOT NULL DEFAULT '1970-01-01 00:00:00.000' COMMENT '逻辑删除：1970-01-01 00:00:00 表示启用，有值 表示删除',
    `tenant_id`       varchar(64)  NOT NULL COMMENT '租户id',
    `create_time`     datetime(3)  NULL     DEFAULT NULL COMMENT '创建时间',
    `create_username` varchar(64)  NULL     DEFAULT NULL COMMENT '创建人',
    `update_time`     datetime(3)  NULL     DEFAULT NULL COMMENT '最后更新时间',
    `update_username` varchar(64)  NULL     DEFAULT NULL COMMENT '最后更新人',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `U_code_tenant_deleted` (`code`, `tenant_id`, `is_deleted`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '基础物料表'
  ROW_FORMAT = Dynamic;
-- ----------------------------
-- Records of base_material
-- ----------------------------
INSERT INTO mes3.base_material
(id, code, name, unit, description, extra, model, `type`, can_buy, state, is_deleted, tenant_id, create_time,
 create_username, update_time, update_username)
VALUES (22, 'MAT-000', '原材料0', '个', '描述这个物品', NULL, '原材料0', 'RAW', 1, 'ACTIVE',
        '1970-01-01 00:00:00', '58a0e82e-0ea2-4046-85c9-9e0e4b19ab21', '2021-09-17 09:09:08.852000000', 'anonymous',
        '2021-09-17 09:09:08.854000000', 'anonymous');

INSERT INTO mes3.base_material
(id, code, name, unit, description, extra, model, `type`, can_buy, state, is_deleted, tenant_id, create_time,
 create_username, update_time, update_username)
VALUES (23, 'MAT-001', '原材料A', '个', '描述这个物品', NULL, '物品信号A', 'RAW', 1, 'ACTIVE',
        '1970-01-01 00:00:00', '58a0e82e-0ea2-4046-85c9-9e0e4b19ab21', '2021-09-17 09:09:08.852000000', 'anonymous',
        '2021-09-17 09:09:08.854000000', 'anonymous');

INSERT INTO mes3.base_material
(id, code, name, unit, description, extra, model, `type`, can_buy, state, is_deleted, tenant_id, create_time,
 create_username, update_time, update_username)
VALUES (24, 'WIP-002', '半成品2', '吨', '描述这个物品', NULL, '物品型号2', 'WIP', 1, 'ACTIVE',
        '1970-01-01 00:00:00', '58a0e82e-0ea2-4046-85c9-9e0e4b19ab21', '2021-09-17 09:09:08.852000000', 'anonymous',
        '2021-09-17 09:09:08.854000000', 'anonymous');

INSERT INTO mes3.base_material
(id, code, name, unit, description, extra, model, `type`, can_buy, state, is_deleted, tenant_id, create_time,
 create_username, update_time, update_username)
VALUES (25, 'WIP-003', '半成品3', '个', '描述这个物品', NULL, '物品信号A', 'WIP', 0, 'ACTIVE',
        '1970-01-01 00:00:00', '58a0e82e-0ea2-4046-85c9-9e0e4b19ab21', '2021-09-17 09:09:08.852000000', 'anonymous',
        '2021-09-17 09:09:08.854000000', 'anonymous');

INSERT INTO mes3.base_material
(id, code, name, unit, description, extra, model, `type`, can_buy, state, is_deleted, tenant_id, create_time,
 create_username, update_time, update_username)
VALUES (26, 'PRD-004', '成品4', '件', '描述这个物品', NULL, '成品4', 'PRD', 0, 'ACTIVE',
        '1970-01-01 00:00:00', '58a0e82e-0ea2-4046-85c9-9e0e4b19ab21', '2021-09-17 09:09:08.852000000', 'anonymous',
        '2021-09-17 09:09:08.854000000', 'anonymous');

INSERT INTO mes3.base_material
(id, code, name, unit, description, extra, model, `type`, can_buy, state, is_deleted, tenant_id, create_time,
 create_username, update_time, update_username)
VALUES (27, 'PRD-005', '成品5', '个', '描述这个物品', NULL, '成品5', 'PRD', 0, 'ACTIVE',
        '1970-01-01 00:00:00', '58a0e82e-0ea2-4046-85c9-9e0e4b19ab21', '2021-09-17 09:09:08.852000000', 'anonymous',
        '2021-09-17 09:09:08.854000000', 'anonymous');


-- ----------------------------
-- 供应商信息表 name 唯一
-- ----------------------------
DROP TABLE IF EXISTS `base_supplier`;
CREATE TABLE `base_supplier`
(
    `id`              bigint       NOT NULL AUTO_INCREMENT COMMENT '供应商id',
    `name`            varchar(255) NOT NULL COMMENT '供应商名称',
    `description`     text COMMENT '客户描述',
    `phone`           varchar(64) COMMENT '联系电话',
    `address`         varchar(255) COMMENT '地址',
    `info`            json COMMENT '其他信息',

    `is_deleted`      datetime(3)  NOT NULL DEFAULT '1970-01-01 00:00:00.000' COMMENT '逻辑删除：1970-01-01 00:00:00 表示启用，有值 表示删除',
    `tenant_id`       varchar(64)  NOT NULL COMMENT '租户id',
    `create_time`     datetime(3)  NULL     DEFAULT NULL COMMENT '创建时间',
    `create_username` varchar(64)  NULL     DEFAULT NULL COMMENT '创建人',
    `update_time`     datetime(3)  NULL     DEFAULT NULL COMMENT '最后更新时间',
    `update_username` varchar(64)  NULL     DEFAULT NULL COMMENT '最后更新人',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `IU_name_tenant_deleted` (`name`, `tenant_id`, `is_deleted`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '供应商表'
  ROW_FORMAT = DYNAMIC;


-- ----------------------------
-- 客户信息表 name 唯一
-- ----------------------------
DROP TABLE IF EXISTS `base_customer`;
CREATE TABLE `base_customer`
(
    `id`              bigint       NOT NULL AUTO_INCREMENT COMMENT '客户id',
    `name`            varchar(255) NOT NULL COMMENT '客户名称',
    `description`     text COMMENT '客户描述',
    `phone`           varchar(64) COMMENT '联系电话',
    `address`         varchar(255) COMMENT '地址',
    `info`            json COMMENT '其他信息',

    `is_deleted`      datetime(3)  NOT NULL DEFAULT '1970-01-01 00:00:00.000' COMMENT '逻辑删除：1970-01-01 00:00:00 表示启用，有值 表示删除',
    `tenant_id`       varchar(64)  NOT NULL COMMENT '租户id',
    `create_time`     datetime(3)  NULL     DEFAULT NULL COMMENT '创建时间',
    `create_username` varchar(64)  NULL     DEFAULT NULL COMMENT '创建人',
    `update_time`     datetime(3)  NULL     DEFAULT NULL COMMENT '最后更新时间',
    `update_username` varchar(64)  NULL     DEFAULT NULL COMMENT '最后更新人',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `IU_name_tenant_deleted` (`name`, `tenant_id`, `is_deleted`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '客户表'
  ROW_FORMAT = DYNAMIC;


-- ----------------------------
-- 工厂资源组表 name 唯一
-- ----------------------------
DROP TABLE IF EXISTS `base_factory_resource_group`;
CREATE TABLE `base_factory_resource_group`
(
    `id`              bigint       NOT NULL AUTO_INCREMENT COMMENT 'id',
    `name`            varchar(255) NOT NULL COMMENT '组名称',
    `description`     text COMMENT '组描述',
    `info`            json COMMENT '其他信息',

    `is_deleted`      datetime(3)  NOT NULL DEFAULT '1970-01-01 00:00:00.000' COMMENT '逻辑删除：1970-01-01 00:00:00 表示启用，有值 表示删除',
    `tenant_id`       varchar(64)  NOT NULL COMMENT '租户id',
    `create_time`     datetime(3)  NULL     DEFAULT NULL COMMENT '创建时间',
    `create_username` varchar(64)  NULL     DEFAULT NULL COMMENT '创建人',
    `update_time`     datetime(3)  NULL     DEFAULT NULL COMMENT '最后更新时间',
    `update_username` varchar(64)  NULL     DEFAULT NULL COMMENT '最后更新人',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `IU_name_tenant_deleted` (`name`, `tenant_id`, `is_deleted`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '工厂资源组表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- 加工工具组表 name 唯一
-- ----------------------------
DROP TABLE IF EXISTS `base_prd_tool_group`;
CREATE TABLE `base_prd_tool_group`
(
    `id`              bigint       NOT NULL AUTO_INCREMENT COMMENT 'id',
    `name`            varchar(255) NOT NULL COMMENT '组名称',
    `description`     text COMMENT '描述',
    `info`            json COMMENT '其他信息',

    `is_deleted`      datetime(3)  NOT NULL DEFAULT '1970-01-01 00:00:00.000' COMMENT '逻辑删除：1970-01-01 00:00:00 表示启用，有值 表示删除',
    `tenant_id`       varchar(64)  NOT NULL COMMENT '租户id',
    `create_time`     datetime(3)  NULL     DEFAULT NULL COMMENT '创建时间',
    `create_username` varchar(64)  NULL     DEFAULT NULL COMMENT '创建人',
    `update_time`     datetime(3)  NULL     DEFAULT NULL COMMENT '最后更新时间',
    `update_username` varchar(64)  NULL     DEFAULT NULL COMMENT '最后更新人',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `IU_name_tenant_deleted` (`name`, `tenant_id`, `is_deleted`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '加工工具组表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- 质检工具组表 name 唯一
-- ----------------------------
DROP TABLE IF EXISTS `base_qc_tool_group`;
CREATE TABLE `base_qc_tool_group`
(
    `id`              bigint       NOT NULL AUTO_INCREMENT COMMENT 'id',
    `name`            varchar(255) NOT NULL COMMENT '组名称',
    `description`     text COMMENT '描述',
    `info`            json COMMENT '其他信息',

    `is_deleted`      datetime(3)  NOT NULL DEFAULT '1970-01-01 00:00:00.000' COMMENT '逻辑删除：1970-01-01 00:00:00 表示启用，有值 表示删除',
    `tenant_id`       varchar(64)  NOT NULL COMMENT '租户id',
    `create_time`     datetime(3)  NULL     DEFAULT NULL COMMENT '创建时间',
    `create_username` varchar(64)  NULL     DEFAULT NULL COMMENT '创建人',
    `update_time`     datetime(3)  NULL     DEFAULT NULL COMMENT '最后更新时间',
    `update_username` varchar(64)  NULL     DEFAULT NULL COMMENT '最后更新人',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `IU_name_tenant_deleted` (`name`, `tenant_id`, `is_deleted`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '质检工具组表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- 单位(unit)表 unit 唯一
-- ----------------------------
DROP TABLE IF EXISTS `base_unit`;
CREATE TABLE `base_unit`
(
    `id`              bigint       NOT NULL AUTO_INCREMENT COMMENT 'id',
    `unit`            varchar(255) NOT NULL COMMENT '单位',
    `description`     text COMMENT '描述',

    `is_deleted`      datetime(3)  NOT NULL DEFAULT '1970-01-01 00:00:00.000' COMMENT '逻辑删除：1970-01-01 00:00:00 表示启用，有值 表示删除',
    `tenant_id`       varchar(64)  NOT NULL COMMENT '租户id',
    `create_time`     datetime(3)  NULL     DEFAULT NULL COMMENT '创建时间',
    `create_username` varchar(64)  NULL     DEFAULT NULL COMMENT '创建人',
    `update_time`     datetime(3)  NULL     DEFAULT NULL COMMENT '最后更新时间',
    `update_username` varchar(64)  NULL     DEFAULT NULL COMMENT '最后更新人',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `IU_unit_tenant_deleted` (`unit`, `tenant_id`, `is_deleted`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '单位(unit)表'
  ROW_FORMAT = DYNAMIC;


-- ----------------------------
-- (暂时放在基础信息模块) 质检管理-质检条目表，表名：base_qc_item 质检条目
-- name 唯一
-- ----------------------------
DROP TABLE IF EXISTS `base_qc_item`;
CREATE TABLE `base_qc_item`
(
    `id`              bigint       NOT NULL AUTO_INCREMENT COMMENT 'id',
    `name`            varchar(255) NOT NULL COMMENT '名称',
    `description`     text COMMENT '描述',
    `type`            varchar(255) NOT NULL COMMENT '类型',
    `extra`           json COMMENT '额外信息',
    `guide_file_name` text COMMENT '质检指导手册（图片或pdf等）获取路径（url或本地路径）',

    `is_deleted`      datetime(3)  NOT NULL DEFAULT '1970-01-01 00:00:00.000' COMMENT '逻辑删除：1970-01-01 00:00:00 表示启用，有值 表示删除',
    `tenant_id`       varchar(64)  NOT NULL COMMENT '租户id',
    `create_time`     datetime(3)  NULL     DEFAULT NULL COMMENT '创建时间',
    `create_username` varchar(64)  NULL     DEFAULT NULL COMMENT '创建人',
    `update_time`     datetime(3)  NULL     DEFAULT NULL COMMENT '最后更新时间',
    `update_username` varchar(64)  NULL     DEFAULT NULL COMMENT '最后更新人',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `UK_name_tenant_deleted` (`name`, `tenant_id`, `is_deleted`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '质检管理-质检条目表'
  ROW_FORMAT = DYNAMIC;


-- ----------------------------
-- (暂时放在基础信息模块) 质检管理-质检类型，表名：base_qc_item_type 质检类型
-- name 唯一； type唯一
-- ----------------------------
DROP TABLE IF EXISTS `base_qc_item_type`;
CREATE TABLE `base_qc_item_type`
(
    `id`              bigint       NOT NULL AUTO_INCREMENT COMMENT 'id',
    `name`            varchar(255) NOT NULL COMMENT '名称',
    `description`     text COMMENT '描述',
    `code`            varchar(255) NOT NULL COMMENT '略缩编码',

    `is_deleted`      datetime(3)  NOT NULL DEFAULT '1970-01-01 00:00:00.000' COMMENT '逻辑删除：1970-01-01 00:00:00 表示启用，有值 表示删除',
    `tenant_id`       varchar(64)  NOT NULL COMMENT '租户id',
    `create_time`     datetime(3)  NULL     DEFAULT NULL COMMENT '创建时间',
    `create_username` varchar(64)  NULL     DEFAULT NULL COMMENT '创建人',
    `update_time`     datetime(3)  NULL     DEFAULT NULL COMMENT '最后更新时间',
    `update_username` varchar(64)  NULL     DEFAULT NULL COMMENT '最后更新人',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `UK_name_tenant_deleted` (`name`, `tenant_id`, `is_deleted`) USING BTREE,
    UNIQUE KEY `UK_type_tenant_deleted` (`code`, `tenant_id`, `is_deleted`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '质检管理-质检类型'
  ROW_FORMAT = DYNAMIC;


-- ----------------------------
-- (暂时放在基础信息模块) 质检管理-质检缺陷表，表名：base_qc_fault 质检缺陷
-- name 唯一
-- ----------------------------
DROP TABLE IF EXISTS `base_qc_fault`;
CREATE TABLE `base_qc_fault`
(
    `id`              bigint       NOT NULL AUTO_INCREMENT COMMENT 'id',
    `name`            varchar(255) NOT NULL COMMENT '名称',
    `description`     text COMMENT '描述',
    `type`            varchar(255) NOT NULL COMMENT '类型，关联base_qc_fault_type中的code',
    `extra`           json COMMENT '额外信息',

    `is_deleted`      datetime(3)  NOT NULL DEFAULT '1970-01-01 00:00:00.000' COMMENT '逻辑删除：1970-01-01 00:00:00 表示启用，有值 表示删除',
    `tenant_id`       varchar(64)  NOT NULL COMMENT '租户id',
    `create_time`     datetime(3)  NULL     DEFAULT NULL COMMENT '创建时间',
    `create_username` varchar(64)  NULL     DEFAULT NULL COMMENT '创建人',
    `update_time`     datetime(3)  NULL     DEFAULT NULL COMMENT '最后更新时间',
    `update_username` varchar(64)  NULL     DEFAULT NULL COMMENT '最后更新人',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `UK_name_tenant_deleted` (`name`, `tenant_id`, `is_deleted`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '质检管理-质检缺陷表'
  ROW_FORMAT = DYNAMIC;


-- ----------------------------
-- (暂时放在基础信息模块) 质检管理-质检缺陷类型，表名：base_qc_fault_type 质检缺陷类型
-- name 唯一； type唯一
-- ----------------------------
DROP TABLE IF EXISTS `base_qc_fault_type`;
CREATE TABLE `base_qc_fault_type`
(
    `id`              bigint       NOT NULL AUTO_INCREMENT COMMENT 'id',
    `name`            varchar(255) NOT NULL COMMENT '名称',
    `description`     text COMMENT '描述',
    `code`            varchar(255) NOT NULL COMMENT '略缩编码',

    `is_deleted`      datetime(3)  NOT NULL DEFAULT '1970-01-01 00:00:00.000' COMMENT '逻辑删除：1970-01-01 00:00:00 表示启用，有值 表示删除',
    `tenant_id`       varchar(64)  NOT NULL COMMENT '租户id',
    `create_time`     datetime(3)  NULL     DEFAULT NULL COMMENT '创建时间',
    `create_username` varchar(64)  NULL     DEFAULT NULL COMMENT '创建人',
    `update_time`     datetime(3)  NULL     DEFAULT NULL COMMENT '最后更新时间',
    `update_username` varchar(64)  NULL     DEFAULT NULL COMMENT '最后更新人',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `UK_name_tenant_deleted` (`name`, `tenant_id`, `is_deleted`) USING BTREE,
    UNIQUE KEY `UK_type_tenant_deleted` (`code`, `tenant_id`, `is_deleted`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '质检管理-质检缺陷类型表'
  ROW_FORMAT = DYNAMIC;

DROP TABLE IF EXISTS mes3.expand_meta;
CREATE TABLE mes3.expand_meta
(
    `id`              bigint       NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `table_name`           varchar(255)   NOT NULL COMMENT '表名',
    `column_name`          varchar(255) NOT NULL COMMENT '其他信息',
    `type`            varchar(32)      NULL COMMENT '类型',
    `order_index`           int  NOT NULL COMMENT '顺序',
    `value_constraint`      json     NULL COMMENT '值约束',
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
    comment '拓展信息元数据表';

DROP TABLE IF EXISTS mes3.expand_data;
CREATE TABLE mes3.expand_data
(
    `id`              bigint       NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `table_name`           varchar(255)   NOT NULL COMMENT '表名',
    `column_name`          varchar(255) NOT NULL COMMENT '其他信息',
    `row_id`          bigint      NOT NULL COMMENT '记录的行ID',
    `type`            varchar(32)      NULL COMMENT '类型',
    `column_value`    text  NULL COMMENT '额外信息',
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
    comment '拓展信息数据表';

-- ----------------------------
-- 司机信息表
-- ----------------------------
DROP TABLE IF EXISTS mes3.base_driver;
CREATE TABLE mes3.base_driver
(
    `id`              bigint       NOT NULL AUTO_INCREMENT COMMENT '司机id',
    `name`            varchar(255) NOT NULL COMMENT '司机名称',
    `phone`           varchar(64)  NULL     DEFAULT NULL COMMENT '联系电话',
    `plate_number`    varchar(255) NULL     DEFAULT NULL COMMENT '车牌号',
    `description`     text COMMENT '备注',

    `is_deleted`      datetime(3)  NOT NULL DEFAULT '1970-01-01 00:00:00.000' COMMENT '逻辑删除：1970-01-01 00:00:00 表示启用，有值 表示删除',
    `tenant_id`       varchar(64)  NOT NULL COMMENT '租户id',
    `create_time`     datetime(3)  NULL     DEFAULT NULL COMMENT '创建时间',
    `create_username` varchar(64)  NULL     DEFAULT NULL COMMENT '创建人',
    `update_time`     datetime(3)  NULL     DEFAULT NULL COMMENT '最后更新时间',
    `update_username` varchar(64)  NULL     DEFAULT NULL COMMENT '最后更新人',
    PRIMARY KEY (`id`) USING BTREE
)   ENGINE = InnoDB
    CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_unicode_ci COMMENT = '司机表'
    ROW_FORMAT = DYNAMIC;

INSERT INTO mes3.base_qc_fault_type
(id, name, description, code, is_deleted, tenant_id, create_time, create_username, update_time, update_username)
VALUES (2, '测试用缺陷类型', NULL, 'TEST1', '1970-01-01 00:00:00', '58a0e82e-0ea2-4046-85c9-9e0e4b19ab21', '2021-09-22
09:55:34.630000000', 'anonymous', '2021-09-22 09:55:34.631000000', 'anonymous');

INSERT INTO mes3.base_qc_fault_type
(id, name, description, code, is_deleted, tenant_id, create_time, create_username, update_time, update_username)
VALUES (3, '测试用缺陷类型2', NULL, 'TEST2', '1970-01-01 00:00:00', '58a0e82e-0ea2-4046-85c9-9e0e4b19ab21', '2021-09-22
09:55:34.630000000', 'anonymous', '2021-09-22 09:55:34.631000000', 'anonymous');

INSERT INTO mes3.base_qc_fault_type
(id, name, description, code, is_deleted, tenant_id, create_time, create_username, update_time, update_username)
VALUES (4, '测试用缺陷类型3', NULL, 'TEST3', '1970-01-01 00:00:00', '58a0e82e-0ea2-4046-85c9-9e0e4b19ab21', '2021-09-22
09:55:34.630000000', 'anonymous', '2021-09-22 09:55:34.631000000', 'anonymous');
