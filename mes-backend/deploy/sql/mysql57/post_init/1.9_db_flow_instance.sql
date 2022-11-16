use mes3;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS `state_flow_instance`;
CREATE TABLE `state_flow_instance` (
                                       `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
                                       `instance_Id` varchar(255)  NOT NULL COMMENT '状态流实例ID',
                                       `name` varchar(255) NOT NULL COMMENT '实例名称',
                                       `definition_name` varchar(255)  NOT NULL COMMENT '流程定义名称',
                                       `definition_key` varchar(255)  NOT NULL COMMENT '流程定义Key',
                                       `business` varchar(64)  NOT NULL COMMENT '所属业务模块',
                                       `state` varchar(32)  NOT NULL COMMENT '状态',
                                       `node_type` varchar(32)  NULL COMMENT '状态',
                                       `version` bigint(20)  NOT NULL COMMENT '版本',
                                       `start_user` bigint(20)  NOT NULL COMMENT '启动用户',
                                       `document_id` varchar(255)  NOT NULL COMMENT '绑定的流转单据ID',
                                       `create_time` datetime(3) DEFAULT NULL COMMENT '创建时间',
                                       `create_username` varchar(255)  DEFAULT NULL COMMENT '创建用户名',
                                       `update_time` datetime(3) DEFAULT NULL COMMENT '更新时间',
                                       `update_username` varchar(255)  DEFAULT NULL COMMENT '更新用户名',
                                       `tenant_id` varchar(64)  NOT NULL COMMENT '租户id',
                                       `is_deleted` datetime(3) NOT NULL DEFAULT '1970-01-01 00:00:00.000' COMMENT '逻辑删除位，‘1970-01-01 00:00:00’为未删除，其余为删除',
                                       PRIMARY KEY (`id`) USING BTREE,
                                       UNIQUE INDEX `instance_id_uq` (`instance_id`) USING BTREE,
                                       KEY `business_index` (`business`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='状态流转实例表';


DROP TABLE IF EXISTS `state_flow_instance_history`;
CREATE TABLE `state_flow_instance_history` (
                                               `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
                                               `instance_Id` varchar(255)  NOT NULL COMMENT '状态流实例ID',
                                               `name` varchar(255) NOT NULL COMMENT '实例名称',
                                               `definition_name` varchar(255)  NOT NULL COMMENT '流程定义名称',
                                               `definition_key` varchar(255)  NOT NULL COMMENT '流程定义Key',
                                               `business` varchar(64)  NOT NULL COMMENT '所属业务模块',
                                               `state` varchar(32)  NOT NULL COMMENT '状态',
                                               `version` bigint(20)  NOT NULL COMMENT '版本',
                                               `document_id` varchar(255)  NOT NULL COMMENT '绑定的流转单据ID',
                                               `create_time` datetime(3) DEFAULT NULL COMMENT '创建时间',
                                               `create_username` varchar(255)  DEFAULT NULL COMMENT '创建用户名',
                                               `update_time` datetime(3) DEFAULT NULL COMMENT '更新时间',
                                               `update_username` varchar(255)  DEFAULT NULL COMMENT '更新用户名',
                                               `tenant_id` varchar(64)  NOT NULL COMMENT '租户id',
                                               `is_deleted` datetime(3) NOT NULL DEFAULT '1970-01-01 00:00:00.000' COMMENT '逻辑删除位，‘1970-01-01 00:00:00’为未删除，其余为删除',
                                               PRIMARY KEY (`id`) USING BTREE,
                                               UNIQUE INDEX `instance_id_uq` (`instance_id`) USING BTREE,
                                               KEY `business_index` (`business`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='状态流转实例历史表';


DROP TABLE IF EXISTS `state_flow_detail`;
CREATE TABLE `state_flow_detail` (
                                     `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
                                     `instance_Id` varchar(255)  NOT NULL COMMENT '状态流实例ID',
                                     `last_state` varchar(32)  NOT NULL COMMENT '上一个流程状态',
                                     `next_state` varchar(32)  NULL COMMENT '下一个流程状态',
                                     `event` json  NOT NULL COMMENT '触发事件',
                                     `message` varchar(255)  NULL COMMENT '备注',
                                     `handler_key` varchar(32)  NOT NULL COMMENT '处理器的KEY',
                                     `trigger_user` varchar(32)  NOT NULL COMMENT '触发的用户',
                                     `execute_error` text NULL COMMENT '执行的错误信息',
                                     `success` tinyint(1)  NOT NULL COMMENT '触发的用户',
                                     `create_time` datetime(3) DEFAULT NULL COMMENT '创建时间',
                                     `create_username` varchar(255)  DEFAULT NULL COMMENT '创建用户名',
                                     `update_time` datetime(3) DEFAULT NULL COMMENT '更新时间',
                                     `update_username` varchar(255)  DEFAULT NULL COMMENT '更新用户名',
                                     `tenant_id` varchar(64)  NOT NULL COMMENT '租户id',
                                     `is_deleted` datetime(3) NOT NULL DEFAULT '1970-01-01 00:00:00.000' COMMENT '逻辑删除位，‘1970-01-01 00:00:00’为未删除，其余为删除',
                                     PRIMARY KEY (`id`) USING BTREE,
                                     KEY `instance_id` (`instance_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='状态流转细节表';



DROP TABLE IF EXISTS `state_flow_definition`;
CREATE TABLE `state_flow_definition` (
                                         `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
                                         `definition_name` varchar(255)  NOT NULL COMMENT '状态流定义name',
                                         `definition_key` varchar(32)  NOT NULL COMMENT '状态流定义Key',
                                         `business` varchar(32)  NOT NULL COMMENT '业务模块',
                                         `definition` json  NOT NULL COMMENT '具体定义',
                                         `create_time` datetime(3) DEFAULT NULL COMMENT '创建时间',
                                         `create_username` varchar(255)  DEFAULT NULL COMMENT '创建用户名',
                                         `update_time` datetime(3) DEFAULT NULL COMMENT '更新时间',
                                         `update_username` varchar(255)  DEFAULT NULL COMMENT '更新用户名',
                                         `tenant_id` varchar(64)  NOT NULL COMMENT '租户id',
                                         `is_deleted` datetime(3) NOT NULL DEFAULT '1970-01-01 00:00:00.000' COMMENT '逻辑删除位，‘1970-01-01 00:00:00’为未删除，其余为删除',
                                         PRIMARY KEY (`id`) USING BTREE,
                                         UNIQUE KEY `business_tenant_uk` (`business`, `tenant_id`) USING BTREE,
                                         KEY `definition_key` (`definition_key`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='状态流程定义表';


DROP TABLE IF EXISTS mes3.procurement_summary;
CREATE TABLE mes3.procurement_summary
(
    `id`              bigint       NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `document_id`      varchar(255)   NOT NULL COMMENT '采购单单号',
    `purchaser`       varchar(255)    NOT NULL COMMENT '采购人',
    `supplier`        varchar(255) NOT NULL COMMENT '供应商',
    `status`          varchar(32)  NULL   DEFAULT NULL COMMENT '验收时间',
    `settlement`      varchar(16)  NULL   DEFAULT NULL COMMENT '结算状态',
    `description`      varchar(255)  NULL   DEFAULT NULL COMMENT '备注',
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
    comment '采购信息主表';

DROP TABLE IF EXISTS mes3.procurement_detail;
CREATE TABLE mes3.procurement_detail
(
    `id`              bigint       NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `document_id`      varchar(255)   NOT NULL COMMENT '采购单单号',
    `material_unit`   varchar(16)   NOT NULL COMMENT '物料单位',
    `material_name`   varchar(255) NOT NULL COMMENT '物料名称',
    `material_id`     bigint      NOT NULL COMMENT '物料ID',
    `purchase_count`  double      NULL COMMENT '采购数量',
    `description`     varchar(255)  DEFAULT NULL COMMENT '详细描述',
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
    comment '采购信息详情';



DROP TABLE IF EXISTS `procurement_settlement`;
CREATE TABLE `procurement_settlement` (
                                          `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
                                          `document_id` varchar(255)  NOT NULL COMMENT '状态流实例ID',
                                          `supplier` varchar(255)  NOT NULL COMMENT '上一个流程状态',
                                          `total` double  NULL COMMENT '总价',
                                          `details` json  NOT NULL COMMENT '结算细节',
                                          `procurement_id` varchar(255)  NULL COMMENT '采购单的单据号',
                                          `pay_status` varchar(32)  NULL COMMENT '支付状态',
                                          `create_time` datetime(3) DEFAULT NULL COMMENT '创建时间',
                                          `create_username` varchar(255)  DEFAULT NULL COMMENT '创建用户名',
                                          `update_time` datetime(3) DEFAULT NULL COMMENT '更新时间',
                                          `update_username` varchar(255)  DEFAULT NULL COMMENT '更新用户名',
                                          `tenant_id` varchar(64)  NOT NULL COMMENT '租户id',
                                          `is_deleted` datetime(3) NOT NULL DEFAULT '1970-01-01 00:00:00.000' COMMENT '逻辑删除位，‘1970-01-01 00:00:00’为未删除，其余为删除',
                                          PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='结算单';

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