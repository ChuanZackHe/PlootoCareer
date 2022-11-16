package org.aiit.mes.factory.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lzj
 * @version 1.0.0
 * @ClassName RelationPair.java
 * @Description 用来作为Relation变为Map的Key
 * @createTime 2021年08月31日 16:33:00
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FactoryRelationPair {

    private String preCode;

    private String afterCode;

}
