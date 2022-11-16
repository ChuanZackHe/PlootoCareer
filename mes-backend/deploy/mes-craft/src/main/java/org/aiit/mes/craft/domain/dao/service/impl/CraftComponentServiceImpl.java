package org.aiit.mes.craft.domain.dao.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.aiit.mes.craft.domain.dao.entity.CraftComponentEntity;
import org.aiit.mes.craft.domain.dao.mapper.ICraftComponentMapper;
import org.aiit.mes.craft.domain.dao.service.ICraftComponentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * @param
 * @Author heyu
 * @description
 * @return
 * @throws
 */
@Service
public class CraftComponentServiceImpl extends ServiceImpl<ICraftComponentMapper, CraftComponentEntity> implements
        ICraftComponentService {

    private static final Logger logger = LoggerFactory.getLogger(CraftComponentServiceImpl.class);

    @Override
    public boolean removeById(Serializable id) {
        boolean ret = super.removeById(id);
        logger.info("delete craft-component by id:{}, result:{}", id, ret);
        return ret;
    }


}
