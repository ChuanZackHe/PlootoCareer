use mes3;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;


-- ----------------------------
-- Table structure for sys_perm
-- ----------------------------
DROP TABLE IF EXISTS `sys_perm`;
CREATE TABLE `sys_perm`
(
    `id`              bigint       NOT NULL AUTO_INCREMENT COMMENT '权限ID',
    `name`            varchar(50)  NULL     DEFAULT NULL COMMENT '权限名称',
    `perms`           varchar(500) NULL     DEFAULT NULL COMMENT '权限标识',
    `path`            varchar(500) NULL     DEFAULT NULL COMMENT '权限api路径',
    `is_deleted`      datetime(3)  NOT NULL DEFAULT '1970-01-01 00:00:00.000' COMMENT '逻辑删除：1970-01-01 00:00:00 表示启用，有值 表示删除',
    `tenant_id`       varchar(64)  NULL     DEFAULT NULL COMMENT '企业id',
    `create_time`     datetime(3)  NULL     DEFAULT NULL COMMENT '创建时间',
    `create_username` varchar(64)  NULL     DEFAULT NULL COMMENT '创建人',
    `update_time`     datetime(3)  NULL     DEFAULT NULL COMMENT '最后更新时间',
    `update_username` varchar(64)  NULL     DEFAULT NULL COMMENT '最后更新人',
    `perm_type`       varchar(8)   NULL COMMENT '权限类型，UPDATE 修改新增，GET 查看',
    `module`          varchar(255) NULL COMMENT '模块类型',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 5
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci COMMENT = '权限表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_perm
-- ----------------------------

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`
(
    `id`              bigint      NOT NULL AUTO_INCREMENT COMMENT '角色ID',
    `name`            varchar(50) NOT NULL COMMENT '角色名称',
    `role`            varchar(50) NOT NULL COMMENT '角色标识',
    `tenant_id`       varchar(64) NULL     DEFAULT NULL COMMENT '企业id',
    `create_time`     datetime(3) NULL     DEFAULT NULL COMMENT '创建时间',
    `create_username` varchar(64) NULL     DEFAULT NULL COMMENT '创建人',
    `update_time`     datetime(3) NULL     DEFAULT NULL COMMENT '最后更新时间',
    `update_username` varchar(64) NULL     DEFAULT NULL COMMENT '最后更新人',
    `is_deleted`      datetime(3) NOT NULL DEFAULT '1970-01-01 00:00:00.000' COMMENT '逻辑删除：1970-01-01 00:00:00 表示启用，有值 表示删除',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `UK_name_tenant_deleted` (`name`, `tenant_id`, `is_deleted`) USING BTREE,
    UNIQUE KEY `UK_role_tenant_deleted` (`role`, `tenant_id`, `is_deleted`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 3
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci COMMENT = '角色表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role`
VALUES (1, '管理员', 'ADMIN', '58a0e82e-0ea2-4046-85c9-9e0e4b19ab21', now(), 'sys', now(), 'sys',
        '1970-01-01 00:00:00.000');
INSERT INTO `sys_role`
VALUES (2, '普通用户', 'USER', '58a0e82e-0ea2-4046-85c9-9e0e4b19ab21', now(), 'sys', now(), 'sys',
        '1970-01-01 00:00:00.000');

-- ----------------------------
-- Table structure for sys_role_perm
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_perm`;
CREATE TABLE `sys_role_perm`
(
    `id`              bigint      NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `role_id`         bigint      NULL     DEFAULT NULL COMMENT '角色ID',
    `perm_id`         bigint      NULL     DEFAULT NULL COMMENT '权限ID',
    `is_deleted`      datetime(3) NOT NULL DEFAULT '1970-01-01 00:00:00.000' COMMENT '逻辑删除：1970-01-01 00:00:00 表示启用，有值 表示删除',
    `tenant_id`       varchar(64) NULL     DEFAULT NULL COMMENT '企业id',
    `create_time`     datetime(3) NULL     DEFAULT NULL COMMENT '创建时间',
    `create_username` varchar(64) NULL     DEFAULT NULL COMMENT '创建人',
    `update_time`     datetime(3) NULL     DEFAULT NULL COMMENT '最后更新时间',
    `update_username` varchar(64) NULL     DEFAULT NULL COMMENT '最后更新人',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci COMMENT = '角色与权限关系表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role_perm
-- ----------------------------


-- ----------------------------
-- Table structure for sys_tenant
-- ----------------------------
DROP TABLE IF EXISTS `sys_tenant`;
CREATE TABLE `sys_tenant`
(
    `id`              bigint       NOT NULL AUTO_INCREMENT COMMENT '主键',
    `tenant_id`       varchar(64)  NOT NULL COMMENT '租户id',
    `login_name`      varchar(255) NULL     DEFAULT NULL COMMENT '租户名',
    `state`           varchar(16)  NULL     DEFAULT 'ACTIVE' COMMENT '0 表示启用，1表示冻结',
    `create_time`     datetime(3)  NULL     DEFAULT NULL COMMENT '创建时间',
    `create_username` varchar(64)  NULL     DEFAULT NULL,
    `update_time`     datetime(3)  NULL     DEFAULT NULL,
    `update_username` varchar(64)  NULL     DEFAULT NULL,
    `is_deleted`      datetime(3)  NOT NULL DEFAULT '1970-01-01 00:00:00.000' COMMENT '逻辑删除：1970-01-01 00:00:00 表示启用，有值 表示删除',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `U_tenant_deleted` (`tenant_id`, `is_deleted`) USING BTREE,
    UNIQUE INDEX `U_name_deleted` (`login_name`, `is_deleted`) USING BTREE,
    INDEX `tenant_id` (`tenant_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 20
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '企业租户'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_tenant
-- ----------------------------
INSERT INTO mes3.sys_tenant
(id, tenant_id, login_name, state, create_time, create_username, update_time, update_username)
VALUES (10, '58a0e82e-0ea2-4046-85c9-9e0e4b19ab21', 'user', 'ACTIVE', '2021-08-01 00:00:00', 'sys', '2021-08-01
00:00:00', 'sys');

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
-- mes3.sys_user definition
CREATE TABLE `sys_user`
(
    `id`              bigint       NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `username`        varchar(50)  NOT NULL COMMENT '用户名',
    `password`        varchar(100) NOT NULL COMMENT '密码',
    `salt`            varchar(50)  NOT NULL COMMENT '盐值',
    `real_name`       varchar(50)  NOT NULL COMMENT '用户真实姓名',
    `state`           varchar(16)  NULL     DEFAULT 'ACTIVE' COMMENT '状态:0-激活，1-冻结',
    `tenant_id`       varchar(64)  NULL     DEFAULT NULL COMMENT '企业id',
    `create_time`     datetime(3)  NULL     DEFAULT NULL COMMENT '创建时间',
    `create_username` varchar(64)  NULL     DEFAULT NULL COMMENT '创建人',
    `update_time`     datetime(3)  NULL     DEFAULT NULL COMMENT '最后更新时间',
    `update_username` varchar(64)  NULL     DEFAULT NULL COMMENT '最后更新人',
    `is_deleted`      datetime(3)  NOT NULL DEFAULT '1970-01-01 00:00:00.000' COMMENT '逻辑删除：1970-01-01 00:00:00 表示启用，有值 表示删除',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `U_name_tenant_deleted` (`username`,`tenant_id`, `is_deleted`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 3
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '系统用户表'
  ROW_FORMAT = DYNAMIC;
-- ----------------------------
-- Records of sys_user， 默认登录初始密码：2$9d&J2kd@@kc9!4
-- ----------------------------
INSERT INTO mes3.sys_user
(id, username, password, salt, real_name, state, tenant_id, create_time, create_username, update_time, update_username)
VALUES (1, 'admin', 'cb49020376df4db2a9db31460af0b89dc988926a9dd7de1dbe90115e91a321c4', 'RvP3UID2n30Q2sycZYvH',
        '租户用户名1', 'ACTIVE', '58a0e82e-0ea2-4046-85c9-9e0e4b19ab21', '2021-08-03 23:19:17', 'sys', '2021-02-17 23:19:17',
        'sys');
INSERT INTO mes3.sys_user
(id, username, password, salt, real_name, state, tenant_id, create_time, create_username, update_time, update_username)
VALUES (2, 'user', '301f93c68e48df51565fbb15d0b61d69ce61f60dc87c8d47ab282a5a161a0cdb', 'OVlrD37bDUKNcFRB10qG',
        '租户用户名2', 'ACTIVE', '58a0e82e-0ea2-4046-85c9-9e0e4b19ab21', '2021-02-17 23:19:17', 'sys', '2021-02-17 23:19:17',
        'sys');

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`
(
    `id`              bigint      NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `user_id`         bigint      NULL     DEFAULT NULL COMMENT '用户ID',
    `role_id`         bigint      NULL     DEFAULT NULL COMMENT '角色ID',
    `create_time`     datetime(3) NULL     DEFAULT NULL COMMENT '创建时间',
    `create_username` varchar(64) NULL     DEFAULT NULL,
    `update_time`     datetime(3) NULL     DEFAULT NULL,
    `update_username` varchar(64) NULL     DEFAULT NULL,
    `is_deleted`      datetime(3) NOT NULL DEFAULT '1970-01-01 00:00:00.000' COMMENT '逻辑删除：1970-01-01 00:00:00 表示启用，有值 表示删除',
    `tenant_id`       varchar(64) NULL     DEFAULT NULL COMMENT '企业id',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 3
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci COMMENT = '用户与角色关系表'
  ROW_FORMAT = DYNAMIC;

DROP TABLE IF EXISTS mes3.sys_tenant_info;
CREATE TABLE mes3.sys_tenant_info
(
    `id`              bigint       NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `tenant`          varchar(64)  NOT NULL COMMENT '租户id',
    `info`            json COMMENT '其他信息',
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
    comment '租户额外信息表';


DROP TABLE IF EXISTS mes3.sys_user_info;
CREATE TABLE mes3.sys_user_info
(
    `id`              bigint       NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `user_id`         bigint       NOT NULL COMMENT '用户id',
    `info`            json COMMENT '其他信息',
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
    comment '用户额外信息表';

DROP TABLE IF EXISTS mes3.sys_role_info;
CREATE TABLE mes3.sys_role_info
(
    `id`              bigint       NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `role_id`         bigint   NOT NULL COMMENT '角色id',
    `info`            json COMMENT '其他信息',
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
    comment '角色额外信息表';


-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` (id, user_id, role_id)
VALUES (1, 1, 1);
INSERT INTO `sys_user_role`(id, user_id, role_id)
VALUES (2, 2, 2);

SET FOREIGN_KEY_CHECKS = 1;