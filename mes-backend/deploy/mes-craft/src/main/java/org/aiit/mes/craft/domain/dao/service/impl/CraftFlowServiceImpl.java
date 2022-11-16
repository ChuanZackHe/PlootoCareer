package org.aiit.mes.craft.domain.dao.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.aiit.mes.craft.domain.dao.entity.CraftFlowEntity;
import org.aiit.mes.craft.domain.dao.mapper.ICraftFlowMapper;
import org.aiit.mes.craft.domain.dao.service.ICraftFlowService;
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
public class CraftFlowServiceImpl extends ServiceImpl<ICraftFlowMapper, CraftFlowEntity> implements ICraftFlowService {

    private static final Logger logger = LoggerFactory.getLogger(CraftFlowServiceImpl.class);

    @Override
    public boolean removeById(Serializable id) {
        boolean ret = super.removeById(id);
        logger.info("delete craft-flow by id:{}, result:{}", id, ret);
        return ret;
    }


}
