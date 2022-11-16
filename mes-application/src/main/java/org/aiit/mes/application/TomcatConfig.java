package org.aiit.mes.application;

import org.apache.catalina.connector.Connector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author heyu
 * @version 1.0.0
 * @ClassName HttConfiguration
 * @Description
 * @createTime 2022.03.29 17:49
 */
@Configuration
public class TomcatConfig {

    @Value("${server.http.port}")
    private int httpPort;

    @Bean
    public TomcatServletWebServerFactory ServletWebServerFactoryservletContainer() {
        TomcatServletWebServerFactory tomcatServletWebServerFactory = new TomcatServletWebServerFactory();
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setPort(httpPort);
        tomcatServletWebServerFactory.addAdditionalTomcatConnectors(connector);
        return tomcatServletWebServerFactory;
    }
}
