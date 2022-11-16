package org.aiit.mes.craft.domain.vo;

import org.aiit.mes.base.material.domain.entity.BaseMaterialEntity;
import org.aiit.mes.common.constant.BaseMaterialType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author heyu
 * @version 1.0.0
 * @ClassName CraftBomTreeNodeRepresentVTest
 * @Description
 * @createTime 2022.01.17 16:21
 */
class CraftBomTreeNodeRepresentVTest {

    @Test
    void convertFrom() {
        BaseMaterialEntity materialEntity = BaseMaterialEntity.builder().type(BaseMaterialType.RAW).build();
        CraftBomTreeNodeRepresentV ret = CraftBomTreeNodeRepresentV.convertFrom(materialEntity, 100D, 200D);

        assertEquals(ret.getType(), materialEntity.getType());
        assertEquals(ret.getMaterialType(), materialEntity.getType().getDesc());
        assertEquals(100D, ret.getStockNum());
        assertEquals(200D, ret.getRequiredNum());
    }
}