/**
  调试库为mes3，正式发布时修改
 */
use mes3;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

/**
  工艺流程管理模块
 */
-- ----------------------------
-- 模板表
-- ----------------------------
DROP TABLE IF EXISTS `craft_template`;
CREATE TABLE `craft_template`
(
    `id`              bigint       NOT NULL AUTO_INCREMENT COMMENT '模板id',
    `name`            varchar(255) NOT NULL COMMENT '模板名称',
    `type`            varchar(255) NOT NULL COMMENT '模板类型',
    `description`     text         NULL COMMENT '模板描述',
    `is_system`       tinyint(1)   NULL     DEFAULT 0 COMMENT '是否系统内置：内置的模板不允许用户删除、编辑，且跨租户共享',
    `params`          json         NULL COMMENT '模板参数列表',
    `state`           varchar(16)  NOT NULL DEFAULT 'ACTIVE' COMMENT '用户定义状态：ACTIVE/FROZEN',
    `tenant_id`       varchar(64)  NOT NULL COMMENT '租户id',
    `create_time`     datetime(3)  NULL     DEFAULT NULL COMMENT '创建时间',
    `create_username` varchar(64)  NULL     DEFAULT NULL COMMENT '创建人',
    `update_time`     datetime(3)  NULL     DEFAULT NULL COMMENT '最后更新时间',
    `update_username` varchar(64)  NULL     DEFAULT NULL COMMENT '最后更新人',
    `is_deleted`      datetime(3)  NOT NULL DEFAULT '1970-01-01 00:00:00.000' COMMENT '逻辑删除：1970-01-01 00:00:00 表示启用，有值 表示删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 61
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '工艺模板表'
  ROW_FORMAT = DYNAMIC;
-- ----------------------------
-- 模板记录
-- ----------------------------
INSERT INTO mes3.craft_template
(id, name, `type`, description, is_system, params, state, tenant_id, create_time, create_username, update_time,
 update_username)
VALUES (51, '测试模板', 'MATERIAL_TRANSFER', NULL, 1, '[
  {
    "name": "test_param1",
    "type": "STRING",
    "is_required": true,
    "default_value": ""
  },
  {
    "name": "test_param2",
    "type": "INT",
    "is_required": false,
    "default_value": 0
  },
  {
    "name": "测试参数3",
    "type": "BOOL",
    "is_required": false
  }
]', 'ACTIVE', 'aaaaaaaaa', '2021-08-26 07:49:30.757000000', 'anonymous', '2021-08-26 07:49:30.757000000', 'anonymous');


-- ----------------------------
-- 组件表
-- ----------------------------
DROP TABLE IF EXISTS `craft_component`;
CREATE TABLE `craft_component`
(
    `id`               bigint         NOT NULL AUTO_INCREMENT COMMENT 'id',
    `name`             varchar(255)   NOT NULL COMMENT '名称',
    `type`             varchar(255)   NOT NULL COMMENT '类型',
    `description`      text           NULL COMMENT '描述',
    `icon_id`          varchar(32)    NOT NULL COMMENT 'icon-id，前端使用',
    `role_id`          bigint         NULL     DEFAULT NULL COMMENT '绑定角色id',
    `verify_role_id`   bigint         NULL     DEFAULT NULL COMMENT '完成后二次检验角色id，默认为空',
    `template_id`      bigint         NULL     DEFAULT NULL COMMENT '模板id',
    `material_id`      bigint         NULL     DEFAULT NULL COMMENT '物料id',
    `material_num`     decimal(16, 3) NULL     DEFAULT NULL COMMENT '物料数量',
    `transfer_type`    varchar(20)    NULL     DEFAULT 'NA' COMMENT '物料流转类型',
    `material_info`    json           NULL COMMENT '物料信息，json格式，对应MaterialInfoV',
    `qc_info`          json           NULL COMMENT '质检信息，json格式，对应QcInfoV',
    `op_resource_code` varchar(64)    NULL     DEFAULT NULL COMMENT '绑定资源编码，对应工厂的resource表',
    `op_info`          json           NULL COMMENT '加工信息，json格式，对应 OperationInfoV',
    `extra`            json           NULL COMMENT '预埋额外信息',
    `state`            varchar(16)    NOT NULL DEFAULT 'ACTIVE' COMMENT '用户定义状态：ACTIVE/FROZEN',
    `is_deleted`       datetime(3)    NOT NULL DEFAULT '1970-01-01 00:00:00.000' COMMENT '逻辑删除：1970-01-01 00:00:00 表示启用，有值 表示删除',
    `tenant_id`        varchar(64)    NOT NULL COMMENT '租户id',
    `create_time`      datetime(3)    NULL     DEFAULT NULL COMMENT '创建时间',
    `create_username`  varchar(64)    NULL     DEFAULT NULL COMMENT '创建人',
    `update_time`      datetime(3)    NULL     DEFAULT NULL COMMENT '最后更新时间',
    `update_username`  varchar(64)    NULL     DEFAULT NULL COMMENT '最后更新人',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 8
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '工艺组件表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- 工艺流程-组件表
-- ----------------------------
DROP TABLE IF EXISTS `craft_flow`;
CREATE TABLE `craft_flow`
(
    `id`               bigint       NOT NULL AUTO_INCREMENT COMMENT 'id',
    `name`             varchar(255) NOT NULL COMMENT '名称',
    `bind_resource_id` int          NULL     DEFAULT NULL COMMENT '绑定车间资源id',
    `state`            varchar(16)  NOT NULL DEFAULT 'ACTIVE' COMMENT '用户定义状态：ACTIVE/FROZEN',
    `is_deleted`       datetime(3)  NOT NULL DEFAULT '1970-01-01 00:00:00.000' COMMENT '逻辑删除：1970-01-01 00:00:00 表示启用，有值 表示删除',
    `tenant_id`        varchar(64)  NOT NULL COMMENT '租户id',
    `create_time`      datetime(3)  NULL     DEFAULT NULL COMMENT '创建时间',
    `create_username`  varchar(64)  NULL     DEFAULT NULL COMMENT '创建人',
    `update_time`      datetime(3)  NULL     DEFAULT NULL COMMENT '最后更新时间',
    `update_username`  varchar(64)  NULL     DEFAULT NULL COMMENT '最后更新人',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 18
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '工艺流程表'
  ROW_FORMAT = DYNAMIC;

-- 工艺流程-组件表记录
INSERT INTO mes3.craft_component
(id, name, `type`, description, icon_id, role_id, template_id, material_id, material_num, transfer_type, material_info,
 qc_info, op_resource_code, op_info, extra, state, tenant_id, create_time, create_username, update_time,
 update_username)
VALUES (5, '测试步骤', 'MATERIAL_TRANSFER', NULL, '10', 10, 10, 111, 10, 'STOCK_IN', '{
  "id": 100,
  "num": 100,
  "code": "TEST_MAT",
  "name": "测试物料",
  "unit": "吨",
  "transfer_type": "STOCK_IN"
}', NULL, NULL, NULL, NULL, 'ACTIVE', '58a0e82e-0ea2-4046-85c9-9e0e4b19ab21', '2021-09-08 02:39:24.289000000',
        'anonymous', '2021-09-08 02:39:24.289000000', 'anonymous');


-- ----------------------------
-- 表名：craft_flow_relation 工艺流程有向图表（记录图关系）  跟随工艺流程表
-- ----------------------------
DROP TABLE IF EXISTS `craft_flow_relation`;
CREATE TABLE `craft_flow_relation`
(
    `id`              bigint       NOT NULL AUTO_INCREMENT COMMENT 'id',
    `flow_id`         varchar(255) NOT NULL COMMENT '名称',
    `node_relations`  json         NULL COMMENT '工艺步骤详表的关系列表:list<Relations>',
    `is_deleted`      datetime(3)  NOT NULL DEFAULT '1970-01-01 00:00:00.000' COMMENT '逻辑删除：1970-01-01 00:00:00 表示启用，有值 表示删除',
    `tenant_id`       varchar(64)  NOT NULL COMMENT '租户id',
    `create_time`     datetime(3)  NULL     DEFAULT NULL COMMENT '创建时间',
    `create_username` varchar(64)  NULL     DEFAULT NULL COMMENT '创建人',
    `update_time`     datetime(3)  NULL     DEFAULT NULL COMMENT '最后更新时间',
    `update_username` varchar(64)  NULL     DEFAULT NULL COMMENT '最后更新人',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 13
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '工艺流程关系表'
  ROW_FORMAT = DYNAMIC;

INSERT INTO mes3.craft_flow_relation
(id, flow_id, node_relations, tenant_id, create_time, create_username, update_time, update_username)
VALUES (2, '100', '[
  {
    "preNode": "9fd05a39-7c35-480e-baf8-e3e5e98aa7e8",
    "nextNode": ""
  },
  {
    "preNode": "",
    "nextNode": "95f458c0-c5f6-4ed1-9770-f3b1b475ea00"
  },
  {
    "preNode": "e878035f-7008-4bd6-b921-82d3a06cffa7",
    "nextNode": "fa29ad29-9f71-458b-b114-8e549b259588"
  }
]', 'tenant-unknown', '2021-09-01 02:24:01.854000000', 'anonymous', '2021-09-01 02:24:01.854000000', 'anonymous');


-- ----------------------------
-- 表名：craft_flow_node 工艺流程步骤详表（组装组件拼合起来的的工艺步骤详情），
-- 当前用宽表，后续考虑非查找字段整合到json，用代码控制序列化去解析参数；跟随工艺流程简表
-- ----------------------------

DROP TABLE IF EXISTS `craft_flow_node`;
CREATE TABLE `craft_flow_node`
(
    `id`               varchar(40)    NOT NULL COMMENT 'id',
    `flow_id`          bigint         NOT NULL COMMENT '流程id',
    `name`             varchar(255)   NOT NULL COMMENT '名称',
    `type`             varchar(64)    NOT NULL COMMENT '类型',
    `description`      text           NULL COMMENT '描述',
    `icon_id`          varchar(32)    NOT NULL COMMENT 'icon-id，前端使用',
    `role_id`          bigint         NULL     DEFAULT NULL COMMENT '绑定角色id',
    `verify_role_id`   bigint         NULL     DEFAULT NULL COMMENT '完成后二次检验角色id，默认为空',
    `material_id`      bigint         NULL     DEFAULT NULL COMMENT '物料id',
    `material_num`     decimal(16, 3) NULL     DEFAULT NULL COMMENT '物料数量',
    `transfer_type`    varchar(20)    NULL     DEFAULT 'NA' COMMENT '物料流转类型',
    `material_info`    json           NULL COMMENT '物料信息，json格式，对应MaterialInfoV',
    `qc_info`          json           NULL COMMENT '质检信息，json格式，对应QcInfoV',
    `op_resource_code` varchar(64)    NULL     DEFAULT NULL COMMENT '绑定资源编码，对应工厂的resource表',
    `op_info`          json           NULL COMMENT '加工信息，json格式，对应 OperationInfoV',
    `extra`            json           NULL COMMENT '预埋额外信息',
    `is_deleted`       datetime(3)    NOT NULL DEFAULT '1970-01-01 00:00:00.000' COMMENT '逻辑删除：1970-01-01 00:00:00 表示启用，有值 表示删除',
    `tenant_id`        varchar(64)    NOT NULL COMMENT '租户id',
    `update_time`      datetime(3)    NULL     DEFAULT NULL COMMENT '最后更新时间',
    `update_username`  varchar(64)    NULL     DEFAULT NULL COMMENT '最后更新人',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '工艺流程-步骤详表'
  ROW_FORMAT = DYNAMIC;

INSERT INTO mes3.craft_flow_node
(id, flow_id, name, `type`, description, icon_id, role_id, material_id, material_num, transfer_type, material_info,
 qc_info,
 op_resource_code, op_info, extra, tenant_id, update_time, update_username)
VALUES ('2e1453dc-1498-4d05-aa5d-7a0bd2849cb1', 100, '测试步骤', 'MATERIAL_TRANSFER', '测试用步骤', '0', NULL, 100, 100,
        'STOCK_IN', '{
    "id": 100,
    "num": 100,
    "code": "TEST_MAT",
    "name": "测试物料",
    "unit": "吨",
    "transfer_type": "STOCK_IN"
  }', NULL, NULL, NULL, NULL, '58a0e82e-0ea2-4046-85c9-9e0e4b19ab21',
        '2021-09-01 03:10:01.083000000', 'anonymous');


