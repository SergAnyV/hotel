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
                .role("ROLE_МЕНЕДЖЕР").implies("ROLE_АДМИНИСТРАТОР")

                .role("ROLE_АДМИНИСТРАТОР").implies("ROLE_ПОВАР")
                .role("ROLE_АДМИНИСТРАТОР").implies("ROLE_УБОРЩИК")
                .role("ROLE_АДМИНИСТРАТОР").implies("ROLE_РАБОТНИК КУХНИ")

                .role("ROLE_ПОВАР").implies("ROLE_КЛИЕНТ")
                .role("ROLE_УБОРЩИК").implies("ROLE_КЛИЕНТ")
                .role("ROLE_РАБОТНИК КУХНИ").implies("ROLE_КЛИЕНТ")
                .build();
    }
}
