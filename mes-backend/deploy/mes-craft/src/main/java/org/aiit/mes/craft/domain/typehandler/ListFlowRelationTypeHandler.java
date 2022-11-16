package org.aiit.mes.craft.domain.typehandler;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import org.aiit.mes.craft.domain.vo.FlowNodeRelationV;

/**
 * @author heyu
 * @version 1.0.0
 * @ClassName handler
 * @Description
 * @createTime 2021.08.26 15:59
 */
public class ListFlowRelationTypeHandler extends JacksonTypeHandler {

    private final Class<? extends Object> type;

    public ListFlowRelationTypeHandler(Class<?> type) {
        super((Class<Object>) type);
        this.type = type;
    }

    @Override
    protected Object parse(String json) {
        return JSON.parseArray(json, FlowNodeRelationV.class);
    }
}
