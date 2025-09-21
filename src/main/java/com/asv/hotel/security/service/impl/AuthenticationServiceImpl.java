package com.asv.hotel.security.service.impl;

import com.asv.hotel.entities.User;
import com.asv.hotel.exceptions.MyAuthException;
import com.asv.hotel.security.domain.JWTAuthenticationResponse;
import com.asv.hotel.security.service.AuthenticationService;
import com.asv.hotel.security.service.TokenStorageService;
import com.asv.hotel.security.util.JWTUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;

    private final CustomUserDetailsServiceImpl userDetailsService;

    private final JWTUtils jwtUtils;

    private final TokenStorageService tokenStorageService;

    @Override
    public JWTAuthenticationResponse signIn(String nickName, String password) {
        //создаю токен аутентификации
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(nickName, password);

        //  аутентифицирую пользователя
        Authentication authentication = authenticationManager.authenticate(authToken);

        // устанавливаю аутентификацию в контекст (для текущего потока)
        SecurityContextHolder.getContext().setAuthentication(authentication);

        //  объект User из результата аутентификации
        User userDetails = (User) authentication.getPrincipal();

        // аксес и рефреш токены
        String accessToken = jwtUtils.generateAccessToken(userDetails);
        String refreshToken = jwtUtils.generateRefreshToken(userDetails);

        // ОБА токена в хранилище кладем (чтобы можно было их потом убрать)
        tokenStorageService.addToken(accessToken);
        tokenStorageService.addToken(refreshToken);

        // ответ клиенту
        return new JWTAuthenticationResponse(accessToken, refreshToken);
    }

    // обновление аксесс с помощью рефреш
    @Override
    public JWTAuthenticationResponse refreshAccessToken(String refreshToken) {
        try {
            //  действителен ли ревреш токен (подпись, срок, активность)
            // null, потому что мы еще не знаем пользователя
            if (!jwtUtils.isTokenValid(refreshToken, null)) {
                throw new MyAuthException("Invalid refresh token");
            }

            // получ имя пользователя (nickName) из рефреш токена
            String nickName = jwtUtils.extractUsername(refreshToken);

            // загрузка  данных пользователя из БД
            User userDetails = (User) userDetailsService.loadUserByUsername(nickName);

            // проверка что рефреш токен действительно принадлежит этому пользователю
            // (метод isTokenValid с userDetails сделает эту проверку)
            if (!jwtUtils.isTokenValid(refreshToken, userDetails)) {
                throw new MyAuthException("Refresh token does not belong to the user");
            }

            // генерация нового аксес токена
            String newAccessToken = jwtUtils.generateAccessToken(userDetails);

            // сохранение  в хранилище
            tokenStorageService.addToken(newAccessToken);

            // возвращаем новый токен
            return new JWTAuthenticationResponse(newAccessToken, refreshToken);

        } catch (MyAuthException e) {

            throw e;
        } catch (Exception e) {
            throw new MyAuthException("Failed to refresh access token: " + e.getMessage());
        }
    }
}
