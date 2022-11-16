use mes3;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

/**
  生产管理模块
 */

-- ----------------------------
-- job表
-- ----------------------------
DROP TABLE IF EXISTS `prd_job`;
CREATE TABLE `prd_job`
(
    `id`                   varchar(64) NOT NULL COMMENT '编码id，使用单号Util生成',
    `order_summary_code`   varchar(64)          DEFAULT NULL COMMENT '订单主单code',
    `deliver_summary_code` varchar(64)          DEFAULT NULL COMMENT '交付单主单code',
    `deliver_detail_code`  varchar(64)          DEFAULT NULL COMMENT '交付单子单code',
    `deliver_deadline`     datetime             DEFAULT NULL COMMENT '交付日期（继承交付单的交付日期）',
    `prd_batch`            bigint(20)           DEFAULT NULL COMMENT '在交付单中的分批顺序',
    `num_per_batch`        double               DEFAULT NULL COMMENT '每个分批的生产数量',
    `flow_id`              bigint(20)           DEFAULT NULL COMMENT '工艺流程id（craft_flow.id)',
    `material_id`          bigint(20)           DEFAULT NULL COMMENT '物料id',
    `material_name`        varchar(255)         DEFAULT NULL COMMENT '物料名称',
    `material_unit`        varchar(32)          DEFAULT NULL COMMENT '物料单位',
    `prd_num`              double               DEFAULT NULL COMMENT '本生产单待生产物料数量',
    `job_stage`            varchar(32)          DEFAULT NULL COMMENT 'job状态：PrdJobStageEnum: 进行中、完成、终止',
    `note`                 text COMMENT '备注',
    `is_deleted`           datetime(3) NOT NULL DEFAULT '1970-01-01 00:00:00.000' COMMENT '逻辑删除：1970-01-01 00:00:00 表示启用，有值 表示删除',
    `tenant_id`            varchar(64) NOT NULL COMMENT '租户id',
    `create_time`          datetime(3)          DEFAULT NULL COMMENT '创建时间',
    `create_username`      varchar(64)          DEFAULT NULL COMMENT '创建人',
    `update_time`          datetime(3)          DEFAULT NULL COMMENT '最后更新时间',
    `update_username`      varchar(64)          DEFAULT NULL COMMENT '最后更新人',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='生产单，和工艺流程对应';
-- ----------------------------
-- task表
-- ----------------------------
DROP TABLE IF EXISTS `prd_task`;
CREATE TABLE `prd_task`
(
    `id`                     varchar(192) NOT NULL COMMENT 'task的完整编码id（job_id+task_id)',
    `task_id`                varchar(128) NOT NULL COMMENT 'task的id（按照工艺步骤先后编码）',
    `prd_job_id`             varchar(64)  NOT NULL COMMENT '所属的job id （prd_job.id)',
    `flow_node_id`           varchar(32)           DEFAULT NULL COMMENT '对应流程的node id（craft_flow_node.id)',
    `name`                   varchar(255) NOT NULL COMMENT 'task名称（继承craft_flow_node.name)',
    `task_type`              varchar(64)  NOT NULL COMMENT 'task类型：入库、出库、质检、加工: PrdTaskTypeEnum',
    `role_id`                bigint(20)   NOT NULL COMMENT '操作角色id（继承craft_node)',
    `op_resource_group_code` varchar(64)           DEFAULT NULL COMMENT '绑定工厂资源组id（继承 craft_flow_node.op_info.op_resource_code)',
    `external_form_id`       varchar(64)           DEFAULT NULL COMMENT '外部单据id（外链到领料、入库、质检单据）',
    `operator_name`          varchar(64)           DEFAULT NULL COMMENT '实际执行人名称（提交时更新）',
    `resource_code`          varchar(255)          DEFAULT NULL COMMENT '实际执行的资源（工位/机器）编码',
    `note`                   text COMMENT '备注',
    `task_stage`             varchar(32)  NOT NULL COMMENT 'task状态（所处阶段），TaskStageEnum: BLOCK,READY,RUNNING, SUCCESS，FAIL，ABORT',
    `start_time`             datetime              DEFAULT NULL COMMENT 'task开始执行时间',
    `end_time`               datetime              DEFAULT NULL COMMENT 'task执行完成时间',
    `cost_time`              bigint(20)            DEFAULT NULL COMMENT '花费时间（单位为秒）',
    `is_deleted`             datetime(3)  NOT NULL DEFAULT '1970-01-01 00:00:00.000' COMMENT '逻辑删除：1970-01-01 00:00:00 表示启用，有值 表示删除',
    `tenant_id`              varchar(64)  NOT NULL COMMENT '租户id',
    `create_time`            datetime(3)           DEFAULT NULL COMMENT '创建时间',
    `create_username`        varchar(64)           DEFAULT NULL COMMENT '创建人',
    `update_time`            datetime(3)           DEFAULT NULL COMMENT '最后更新时间',
    `update_username`        varchar(64)           DEFAULT NULL COMMENT '最后更新人',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='生产任务表，与工艺流程中的节点对应';

-- ----------------------------
-- task_material_detail表
-- ----------------------------
DROP TABLE IF EXISTS `prd_task_material_detail`;
CREATE TABLE `prd_task_material_detail`
(
    `id`              bigint(20)   NOT NULL AUTO_INCREMENT COMMENT 'id',
    `job_id`          varchar(64)  NOT NULL COMMENT 'task所属的job-id（优化查询用）',
    `task_id`         varchar(192) NOT NULL COMMENT '绑定task的id (prd_task.id)',
    `type`            varchar(128) NOT NULL COMMENT '类型：输入/输出/剩余：PrdTaskMaterialType',
    `material_id`     bigint(20)            DEFAULT NULL COMMENT '物料id',
    `material_name`   varchar(255)          DEFAULT NULL COMMENT '物料名称',
    `material_unit`   varchar(32)           DEFAULT NULL COMMENT '基本单位, 从base_unit表获取',
    `batch_num`       varchar(128)          DEFAULT NULL COMMENT '批次号',
    `expected_num`    double                DEFAULT NULL COMMENT '预期数量',
    `real_num`        double                DEFAULT NULL COMMENT '实际数量',
    `is_deleted`      datetime(3)  NOT NULL DEFAULT '1970-01-01 00:00:00.000' COMMENT '逻辑删除：1970-01-01 00:00:00 表示启用，有值 表示删除',
    `tenant_id`       varchar(64)  NOT NULL COMMENT '租户id',
    `create_time`     datetime(3)           DEFAULT NULL COMMENT '创建时间',
    `create_username` varchar(64)           DEFAULT NULL COMMENT '创建人',
    `update_time`     datetime(3)           DEFAULT NULL COMMENT '最后更新时间',
    `update_username` varchar(64)           DEFAULT NULL COMMENT '最后更新人',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='生产任务物料记录表';


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
    UNIQUE INDEX `storage_code_idx_uq` (`code`, `tenant_id`, `is_deleted`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '仓储资源表'
  ROW_FORMAT = Dynamic;


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


DROP TABLE IF EXISTS `wms_stock_summary`;
CREATE TABLE `wms_stock_summary`
(
    `id`              bigint                                                        NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `material_id`     bigint                                                        NOT NULL COMMENT '物料id',
    `material_unit`   varchar(16)                                                   NOT NULL COMMENT '物料基本单位',
    `material_name`   varchar(255)                                                  NOT NULL COMMENT '物料名称',
    `count`           double                                                        NOT NULL COMMENT '库存数量',
    `create_time`     datetime(3)                                                   NULL     DEFAULT NULL COMMENT '创建时间',
    `create_username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL     DEFAULT NULL COMMENT '创建用户名',
    `update_time`     datetime(3)                                                   NULL     DEFAULT NULL COMMENT '更新时间',
    `update_username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL     DEFAULT NULL COMMENT '更新用户名',
    `tenant_id`       varchar(64)                                                   NOT NULL COMMENT '租户id',
    `is_deleted`      datetime(3)                                                   NOT NULL DEFAULT '1970-01-01 00:00:00.000' COMMENT '逻辑删除位，‘1970-01-01 00:00:00’为未删除，其余为删除',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_index` (`material_id`) USING BTREE,
    INDEX `NAME_INDEX` (`material_name`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '记录当前在库的实时库存概览'
  ROW_FORMAT = Dynamic;

DROP TABLE IF EXISTS `wms_stock_detail`;
CREATE TABLE `wms_stock_detail`
(
    `id`              bigint                                                        NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `warehouse_code`  varchar(32)                                                   NOT NULL COMMENT '仓库code',
    `carrier_code`    varchar(32)                                                   NOT NULL COMMENT '货架code',
    `location_code`   varchar(32)                                                   NOT NULL COMMENT '库位code',
    `material_id`     bigint                                                        NOT NULL COMMENT '物料id',
    `material_unit`   varchar(16)                                                   NOT NULL COMMENT '物料基本单位',
    `material_name`   varchar(255)                                                  NOT NULL COMMENT '物料名称',
    `material_price`  double                                                        NULL     DEFAULT NULL COMMENT '物料单位价格',
    `count`           double                                                        NOT NULL COMMENT '库存数量',
    `batch_number`    varchar(64)                                                   NULL     DEFAULT NULL COMMENT '批次号',
    `create_time`     datetime(3)                                                   NULL     DEFAULT NULL COMMENT '创建时间',
    `create_username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL     DEFAULT NULL COMMENT '创建用户名',
    `update_time`     datetime(3)                                                   NULL     DEFAULT NULL COMMENT '更新时间',
    `update_username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL     DEFAULT NULL COMMENT '更新用户名',
    `tenant_id`       varchar(64)                                                   NOT NULL COMMENT '租户id',
    `is_deleted`      datetime(3)                                                   NOT NULL DEFAULT '1970-01-01 00:00:00.000' COMMENT '逻辑删除位，‘1970-01-01 00:00:00’为未删除，其余为删除',
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
    `id`              bigint                                                        NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `warehouse_code`  varchar(255)                                                  NOT NULL COMMENT '仓库code',
    `carrier_code`    varchar(255)                                                  NOT NULL COMMENT '货架code',
    `location_code`   varchar(255)                                                  NOT NULL COMMENT '库位code',
    `material_id`     bigint                                                        NOT NULL COMMENT '物料id',
    `material_name`   varchar(255)                                                  NOT NULL COMMENT '物料名称',
    `count`           double                                                        NOT NULL COMMENT '操作数量',
    `document_id`     varchar(255)                                                  NULL     DEFAULT NULL COMMENT '单据号',
    `type`            varchar(32)                                                   NOT NULL COMMENT '操作类型',
    `create_time`     datetime(3)                                                   NULL     DEFAULT NULL COMMENT '创建时间',
    `create_username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL     DEFAULT NULL COMMENT '创建用户名',
    `update_time`     datetime(3)                                                   NULL     DEFAULT NULL COMMENT '更新时间',
    `update_username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL     DEFAULT NULL COMMENT '更新用户名',
    `tenant_id`       varchar(64)                                                   NOT NULL COMMENT '租户id',
    `is_deleted`      datetime(3)                                                   NOT NULL DEFAULT '1970-01-01 00:00:00.000' COMMENT '逻辑删除位，‘1970-01-01 00:00:00’为未删除，其余为删除',
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
    `material_id`     bigint       NOT NULL COMMENT '物料id',
    `material_unit`   varchar(16)  NOT NULL COMMENT '物料基本单位',
    `material_name`   varchar(255) NOT NULL COMMENT '物料名称',
    `material_price`  double       NULL     DEFAULT NULL COMMENT '物料单位价格',
    `count`           double       NOT NULL COMMENT '库存数量',
    `real_count`      double       NULL     DEFAULT NULL COMMENT '实际入库存数量',
    `batch_number`    varchar(64)  NULL     DEFAULT NULL COMMENT '批次号',
    `status`          varchar(32)  NULL     DEFAULT NULL COMMENT '子单状态',
    `warehouse_code`  varchar(32)  NULL     DEFAULT NULL COMMENT '仓库code',
    `carrier_code`    varchar(32)  NULL     DEFAULT NULL COMMENT '货架code',
    `location_code`   varchar(32)  NULL     DEFAULT NULL COMMENT '库位code',
    `description`     varchar(255) NULL     DEFAULT NULL COMMENT '异常的详细描述',
    `info`            text         NULL COMMENT '描述',
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
  COLLATE = utf8mb4_unicode_ci COMMENT = '入库子单'
  ROW_FORMAT
      = Dynamic;

DROP TABLE IF EXISTS `wms_stock_out_summary`;
CREATE TABLE `wms_stock_out_summary`
(
    `id`              bigint       NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `document_id`     varchar(255) NOT NULL COMMENT '单据id',
    `delivery_id`     varchar(255) NULL     DEFAULT NULL COMMENT '关联的交付单编号/生产单编号',
    `customer`        varchar(255) NOT NULL COMMENT '订货人',
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
    `warehouse_code`  varchar(32)  NULL     DEFAULT NULL COMMENT '仓库code',
    `carrier_code`    varchar(32)  NULL     DEFAULT NULL COMMENT '货架code',
    `location_code`   varchar(32)  NULL     DEFAULT NULL COMMENT '库位code',
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
  COLLATE = utf8mb4_unicode_ci COMMENT = '出库子单'
  ROW_FORMAT
      = Dynamic;

delete
from sys_perm
where perms LIKE 'wms%';
delete
from sys_perm
where perms = 'factory:resources:list';
