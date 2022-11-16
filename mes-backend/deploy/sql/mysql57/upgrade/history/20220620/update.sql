use mes3;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

alter table wms_stock_detail change column
    `batch_number` `batch_number`  varchar(64)  NULL  DEFAULT 'default' COMMENT '批次号';