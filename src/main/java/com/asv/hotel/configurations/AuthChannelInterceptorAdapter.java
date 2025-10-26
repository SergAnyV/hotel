package com.asv.hotel.configurations;

import com.asv.hotel.entities.User;
import com.asv.hotel.exceptions.HotelAuthenticationException;
import com.asv.hotel.exceptions.HotelIncorrectInputData;
import com.asv.hotel.security.util.JWTUtils;
import com.asv.hotel.services.BookingService;
import com.asv.hotel.services.UserInternalService;
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
    private final UserInternalService userInternalService;
    private final BookingService bookingService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor == null) {
            return message;
        }

        StompCommand command = accessor.getCommand();

        if (StompCommand.CONNECT.equals(command)) {
            handleConnect(accessor);
        } else if (StompCommand.SUBSCRIBE.equals(command)) {
            String destination = accessor.getDestination();
            handleSubscribe(accessor, destination);
        } else if (StompCommand.SEND.equals(command)) {
            String destination = accessor.getDestination();
            handleSend(accessor, destination);
        }

        return message;
    }

    private void handleConnect(StompHeaderAccessor accessor) {
        String token = getTokenFromHeader(accessor);

        if (!StringUtils.hasText(token)) {
            log.warn(" WebSocket подключение без токена");
            throw new HotelAuthenticationException("Требуется аутентификация");
        }

        String username = jwtUtils.extractUsername(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (!jwtUtils.isTokenValid(token, userDetails)) {
            log.warn("Невалидный JWT токен при подключении к WebSocket");
            throw new HotelAuthenticationException("Невалидный токен аутентификации");
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
            checkBookingAccess(accessor, destination);
        } else if (destination.startsWith("/user/queue/")) {
            checkUserAccess(accessor, destination);
        }
    }


    private void handleSend(StompHeaderAccessor accessor, String destination) {
        if (destination == null) return;

        if (destination.startsWith("/app/chat.")) {
            checkChatPermissions(accessor);
        }
    }


    private void checkBookingAccess(StompHeaderAccessor accessor, String destination) {
        try {
            String[] pathParts = destination.split("/");
            Long bookingId = Long.parseLong(pathParts[3]);

            UsernamePasswordAuthenticationToken auth =
                    (UsernamePasswordAuthenticationToken) accessor.getUser();

            if (auth == null) {
                throw new HotelAuthenticationException("Пользователь не аутентифицирован");
            }

            if (!hasAccessToBooking(auth, bookingId)) {
                throw new HotelAuthenticationException("Нет доступа к чату бронирования: " + bookingId);
            }

            log.info("Разрешена подписка на бронирование {} для {}", bookingId, auth.getName());

        } catch (NumberFormatException e) {
            throw new HotelIncorrectInputData("Неверный ID бронирования: " + destination);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new HotelIncorrectInputData("Неверный формат destination: " + destination);
        }
    }


    private void checkUserAccess(StompHeaderAccessor accessor, String destination) {
        UsernamePasswordAuthenticationToken auth =
                (UsernamePasswordAuthenticationToken) accessor.getUser();

        if (auth == null) {
            throw new HotelAuthenticationException("Пользователь не аутентифицирован");
        }

        log.info("Разрешена подписка на персональные каналы для: {}", auth.getName());
    }


    private void checkChatPermissions(StompHeaderAccessor accessor) {
        UsernamePasswordAuthenticationToken auth =
                (UsernamePasswordAuthenticationToken) accessor.getUser();

        if (auth == null) {
            throw new HotelAuthenticationException("Пользователь не аутентифицирован для отправки сообщений");
        }


        if (!isUserAllowedToChat(auth)) {
            throw new HotelAuthenticationException("Пользователь не имеет прав на отправку сообщений");
        }

        log.info("Пользователь {} имеет права на отправку сообщений", auth.getName());
    }


    private boolean hasAccessToBooking(UsernamePasswordAuthenticationToken auth, Long bookingId) {
        String nickName = auth.getName();
        User bookingOwnerUser = bookingService.findUserOwnerOfBookingByIdOrNull(bookingId);
        if (bookingOwnerUser.getNickName().equals(nickName)) {
            return Boolean.TRUE;
        }
        log.info("Проверка доступа пользователя {} к бронированию {}", auth.getName(), bookingId);
        return Boolean.FALSE;

    }


    private boolean isUserAllowedToChat(UsernamePasswordAuthenticationToken auth) {
        User user = userInternalService.findUserByNickName(auth.getName());

        if (Boolean.TRUE.equals(user.getVerifyStatus())) {
            return Boolean.TRUE;
        }

        return Boolean.FALSE;
    }


    private String getTokenFromHeader(StompHeaderAccessor accessor) {
        String authHeader = accessor.getFirstNativeHeader(JWTUtils.AUTHORIZATION);

        if (StringUtils.hasText(authHeader) && authHeader.startsWith(JWTUtils.BEARER)) {
            return authHeader.substring(7);
        }

        return null;
    }
}
