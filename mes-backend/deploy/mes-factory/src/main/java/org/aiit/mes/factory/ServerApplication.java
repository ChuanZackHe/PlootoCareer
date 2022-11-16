package org.aiit.mes.factory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author heyu
 */
@EnableAspectJAutoProxy
@SpringBootApplication
@ComponentScan("org.aiit.mes")
@EnableTransactionManagement(proxyTargetClass = true)
public class ServerApplication {

    public static void main(String[] args) {
        ApplicationContext app = SpringApplication.run(ServerApplication.class, args);
    }
}

