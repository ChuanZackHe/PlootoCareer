use mes3;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

alter table wms_stock_detail
    add column `description` varchar(255) NULL COMMENT '描述';
