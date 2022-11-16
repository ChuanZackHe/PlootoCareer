package org.aiit.mes.factory.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aiit.mes.common.constant.DataStateEnum;

/**
 * @author heyu
 * @version 1.0.0
 * @ClassName FactoryResourceStatueChangeCmd
 * @Description
 * @createTime 2022.01.19 16:27
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FactoryResourceStatueChangeCmd {

    private String resourceCode;

    private String message;

    private DataStateEnum targetStatus;
}
