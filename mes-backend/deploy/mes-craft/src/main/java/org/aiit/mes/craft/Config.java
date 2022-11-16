package org.aiit.mes.craft;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author heyu
 * @version 1.0.0
 * @ClassName Config
 * @Description
 * @createTime 2021.09.01 10:43
 */
@Configuration
@MapperScan("org.aiit.mes.craft.domain.dao.mapper")
public class Config {
}
