package org.aiit.mes.factory;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author lzj
 * @version 1.0.0
 * @ClassName FactoryMgtConfig.java
 * @Description TODO
 * @createTime 2021年09月23日 10:05:00
 */
@Configuration
@MapperScan("org.aiit.mes.factory.domain.dao.mapper")
public class FactoryMgtConfig {
}
