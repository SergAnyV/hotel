package com.asv.hotel.security.configuration;


import com.asv.hotel.security.service.impl.JwtFilter;
import com.asv.hotel.services.UserInternalService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtFilter jwtFilter;
    private final UserInternalService userService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.httpBasic(basic -> basic.disable())
                .csrf(csrf ->
                        csrf.disable()).
                cors(cors -> cors.disable())
                .sessionManagement(session -> session
                        .sessionCreationPolicy((SessionCreationPolicy.STATELESS)))
                .authorizeHttpRequests(auth ->
                        auth
                                //доступ для всех
                                .requestMatchers("/api/v1/auth/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                                // доступ для всех зарегистрированных
//                                .requestMatchers("/api/v1/auth/**").permitAll() // Разрешить всем доступ к аутентификации
//                                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").authenticated()
                                .requestMatchers("/bookings").authenticated()
                                //доступ по ролям
                                .requestMatchers("/bookings/{number}", "/bookings/{id}").hasAnyAuthority("администратор", "менеджер")
                                .requestMatchers("/users").permitAll()
                                .requestMatchers("/users/by-**").hasAnyAuthority("администратор", "менеджер")

                                .requestMatchers(HttpMethod.GET, "/services").permitAll()
                                .requestMatchers(HttpMethod.GET, "/services/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/services").hasAuthority("MANAGER")
                                .requestMatchers(HttpMethod.DELETE, "/services/**").hasAuthority("MANAGER")
                                .anyRequest().authenticated())
                .addFilterAfter(jwtFilter, UsernamePasswordAuthenticationFilter.class).build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
