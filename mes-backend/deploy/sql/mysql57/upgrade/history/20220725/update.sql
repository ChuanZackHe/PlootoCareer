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

alter table wms_stock_detail change column
    `batch_number` `batch_number`  varchar(64)  NULL COMMENT '批次号';

DROP TABLE IF EXISTS mes3.base_material_type;
CREATE TABLE mes3.base_material_type
(
    `id`              bigint       NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `name`      varchar(255) NOT NULL COMMENT '类型名称',
    `path`      varchar(255) NOT NULL COMMENT '从根节点到当前的路径',
    `level`      int NOT NULL COMMENT '层级',
    `parent_id`       bigint      NOT NULL default 0 COMMENT '父节点ID',
    `create_time`     datetime(3) NULL DEFAULT NULL COMMENT '创建时间',
    `create_username` varchar(255) NULL DEFAULT NULL COMMENT '创建用户名',
    `update_time`     datetime(3) NULL DEFAULT NULL COMMENT '更新时间',
    `update_username` varchar(255) NULL DEFAULT NULL COMMENT '更新用户名',
    `tenant_id`       varchar(64)  NOT NULL COMMENT '租户id',
    `is_deleted`      datetime(3) NOT NULL DEFAULT '1970-01-01 00:00:00.000' COMMENT '逻辑删除位，‘1970-01-01 00:00:00’为未删除，其余为删除',
    UNIQUE KEY `uk_index` (`name`, `parent_id`,`is_deleted`) USING BTREE,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
    AUTO_INCREMENT = 1
	CHARACTER SET = utf8mb4
	COLLATE = utf8mb4_unicode_ci COMMENT = '基础物料型号信息'
	ROW_FORMAT = Dynamic;

DROP TABLE IF EXISTS mes3.base_material_type_relation;
CREATE TABLE mes3.base_material_type_relation
(
    `id`              bigint       NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `type_id`              bigint       NOT NULL  COMMENT '类型ID',
    `material_id`     bigint    NOT NULL  COMMENT '物料ID',
    `create_time`     datetime(3) NULL DEFAULT NULL COMMENT '创建时间',
    `create_username` varchar(255) NULL DEFAULT NULL COMMENT '创建用户名',
    `update_time`     datetime(3) NULL DEFAULT NULL COMMENT '更新时间',
    `update_username` varchar(255) NULL DEFAULT NULL COMMENT '更新用户名',
    `tenant_id`       varchar(64)  NOT NULL COMMENT '租户id',
    `is_deleted`      datetime(3) NOT NULL DEFAULT '1970-01-01 00:00:00.000' COMMENT '逻辑删除位，‘1970-01-01 00:00:00’为未删除，其余为删除',
    UNIQUE KEY `uk_index` (`type_id`, `material_id`,`is_deleted`) USING BTREE,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
    AUTO_INCREMENT = 1
	CHARACTER SET = utf8mb4
	COLLATE = utf8mb4_unicode_ci COMMENT = '基础物料型号绑定关系'
	ROW_FORMAT = Dynamic;

-- 增加物料表中 自定义类型的绑定
alter table mes3.base_material add column
    `material_type_id`  json  NULL COMMENT '绑定的物料类型的ID';
alter table mes3.base_material add column
    `material_type_name`  json  NULL COMMENT '绑定的物料类型的名称';
alter table mes3.base_material add column
    `default_location`  json  NULL COMMENT '绑定的默认仓库';

-- 增加库存表 对 金属厂的适配
alter table wms_stock_detail add column
    `stock_in_date` datetime(3) NULL COMMENT '入库日期';
alter table wms_stock_detail add column
    `supplier` varchar(64) NULL COMMENT '供应商名称';
alter table wms_stock_detail add column
    `index_number` varchar(64) NULL COMMENT '编号';

-- 增加库存表 对 金属厂的适配
alter table wms_stock_in_detail add column
    `stock_in_date` datetime(3) NULL COMMENT '入库日期';
alter table wms_stock_in_detail add column
    `index_number` varchar(64) NULL COMMENT '编号';
alter table wms_stock_in_detail add column
    `stock_id` bigint NULL COMMENT '库存Id';

alter table wms_stock_out_detail add column
    `stock_id` bigint NULL COMMENT '绑定库存的ID';

DROP TABLE IF EXISTS mes3.ingredient_sheet_summary;
CREATE TABLE mes3.ingredient_sheet_summary
(
    `id`              bigint       NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `document_id`  varchar(64) NOT NULL COMMENT '主单ID',
    `user_name`  varchar(64) NOT NULL COMMENT '配料人',
    `description`  varchar(256)  NULL COMMENT '描述',
    `ingredient_date`      date NOT NULL COMMENT '配料时间',
    `create_time`     datetime(3) NULL DEFAULT NULL COMMENT '创建时间',
    `create_username` varchar(255) NULL DEFAULT NULL COMMENT '创建用户名',
    `update_time`     datetime(3) NULL DEFAULT NULL COMMENT '更新时间',
    `update_username` varchar(255) NULL DEFAULT NULL COMMENT '更新用户名',
    `tenant_id`       varchar(64)  NOT NULL COMMENT '租户id',
    `is_deleted`      datetime(3) NOT NULL DEFAULT '1970-01-01 00:00:00.000' COMMENT '逻辑删除位，‘1970-01-01 00:00:00’为未删除，其余为删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
    AUTO_INCREMENT = 1
	CHARACTER SET = utf8mb4
	COLLATE = utf8mb4_unicode_ci COMMENT = '配料单主单'
	ROW_FORMAT = Dynamic;


DROP TABLE IF EXISTS mes3.ingredient_sheet_detail;
CREATE TABLE mes3.ingredient_sheet_detail
(
    `id`              bigint       NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `document_id`  varchar(64) NOT NULL COMMENT '主单ID',
    `index_number`  varchar(32)  NULL COMMENT '炉号',
    `ingredient_result`      json  NULL COMMENT '配料成品结果',
    `ingredient_details`      json  NULL COMMENT '配料详情',
    `create_time`     datetime(3) NULL DEFAULT NULL COMMENT '创建时间',
    `create_username` varchar(255) NULL DEFAULT NULL COMMENT '创建用户名',
    `update_time`     datetime(3) NULL DEFAULT NULL COMMENT '更新时间',
    `update_username` varchar(255) NULL DEFAULT NULL COMMENT '更新用户名',
    `tenant_id`       varchar(64)  NOT NULL COMMENT '租户id',
    `is_deleted`      datetime(3) NOT NULL DEFAULT '1970-01-01 00:00:00.000' COMMENT '逻辑删除位，‘1970-01-01 00:00:00’为未删除，其余为删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
    AUTO_INCREMENT = 1
	CHARACTER SET = utf8mb4
	COLLATE = utf8mb4_unicode_ci COMMENT = '配料单子单'
	ROW_FORMAT = Dynamic;

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

DROP TABLE IF EXISTS mes3.procurement_receive_summary;
CREATE TABLE mes3.procurement_receive_summary
(
    `id`              bigint       NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `procurement_id`  varchar(255) NOT NULL COMMENT '采购单编号',
    `driver_id`       bigint       NOT NULL COMMENT '司机id',
    `driver_name`     varchar(255) NOT NULL COMMENT '司机名称',
    `driver_phone`    varchar(64)  NULL     DEFAULT NULL COMMENT '司机联系电话',
    `plate_number`    varchar(255) NULL     DEFAULT NULL COMMENT '车牌号',
    `receiver`        varchar(255) NOT NULL COMMENT '验收人',
    `supplier`        varchar(255) NOT NULL COMMENT '供应商名称',
    `receive_time`    datetime(3)  NULL     DEFAULT NULL COMMENT '到货时间',
    `settlement`      varchar(16)  NULL     DEFAULT NULL COMMENT '结算状态',
    `settlement_time` datetime(3)  NULL     DEFAULT NULL COMMENT '结算时间',
    `amount`          double       NULL     DEFAULT NULL COMMENT '结算金额',
    `description`     text                  COMMENT '备注',

    `is_deleted`      datetime(3)  NOT NULL DEFAULT '1970-01-01 00:00:00.000' COMMENT '逻辑删除：1970-01-01 00:00:00 表示启用，有值 表示删除',
    `tenant_id`       varchar(64)  NOT NULL COMMENT '租户id',
    `create_time`     datetime(3)  NULL     DEFAULT NULL COMMENT '创建时间',
    `create_username` varchar(64)  NULL     DEFAULT NULL COMMENT '创建人',
    `update_time`     datetime(3)  NULL     DEFAULT NULL COMMENT '最后更新时间',
    `update_username` varchar(64)  NULL     DEFAULT NULL COMMENT '最后更新人',
    PRIMARY KEY (`id`) USING BTREE
)   ENGINE = InnoDB
    CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_unicode_ci COMMENT = '到货记录主表'
    ROW_FORMAT = DYNAMIC;

DROP TABLE IF EXISTS mes3.procurement_receive_detail;
CREATE TABLE mes3.procurement_receive_detail
(
    `id`              bigint       NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `document_id`     bigint       NOT NULL COMMENT '到货记录主表编号',
    `material_id`     bigint       NOT NULL COMMENT '物料id',
    `material_name`   varchar(255) NOT NULL COMMENT '物料名称',
    `material_unit`   varchar(16)  NOT NULL COMMENT '物料单位',
    `supplier`        varchar(255) NOT NULL COMMENT '供应商名称',
    `receive_count`   double       NULL     DEFAULT NULL COMMENT '到货数量',
    `receive_time`    datetime(3)  NULL     DEFAULT NULL COMMENT '到货时间',
    `index_number`    varchar(255) NULL     DEFAULT NULL COMMENT '编号',
    `description`     text                  COMMENT '备注',

    `is_deleted`      datetime(3)  NOT NULL DEFAULT '1970-01-01 00:00:00.000' COMMENT '逻辑删除：1970-01-01 00:00:00 表示启用，有值 表示删除',
    `tenant_id`       varchar(64)  NOT NULL COMMENT '租户id',
    `create_time`     datetime(3)  NULL     DEFAULT NULL COMMENT '创建时间',
    `create_username` varchar(64)  NULL     DEFAULT NULL COMMENT '创建人',
    `update_time`     datetime(3)  NULL     DEFAULT NULL COMMENT '最后更新时间',
    `update_username` varchar(64)  NULL     DEFAULT NULL COMMENT '最后更新人',
    PRIMARY KEY (`id`) USING BTREE
)   ENGINE = InnoDB
    CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_unicode_ci COMMENT = '到货记录子表'
    ROW_FORMAT = DYNAMIC;

-- # 采购信息主表和子表修改
ALTER TABLE procurement_summary DROP COLUMN delivery_detail;
ALTER TABLE procurement_detail DROP COLUMN receive_detail, DROP COLUMN receive_count;