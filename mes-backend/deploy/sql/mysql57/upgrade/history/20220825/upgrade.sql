use mes3;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;


-- 配料主表进行增加
ALTER TABLE ingredient_sheet_summary ADD COLUMN `ingredient_material` json NULL COMMENT '配料的物料';

-- 配料主表进行增加
ALTER TABLE base_material ADD COLUMN `stock_aggregate` tinyint default 0 COMMENT '库存是否聚合,0 不聚合作为明细，1聚合，通过默认批次号进行聚合';