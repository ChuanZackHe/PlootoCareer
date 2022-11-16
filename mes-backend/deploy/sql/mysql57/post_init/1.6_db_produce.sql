/**
  调试库为mes3，正式发布时修改
 */
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
    `deliver_detail_id`    bigint               DEFAULT NULL COMMENT '交付单子单id',
    `deliver_deadline`     datetime             DEFAULT NULL COMMENT '交付日期（继承交付单的交付日期）',
    `prd_batch`            bigint(20)           DEFAULT NULL COMMENT '在交付单中的分批顺序',
    `num_per_batch`        double               DEFAULT NULL COMMENT '每个分批的生产数量',
    `flow_id`              bigint(20)           DEFAULT NULL COMMENT '工艺流程id（craft_flow.id)',
    `material_id`          bigint(20)           DEFAULT NULL COMMENT '物料id',
    `material_name`        varchar(255)         DEFAULT NULL COMMENT '物料名称',
    `material_unit`        varchar(32)          DEFAULT NULL COMMENT '物料单位',
    `prd_num`              double               DEFAULT NULL COMMENT '本生产单待生产物料数量',
    `output_batch_num`     varchar(64)          DEFAULT NULL COMMENT '手动指定的输出成品批次号',
    `priority`             bigint               DEFAULT 0 COMMENT '优先级，数字越大优先级越高，默认为0',
    `job_stage`            varchar(32)          DEFAULT NULL COMMENT 'job状态：PrdJobStageEnum: 进行中、完成、终止',
    `note`                 text COMMENT '备注',
    `user_define_info`     json COMMENT '用户自定义备注信息，key:val格式的json',
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
    `id`                     bigint(20)   NOT NULL AUTO_INCREMENT COMMENT 'id',
    `task_code`              varchar(128) NOT NULL COMMENT 'task的code（按照工艺步骤先后编码）',
    `prd_job_id`             varchar(64)  NOT NULL COMMENT '所属的job id （prd_job.id)',
    `flow_node_id`           varchar(64)           DEFAULT NULL COMMENT '对应流程的node id（craft_flow_node.id)',
    `name`                   varchar(255) NOT NULL COMMENT 'task名称（继承craft_flow_node.name)',
    `task_type`              varchar(64)  NOT NULL COMMENT 'task类型：入库、出库、质检、加工: PrdTaskTypeEnum',
    `assign_user_id`         bigint       NULL     default -1 COMMENT '绑定用户id',
    `assign_user_real_name`  varchar(50)           DEFAULT 'NA' COMMENT '绑定用户真实姓名',
    `priority`               bigint                DEFAULT 0 COMMENT '优先级，数字越大优先级越高，默认为0，继承job的优先级',
    `target_material_name`   varchar(255) NULL COMMENT '成品物料名称',
    `role_id`                bigint(20)   NOT NULL COMMENT '操作角色id（继承craft_node)',
    `verify_role_id`         bigint       NULL     DEFAULT NULL COMMENT '完成后二次检验角色id，默认为空',
    `op_resource_group_code` varchar(64)           DEFAULT NULL COMMENT '绑定工厂资源组id（继承 craft_flow_node.op_info.op_resource_code)',
    `external_form_id`       varchar(64)           DEFAULT NULL COMMENT '外部单据id（外链到领料、入库、质检单据）',
    `output_batch_num`       varchar(64)           DEFAULT NULL COMMENT '手动指定的输出成品批次号',
    `relations`              json                  DEFAULT NULL COMMENT 'task前后关系',
    `material_info`          json         NULL COMMENT '物料信息，json格式，对应MaterialInfoV',
    `qc_info`                json         NULL COMMENT '质检信息，json格式，对应QcInfoV',
    `op_info`                json         NULL COMMENT '加工信息，json格式，对应 OperationInfoV',
    `operator_name`          varchar(64)           DEFAULT NULL COMMENT '实际执行人名称（提交时更新）',
    `resource_code`          varchar(255)          DEFAULT NULL COMMENT '实际执行的资源（工位/机器）编码',
    `note`                   text COMMENT '备注',
    `user_define_info`       json COMMENT '用户自定义备注信息，key:val格式的json',
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
    `task_id`         bigint(20)   NOT NULL COMMENT '绑定task的id (prd_task.id)',
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
  AUTO_INCREMENT = 2
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='task输入、输出、剩余物料详单';