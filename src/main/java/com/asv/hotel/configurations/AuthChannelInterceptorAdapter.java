package com.asv.hotel.configurations;

import com.asv.hotel.security.util.JWTUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthChannelInterceptorAdapter implements ChannelInterceptor {
    private final UserDetailsService userDetailsService;
    private final JWTUtils jwtUtils;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor == null) {
            return message;
        }

        StompCommand command = accessor.getCommand();

        if (StompCommand.CONNECT.equals(command)) {
            log.info("Попытка подключения к WebSocket");
            handleConnect(accessor);
        } else if (StompCommand.SUBSCRIBE.equals(command)) {
               String destination = accessor.getDestination();
            log.info("Попытка подписки на: {}", destination);
            handleSubscribe(accessor, destination);
        } else if (StompCommand.SEND.equals(command)) {
            String destination = accessor.getDestination();
            log.info("Отправка сообщения в: {}", destination);
            handleSend(accessor, destination);
        }

        return message;
    }
    private void handleConnect(StompHeaderAccessor accessor) {
        String token = getTokenFromHeader(accessor);

        if (!StringUtils.hasText(token)) {
            log.warn(" WebSocket подключение без токена");
            throw new RuntimeException("Требуется аутентификация");
        }

        String username = jwtUtils.extractUsername(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (!jwtUtils.isTokenValid(token,userDetails)) {
            log.warn("Невалидный JWT токен при подключении к WebSocket");
            throw new RuntimeException("Невалидный токен аутентификации");
        }

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        accessor.setUser(authentication);

        log.info(" аутентификация WebSocket прошла успешно для пользователя: {}", username);
    }


    private void handleSubscribe(StompHeaderAccessor accessor, String destination) {
        if (destination == null) return;


        if (destination.startsWith("/topic/booking/")) {
            // 🏨 ЧАТ БРОНИРОВАНИЯ - проверяем доступ
            checkBookingAccess(accessor, destination);
        } else if (destination.startsWith("/user/queue/")) {
            // 👤 ПЕРСОНАЛЬНЫЕ КАНАЛЫ - проверяем что пользователь подписывается на свои
            checkUserAccess(accessor, destination);
        }
        // Можно добавить проверки для других типов каналов
    }

    /**
     * 📤 ПРОВЕРКА ПРАВ ПРИ ОТПРАВКЕ СООБЩЕНИЙ
     */
    private void handleSend(StompHeaderAccessor accessor, String destination) {
        if (destination == null) return;

        // 💬 ПРОВЕРЯЕМ ЧАТ СООБЩЕНИЯ
        if (destination.startsWith("/app/chat.")) {
            checkChatPermissions(accessor);
        }
    }

    /**
     * 🏨 ПРОВЕРКА ДОСТУПА К ЧАТУ БРОНИРОВАНИЯ
     */
    private void checkBookingAccess(StompHeaderAccessor accessor, String destination) {
        try {
            // 🔢 ИЗВЛЕКАЕМ ID БРОНИРОВАНИЯ ИЗ ПУТИ: "/topic/booking/123"
            String[] pathParts = destination.split("/");
            Long bookingId = Long.parseLong(pathParts[3]); // 4-я часть пути

            // 👤 ПОЛУЧАЕМ ДАННЫЕ АУТЕНТИФИЦИРОВАННОГО ПОЛЬЗОВАТЕЛЯ
            UsernamePasswordAuthenticationToken auth =
                    (UsernamePasswordAuthenticationToken) accessor.getUser();

            if (auth == null) {
                throw new RuntimeException("Пользователь не аутентифицирован");
            }

            // 🔍 ПРОВЕРЯЕМ ЕСТЬ ЛИ ДОСТУП К БРОНИРОВАНИЮ
            if (!hasAccessToBooking(auth, bookingId)) {
                throw new RuntimeException("Нет доступа к чату бронирования: " + bookingId);
            }

            log.info("✅ Разрешена подписка на бронирование {} для {}", bookingId, auth.getName());

        } catch (NumberFormatException e) {
            throw new RuntimeException("Неверный ID бронирования: " + destination);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new RuntimeException("Неверный формат destination: " + destination);
        }
    }

    /**
     * 👤 ПРОВЕРКА ДОСТУПА К ПЕРСОНАЛЬНЫМ КАНАЛАМ
     */
    private void checkUserAccess(StompHeaderAccessor accessor, String destination) {
        UsernamePasswordAuthenticationToken auth =
                (UsernamePasswordAuthenticationToken) accessor.getUser();

        if (auth == null) {
            throw new RuntimeException("Пользователь не аутентифицирован");
        }

        // ✅ Spring автоматически проверяет что пользователь подписывается на свои каналы
        log.info("✅ Разрешена подписка на персональные каналы для: {}", auth.getName());
    }

    /**
     * 💬 ПРОВЕРКА ПРАВ НА ОТПРАВКУ СООБЩЕНИЙ
     */
    private void checkChatPermissions(StompHeaderAccessor accessor) {
        UsernamePasswordAuthenticationToken auth =
                (UsernamePasswordAuthenticationToken) accessor.getUser();

        if (auth == null) {
            throw new RuntimeException("Пользователь не аутентифицирован для отправки сообщений");
        }

        // 🔍 ПРОВЕРЯЕМ МОЖЕТ ЛИ ПОЛЬЗОВАТЕЛЬ ОТПРАВЛЯТЬ СООБЩЕНИЯ
        if (!isUserAllowedToChat(auth)) {
            throw new RuntimeException("Пользователь не имеет прав на отправку сообщений");
        }

        log.info("✅ Пользователь {} имеет права на отправку сообщений", auth.getName());
    }

    /**
     * 🎯 ПРОВЕРКА ДОСТУПА К КОНКРЕТНОМУ БРОНИРОВАНИЮ
     */
    private boolean hasAccessToBooking(UsernamePasswordAuthenticationToken auth, Long bookingId) {
        // 💡 В РЕАЛЬНОМ ПРИЛОЖЕНИИ ЗДЕСЬ ДОЛЖНА БЫТЬ ЛОГИКА:
        // 1. Найти пользователя в базе по auth.getName()
        // 2. Найти бронирование по bookingId
        // 3. Проверить:
        //    - Пользователь владелец бронирования?
        //    - Пользователь персонал отеля?
        //    - Пользователь менеджер?

        log.info("🔍 Проверка доступа пользователя {} к бронированию {}", auth.getName(), bookingId);

        // 🎯 ВРЕМЕННАЯ ЗАГЛУШКА - всегда разрешаем
        // В реальном приложении заменить на вызов сервиса проверки прав
        return true;
    }

    /**
     * ✅ ПРОВЕРКА МОЖЕТ ЛИ ПОЛЬЗОВАТЕЛЬ ОТПРАВЛЯТЬ СООБЩЕНИЯ
     */
    private boolean isUserAllowedToChat(UsernamePasswordAuthenticationToken auth) {
        // 💡 В РЕАЛЬНОМ ПРИЛОЖЕНИИ ПРОВЕРЯТЬ:
        // - Аккаунт подтвержден?
        // - Пользователь не заблокирован?
        // - Имеет нужную роль?

        return true; // Временная заглушка
    }

    /**
     * 🔑 ИЗВЛЕЧЕНИЕ JWT ТОКЕНА ИЗ ЗАГОЛОВКОВ
     */
    private String getTokenFromHeader(StompHeaderAccessor accessor) {
        // 📨 ИЩЕМ ЗАГОЛОВОК Authorization
        String authHeader = accessor.getFirstNativeHeader("Authorization");

        // ✂️ ИЗВЛЕКАЕМ ТОКЕН ИЗ "Bearer ваш_токен"
        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7); // Убираем "Bearer "
        }

        return null; // Токен не найден
    }
}
