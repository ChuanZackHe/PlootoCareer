use mes3;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

ALTER TABLE sys_perm
    DROP COLUMN `IS_DELETED`;
ALTER TABLE sys_perm
    DROP COLUMN `TENANT_ID`;

ALTER TABLE sys_role_perm
    DROP COLUMN `IS_DELETED`;
ALTER TABLE sys_role_perm
    DROP COLUMN `TENANT_ID`;
ALTER TABLE sys_role_perm
    DROP COLUMN `CREATE_TIME`;
ALTER TABLE sys_role_perm
    DROP COLUMN `CREATE_USERNAME`;
ALTER TABLE sys_role_perm
    DROP COLUMN `UPDATE_TIME`;
ALTER TABLE sys_role_perm
    DROP COLUMN `UPDATE_USERNAME`;

ALTER TABLE sys_user_role
    DROP COLUMN `IS_DELETED`;
ALTER TABLE sys_user_role
    DROP COLUMN `TENANT_ID`;
ALTER TABLE sys_user_role
    DROP COLUMN `CREATE_TIME`;
ALTER TABLE sys_user_role
    DROP COLUMN `CREATE_USERNAME`;
ALTER TABLE sys_user_role
    DROP COLUMN `UPDATE_TIME`;
ALTER TABLE sys_user_role
    DROP COLUMN `UPDATE_USERNAME`;

alter table craft_component
    modify icon_id int NULL;
alter table craft_flow_node
    modify icon_id int NULL;

ALTER TABLE fac_resource_relation
    DROP COLUMN `IS_DELETED`;

ALTER TABLE craft_flow_relation
    DROP COLUMN `CREATE_TIME`;
ALTER TABLE craft_flow_relation
    DROP COLUMN `CREATE_USERNAME`;

ALTER TABLE base_qc_item
    DROP COLUMN guide_file_name;