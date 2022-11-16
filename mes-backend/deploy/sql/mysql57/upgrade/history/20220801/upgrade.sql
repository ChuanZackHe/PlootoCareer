use mes3;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- 拓展信息元数据表修改
ALTER TABLE expand_meta ADD COLUMN `value_constraint` json NULL COMMENT '值约束';