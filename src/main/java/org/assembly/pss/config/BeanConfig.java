package org.assembly.pss.config;

import org.assembly.pss.service.EventService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    public EventService eventService() {
        return new EventService();
    }
}
