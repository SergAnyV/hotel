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
                .role("ROLE_менеджер").implies("ROLE_АДМИНИСТРАТОР")

                .role("ROLE_администратор").implies("ROLE_ПОВАР")
                .role("ROLE_администратор").implies("ROLE_УБОРЩИК")
                .role("ROLE_администратор").implies("ROLE_РАБОТНИК КУХНИ")

                .role("ROLE_повар").implies("ROLE_КЛИЕНТ")
                .role("ROLE_уборщик").implies("ROLE_КЛИЕНТ")
                .role("ROLE_работник кухни").implies("ROLE_КЛИЕНТ")
                .build();
    }
}
