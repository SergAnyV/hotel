package com.asv.hotel.security.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
@Configuration
public class RoleHierarchyConfig {
    @Bean
    public RoleHierarchy roleHierarchy() {
        return RoleHierarchyImpl.withDefaultRolePrefix()
                .role("МЕНЕДЖЕР").implies("АДМИНИСТРАТОР")

                .role("АДМИНИСТРАТОР").implies("ПОВАР")
                .role("АДМИНИСТРАТОР").implies("УБОРЩИК")
                .role("АДМИНИСТРАТОР").implies("РАБОТНИК КУХНИ")

                .role("ПОВАР").implies("КЛИЕНТ")
                .role("УБОРЩИК").implies("КЛИЕНТ")
                .role("РАБОТНИК КУХНИ").implies("КЛИЕНТ")
                .build();
    }
}
