use mes3;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;


ALTER TABLE sys_perm
    ADD `IS_DELETED` DATETIME(3) NULL DEFAULT '1970-01-01 00:00:00.000' COMMENT '逻辑删除：1970-01-01
00:00:00 表示启用，有值 表示删除';
ALTER TABLE sys_perm
    ADD `TENANT_ID` VARCHAR(64) NULL DEFAULT NULL COMMENT '企业ID';
update sys_perm
set sys_perm.IS_DELETED='1970-01-01 00:00:00.000'
where sys_perm.IS_DELETED is null;


ALTER TABLE sys_role_perm
    ADD `IS_DELETED` DATETIME(3) NULL DEFAULT '1970-01-01 00:00:00.000' COMMENT '逻辑删除：1970-01-01 00:00:00 表示启用，有值 表示删除';
ALTER TABLE sys_role_perm
    ADD `TENANT_ID` VARCHAR(64) NULL DEFAULT NULL COMMENT '企业ID';
ALTER TABLE sys_role_perm
    ADD `CREATE_TIME` DATETIME(3) NULL DEFAULT NULL COMMENT '创建时间';
ALTER TABLE sys_role_perm
    ADD `CREATE_USERNAME` VARCHAR(64) NULL DEFAULT NULL COMMENT '创建人';
ALTER TABLE sys_role_perm
    ADD `UPDATE_TIME` DATETIME(3) NULL DEFAULT NULL COMMENT '最后更新时间';
ALTER TABLE sys_role_perm
    ADD `UPDATE_USERNAME` VARCHAR(64) NULL DEFAULT NULL COMMENT '最后更新人';
update sys_role_perm
set sys_role_perm.IS_DELETED='1970-01-01 00:00:00.000'
where sys_role_perm.IS_DELETED is null;

ALTER TABLE sys_user_role
    ADD `IS_DELETED` DATETIME(3) NULL DEFAULT '1970-01-01 00:00:00.000' COMMENT '逻辑删除：1970-01-01 00:00:00 表示启用，有值 表示删除';
ALTER TABLE sys_user_role
    ADD `TENANT_ID` VARCHAR(64) NULL DEFAULT NULL COMMENT '企业ID';
ALTER TABLE sys_user_role
    ADD `CREATE_TIME` DATETIME(3) NULL DEFAULT NULL COMMENT '创建时间';
ALTER TABLE sys_user_role
    ADD `CREATE_USERNAME` VARCHAR(64) NULL DEFAULT NULL COMMENT '创建人';
ALTER TABLE sys_user_role
    ADD `UPDATE_TIME` DATETIME(3) NULL DEFAULT NULL COMMENT '最后更新时间';
ALTER TABLE sys_user_role
    ADD `UPDATE_USERNAME` VARCHAR(64) NULL DEFAULT NULL COMMENT '最后更新人';
update sys_user_role
set sys_user_role.IS_DELETED='1970-01-01 00:00:00.000'
where sys_user_role.IS_DELETED is null;

alter table craft_component
    modify icon_id varchar(32) NOT NULL;
alter table craft_flow_node
    modify icon_id varchar(32) NOT NULL;

ALTER TABLE fac_resource_relation
    ADD `IS_DELETED` DATETIME(3) NULL DEFAULT '1970-01-01 00:00:00.000' COMMENT '逻辑删除：1970-01-01 00:00:00 表示启用，有值 表示删除';
update fac_resource_relation
set fac_resource_relation.IS_DELETED='1970-01-01 00:00:00.000'
where fac_resource_relation.IS_DELETED is null;

ALTER TABLE craft_flow_relation
    ADD `CREATE_TIME` DATETIME(3) NULL DEFAULT NULL COMMENT '创建时间';
ALTER TABLE craft_flow_relation
    ADD `CREATE_USERNAME` VARCHAR(64) NULL DEFAULT NULL COMMENT '创建人';

ALTER TABLE base_qc_item
    ADD `guide_file_name` text COMMENT '质检指导手册（图片或pdf等）获取路径（url或本地路径）';