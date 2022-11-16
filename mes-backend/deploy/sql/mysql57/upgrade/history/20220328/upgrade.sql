use mes3;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

alter table wms_stock_detail
    add column `lock_status` varchar(32) default "UNLOCKED" COMMENT '状态';

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

DROP TABLE IF EXISTS mes3.expand_meta;
CREATE TABLE mes3.expand_meta
(
    `id`              bigint       NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `table_name`           varchar(255)   NOT NULL COMMENT '表名',
    `column_name`          varchar(255) NOT NULL COMMENT '其他信息',
    `type`            varchar(32)      NULL COMMENT '类型',
    `order_index`           int  NOT NULL COMMENT '顺序',
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
-- Table structure for sys_perm
-- ----------------------------
DROP TABLE IF EXISTS `internal_tenant_config`;
CREATE TABLE `internal_tenant_config`
(
    `id`              bigint      NOT NULL AUTO_INCREMENT COMMENT '记录ID',
    `is_deleted`      datetime(3) NOT NULL DEFAULT '1970-01-01 00:00:00.000' COMMENT '逻辑删除：1970-01-01 00:00:00 表示启用，有值 表示删除',
    `tenant_id`       varchar(64) NULL     DEFAULT NULL COMMENT '租户id',
    `tenant_name`     varchar(64) NULL     DEFAULT NULL COMMENT '租户名称',
    `visible_modules` json        NULL COMMENT '可见菜单列表',
    `visible_perms`   json        NULL COMMENT '可见权限列表',
    `configs`         json        NULL COMMENT '租户配置',
    `quotas`          json        NULL COMMENT '租户限额',
    `create_time`     datetime(3) NULL     DEFAULT NULL COMMENT '创建时间',
    `create_username` varchar(64) NULL     DEFAULT NULL COMMENT '创建人',
    `update_time`     datetime(3) NULL     DEFAULT NULL COMMENT '最后更新时间',
    `update_username` varchar(64) NULL     DEFAULT NULL COMMENT '最后更新人',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 5
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci COMMENT = '内部管理-租户配置表'
  ROW_FORMAT = DYNAMIC;

INSERT
INTO
    mes3.internal_tenant_config (id,
                                 is_deleted,
                                 tenant_id,
                                 tenant_name,
                                 visible_modules,
                                 visible_perms,
                                 configs,
                                 quotas,
                                 create_time,
                                 create_username,
                                 update_time,
                                 update_username)
VALUES(1,
       '1970-01-01 00:00:00',
       '58a0e82e-0ea2-4046-85c9-9e0e4b19ab21',
       '默认内置配置',
       '[{"title":"工厂实况","code":"FACTORY_LIVE"},{"title":"订单管理","code":"ORDER","subs":[{"title":"订单总览","code":"ORDER_OVERVIEW"},{"title":"订单维护","code":"ORDER_MAINTAIN"},{"title":"交付单维护","code":"DELIVERY_MAINTAIN"}]},{"title":"生产单管理","code":"PRODUCE","subs":[{"title":"生产单列表","code":"JOB_MAINTAIN"},{"title":"任务列表","code":"TASK_MAINTAIN"}]},{"title":"工艺管理","code":"CRAFT","subs":[{"title":"工艺步骤管理","code":"CRAFT_STEP"},{"title":"工艺流程管理","code":"CRAFT_FLOW"}]},{"title":"仓储管理","code":"WAREHOUSING","subs":[{"title":"仓库管理","code":"WAREHOUSE_MAINTAIN"},{"title":"库存管理","code":"STOCK_MAINTAIN"},{"title":"库房盘点","code":"INVENTORY_MAINTAIN"},{"title":"入库管理","code":"STOCKIN_MAINTAIN"},{"index":"outStorageManage","title":"出库管理","code":"STOCKOUT_MAINTAIN"}]},{"title":"基础信息","code":"BASE","subs":[{"title":"物料库","code":"MATERIAL"},{"title":"基本物料单位","code":"UNIT"},{"title":"工具组","code":"TOOL_GROUP"},{"title":"外部机构","code":"EXTERNAL_AGENCY"},{"title":"质检信息","code":"QC"},{"title":"工厂资源组","code":"RESOURCE_GROUP"},{"title":"基本类型","code":"BASE_TYPE"}]},{"title":"系统管理","code":"SYSTEM","subs":[{"title":"用户管理","code":"TENANT"},{"title":"角色管理","code":"ROLE"}]}]',
       '[1,2,3,4,6,7,8,9,10,11,13,15,16,18,20,21,23,25,26,28,30,31,33,35,36,38,40,41,42,43,44,46,48,49,51,53,54,56,58,72,75,76,77,88,90,92,93,95,97,108,109,111,112,113,114,115,117,118,119,124,125,127,128,129,130,132,133,134,139,141,153,165,166,172,188,207,208,209,210,211,212,213,214,215,216,217,218,219,220,221,222,223,224,225,226,227,228,229,230,231,232,233,234,235,236,237,238,239,240,241,242,243,244]',
       '[{"value": "true", "config": "配置项1"}, {"value": "100", "config": "配置项2"}]',
       '[{"quota": "资源限制1", "value": "100"}, {"quota": "资源限制2", "value": "200"}]',
       '2022-03-22 07:27:50.850000000',
       'system',
       '2022-03-22 07:27:50.852000000',
       'system');

INSERT
INTO
    mes3.internal_tenant_config (id,
                                 is_deleted,
                                 tenant_id,
                                 tenant_name,
                                 visible_modules,
                                 visible_perms,
                                 configs,
                                 quotas,
                                 create_time,
                                 create_username,
                                 update_time,
                                 update_username)
VALUES(10,
       '1970-01-01 00:00:00',
       '87ba0ecc-cd17-4643-a37d-a561f8eb891c',
       '桐庐宇鑫',
       '[{"title":"工厂实况","code":"FACTORY_LIVE"},{"title":"订单管理","code":"ORDER","subs":[{"title":"订单总览","code":"ORDER_OVERVIEW"},{"title":"订单维护","code":"ORDER_MAINTAIN"},{"title":"交付单维护","code":"DELIVERY_MAINTAIN"}]},{"title":"生产单管理","code":"PRODUCE","subs":[{"title":"生产单列表","code":"JOB_MAINTAIN"},{"title":"任务列表","code":"TASK_MAINTAIN"}]},{"title":"工艺管理","code":"CRAFT","subs":[{"title":"工艺步骤管理","code":"CRAFT_STEP"},{"title":"工艺流程管理","code":"CRAFT_FLOW"}]},{"title":"仓储管理","code":"WAREHOUSING","subs":[{"title":"仓库管理","code":"WAREHOUSE_MAINTAIN"},{"title":"库存管理","code":"STOCK_MAINTAIN"},{"title":"库房盘点","code":"INVENTORY_MAINTAIN"},{"title":"入库管理","code":"STOCKIN_MAINTAIN"},{"index":"outStorageManage","title":"出库管理","code":"STOCKOUT_MAINTAIN"}]},{"title":"基础信息","code":"BASE","subs":[{"title":"物料库","code":"MATERIAL"},{"title":"基本物料单位","code":"UNIT"},{"title":"工具组","code":"TOOL_GROUP"},{"title":"外部机构","code":"EXTERNAL_AGENCY"},{"title":"质检信息","code":"QC"},{"title":"工厂资源组","code":"RESOURCE_GROUP"},{"title":"基本类型","code":"BASE_TYPE"}]},{"title":"系统管理","code":"SYSTEM","subs":[{"title":"用户管理","code":"TENANT"},{"title":"角色管理","code":"ROLE"}]}]',
       '[1,2,3,4,6,7,8,9,10,11,13,15,16,18,20,21,23,25,26,28,30,31,33,35,36,38,40,41,42,43,44,46,48,49,51,53,54,56,58,72,75,76,77,88,90,92,93,95,97,108,109,111,112,113,114,115,117,118,119,124,125,127,128,129,130,132,133,134,139,141,153,165,166,172,188,207,208,209,210,211,212,213,214,215,216,217,218,219,220,221,222,223,224,225,226,227,228,229,230,231,232,233,234,235,236,237,238,239,240,241,242,243,244]',
       '[{"value": "06588", "config": "gygwX"}, {"value": "04044", "config": "ceNof"}]',
       '[{"quota": "PUQaf", "value": "40843"}, {"quota": "KBnyX", "value": "54395"}]',
       '2022-03-22 07:27:50.850000000',
       'system',
       '2022-03-22 07:27:50.852000000',
       'system');
alter table wms_stock_out_detail
    add column `customer` varchar(255) NULL COMMENT '订货人';

alter table wms_stock_in_detail
    add column `supplier` varchar(255) NULL COMMENT '供应商';

delete from mes3.sys_perm where id>244 and id <255;
# 生成配料单
INSERT INTO mes3.sys_perm (id, name, perms, `path`, create_time, create_username, update_time, update_username,
                           IS_DELETED, TENANT_ID, perm_type, module) VALUES(245, '创建配料单',
                                                                            'wms:stock:output:metal:create',
                                                                            '/v1/wms/stock/output/detail/{document-id}/metalfactory/create', '2022-01-06 09:15:09.429000000', 'system', '2022-03-06 13:39:38.069000000', 'system', '1970-01-01 00:00:00', '58a0e82e-0ea2-4046-85c9-9e0e4b19ab21', 'UPDATE', 'WAREHOUSING:STOCKOUT_MAINTAIN');

ALTER TABLE sys_tenant ADD UNIQUE INDEX `U_name_deleted` (`login_name`, `is_deleted`) USING BTREE;
# 盘点相关的权限控制
INSERT INTO mes3.sys_perm (id, name, perms, `path`, create_time, create_username, update_time, update_username, IS_DELETED, TENANT_ID, perm_type, module)
VALUES(246, '创建盘点主单信息', 'wms:stock:inventory:summary:create', '/v1/wms/stock/inventory/summary/create', '2022-03-08
08:07:13.772000000', 'system', '2022-03-08 08:07:13.773000000', 'system', '1970-01-01 00:00:00', '58a0e82e-0ea2-4046-85c9-9e0e4b19ab21', 'UPDATE', 'WAREHOUSING:STOCK_INVENTORY');

INSERT INTO mes3.sys_perm (id, name, perms, `path`, create_time, create_username, update_time, update_username, IS_DELETED, TENANT_ID, perm_type, module)
VALUES(247, '删除盘点主单信息', 'wms:stock:inventory:summary:delete', '/v1/wms/stock/inventory/summary/delete', '2022-03-08
08:07:13.772000000', 'system', '2022-03-08 08:07:13.773000000', 'system', '1970-01-01 00:00:00', '58a0e82e-0ea2-4046-85c9-9e0e4b19ab21', 'UPDATE', 'WAREHOUSING:STOCK_INVENTORY');

INSERT INTO mes3.sys_perm (id, name, perms, `path`, create_time, create_username, update_time, update_username, IS_DELETED, TENANT_ID, perm_type, module)
VALUES(248, '确认盘点主单信息', 'wms:stock:inventory:summary:confirm',
       '/v1/wms/stock/inventory/summary/{document-id}/confirm', '2022-03-08
08:07:13.772000000', 'system', '2022-03-08 08:07:13.773000000', 'system', '1970-01-01 00:00:00', '58a0e82e-0ea2-4046-85c9-9e0e4b19ab21', 'UPDATE', 'WAREHOUSING:STOCK_INVENTORY');

INSERT INTO mes3.sys_perm (id, name, perms, `path`, create_time, create_username, update_time, update_username, IS_DELETED, TENANT_ID, perm_type, module)
VALUES(249, '创建盘点子单信息', 'wms:stock:inventory:detail:create',
       '/v1/wms/stock/inventory/detail/{document-id}/create', '2022-03-08
08:07:13.772000000', 'system', '2022-03-08 08:07:13.773000000', 'system', '1970-01-01 00:00:00', '58a0e82e-0ea2-4046-85c9-9e0e4b19ab21', 'UPDATE', 'WAREHOUSING:STOCK_INVENTORY');

INSERT INTO mes3.sys_perm (id, name, perms, `path`, create_time, create_username, update_time, update_username, IS_DELETED, TENANT_ID, perm_type, module)
VALUES(250, '更新盘点子单信息', 'wms:stock:inventory:detail:update',
       '/v1/wms/stock/inventory/detail/{document-id}/update', '2022-03-08
08:07:13.772000000', 'system', '2022-03-08 08:07:13.773000000', 'system', '1970-01-01 00:00:00', '58a0e82e-0ea2-4046-85c9-9e0e4b19ab21', 'UPDATE', 'WAREHOUSING:STOCK_INVENTORY');

INSERT INTO mes3.sys_perm (id, name, perms, `path`, create_time, create_username, update_time, update_username, IS_DELETED, TENANT_ID, perm_type, module)
VALUES(251, '删除盘点子单信息', 'wms:stock:inventory:detail:delete',
       '/v1/wms/stock/inventory/detail/{document-id}/{id}/update', '2022-03-08
08:07:13.772000000', 'system', '2022-03-08 08:07:13.773000000', 'system', '1970-01-01 00:00:00', '58a0e82e-0ea2-4046-85c9-9e0e4b19ab21', 'UPDATE', 'WAREHOUSING:STOCK_INVENTORY');

INSERT INTO mes3.sys_perm (id, name, perms, `path`, create_time, create_username, update_time, update_username, IS_DELETED, TENANT_ID, perm_type, module)
VALUES(252, '完成盘点子单', 'wms:stock:inventory:detail:complete',
       '/v1/wms/stock/inventory/detail/{document-id}/{id}/complete', '2022-03-08
08:07:13.772000000', 'system', '2022-03-08 08:07:13.773000000', 'system', '1970-01-01 00:00:00', '58a0e82e-0ea2-4046-85c9-9e0e4b19ab21', 'UPDATE', 'WAREHOUSING:STOCK_INVENTORY');

INSERT INTO mes3.sys_perm (id, name, perms, `path`, create_time, create_username, update_time, update_username, IS_DELETED, TENANT_ID, perm_type, module)
VALUES(253, '关闭盘点子单', 'wms:stock:inventory:detail:close',
       '/v1/wms/stock/inventory/detail/{document-id}/{id}/close', '2022-03-08
08:07:13.772000000', 'system', '2022-03-08 08:07:13.773000000', 'system', '1970-01-01 00:00:00', '58a0e82e-0ea2-4046-85c9-9e0e4b19ab21', 'UPDATE', 'WAREHOUSING:STOCK_INVENTORY');

INSERT INTO mes3.sys_perm (id, name, perms, `path`, create_time, create_username, update_time, update_username, IS_DELETED, TENANT_ID, perm_type, module)
VALUES(254, '完成主单盘点', 'wms:stock:inventory:summary:complete',
       '/v1/wms/stock/inventory/summary/{document-id}/complete', '2022-03-08
08:07:13.772000000', 'system', '2022-03-08 08:07:13.773000000', 'system', '1970-01-01 00:00:00', '58a0e82e-0ea2-4046-85c9-9e0e4b19ab21', 'UPDATE', 'WAREHOUSING:STOCK_INVENTORY');


# 用戶表修改
ALTER TABLE sys_user DROP INDEX `U_user_tenant_deleted`;
ALTER TABLE sys_user ADD UNIQUE INDEX `U_name_tenant_deleted` (`username`,`tenant_id`, `is_deleted`) USING BTREE;

