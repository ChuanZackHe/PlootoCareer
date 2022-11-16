package org.aiit.mes.factory.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lzj
 * @version 1.0.0
 * @ClassName FactoryPager.java
 * @Description 用来解析前端传进来的信息。分页版
 * @createTime 2021年08月19日 16:39:00
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FactoryResourcePager {

    @ApiModelProperty("当前页码")
    @JsonProperty("page_no")
    private int pageNo;

    @ApiModelProperty("页面大小")
    @JsonProperty("page_size")
    private int pageSize;

    @ApiModelProperty("资源代码")
    @JsonProperty("resource_code")
    private String resourceCode;
}
