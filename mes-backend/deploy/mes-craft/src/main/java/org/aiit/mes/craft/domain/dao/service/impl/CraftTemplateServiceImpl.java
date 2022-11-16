package org.aiit.mes.craft.domain.dao.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.aiit.mes.craft.domain.dao.entity.CraftTemplateEntity;
import org.aiit.mes.craft.domain.dao.mapper.ICraftTemplateMapper;
import org.aiit.mes.craft.domain.dao.service.ICraftTemplateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * @author heyu
 * @version 1.0.0
 * @ClassName BaseMaterialService
 * @Description
 * @createTime 2021.07.28 11:16
 */
@Service
public class CraftTemplateServiceImpl extends ServiceImpl<ICraftTemplateMapper, CraftTemplateEntity> implements
        ICraftTemplateService {

    private static final Logger logger = LoggerFactory.getLogger(CraftTemplateServiceImpl.class);

    @Override
    public boolean removeById(Serializable id) {
        boolean ret = super.removeById(id);
        logger.info("delete craft-template by id:{}, result:{}", id, ret);
        return ret;
    }


}
