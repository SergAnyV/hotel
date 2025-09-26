package com.asv.hotel.security.service.impl;

import com.asv.hotel.security.util.JWTUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JWTUtils jwtUtils; // Утилита для работы с JWT (проверка, извлечение данных)

    private final CustomUserDetailsServiceImpl userDetailsService; // Сервис для загрузки пользователя из БД

    private static final String STRING_BEARER="Bearer ";
    private static final String STRING_AUTHORIZATION="Authorization";
    private static final int NUMBER_FOR_CUTTING_TOKEN=7;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        //  ШАГ 1: Извлекаем токен из заголовка Authorization
        final String authHeader = request.getHeader(STRING_AUTHORIZATION);
        final String jwt;
        final String nickName;

        // заголовка нет или он начинается  не с "Bearer ", пропускаем фильтр
        if (authHeader == null || !authHeader.startsWith(STRING_BEARER)) {
            filterChain.doFilter(request, response); // Передаем запрос дальше по цепочке
            return; // Выходим из метода, так как токена нет
        }

        // Извлекаем сам токен (убираем "Bearer ")
        jwt = authHeader.substring(NUMBER_FOR_CUTTING_TOKEN); // "Bearer " — это 7 символов

        // ШАГ 2: Извлекаем имя пользователя (nickName) из токена
        nickName = jwtUtils.extractUsername(jwt); // пользую утилиту

        // ШАГ 3: Проверяем, есть ли уже аутентификация в SecurityContext
        // Если аутентификация уже установлена (например, другой фильтр уже обработал запрос),
        // и имя пользователя совпадает — ничего не делаем.
        if (nickName != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // ШАГ 4: Загружаем данные пользователя из БД
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(nickName);

            // ШАГ 5: Проверяем валидность токена
            // Проверяем: подпись верна, срок не истек, токен не отозван (находится в TokenStorageService)
            if (jwtUtils.isTokenValid(jwt, userDetails)) {

                // ШАГ 6: Создаем объект аутентификации
                // UsernamePasswordAuthenticationToken — это стандартный класс Spring Security.
                // Хотя он называется "Password", для JWT пароль не нужен, поэтому передаем null.
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, // Принципал (Principal) — объект User
                        null,        // Учетные данные (Credentials) — для JWT это null
                        userDetails.getAuthorities() // Список полномочий (ролей)
                );

                // ШАГ 7: Устанавливаем детали запроса
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // ШАГ 8: Устанавливаем аутентификацию в SecurityContext
                // Это самая важная строка. После этого Spring Security "знает", кто делает запрос.
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // ШАГ 9: Передаем запрос дальше по цепочке фильтров
        // Даже если токен не валиден или отсутствует, мы все равно передаем запрос дальше.
        // Это позволяет контроллерам обрабатывать запросы, которые не требуют аутентификации (permitAll),
        // или возвращать ошибку 401/403, если аутентификация нужна, но токен неверен.
        filterChain.doFilter(request, response);
    }
}
