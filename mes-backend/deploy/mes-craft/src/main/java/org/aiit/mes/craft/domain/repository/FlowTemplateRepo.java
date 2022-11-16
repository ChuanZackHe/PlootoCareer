package org.aiit.mes.craft.domain.repository;

import org.aiit.mes.craft.domain.dao.service.ICraftTemplateService;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * @author heyu
 * @version 1.0.0
 * @ClassName FlowComponentRepo
 * @Description
 * @createTime 2021.09.08 10:41
 */
@Repository
public class FlowTemplateRepo {

    @Resource
    private ICraftTemplateService craftTemplateService;

}
