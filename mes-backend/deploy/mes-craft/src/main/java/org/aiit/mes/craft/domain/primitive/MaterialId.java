package org.aiit.mes.craft.domain.primitive;

import org.aiit.mes.common.primitive.LongId;

/**
 * @author heyu
 * @version 1.0.0
 * @ClassName MaterialId
 * @Description
 * @createTime 2021.12.22 16:39
 */
public class MaterialId extends LongId {

    public MaterialId(Long id) {
        super(id);
    }

    @Override
    public String toString() {
        return String.valueOf(getId());
    }
}
