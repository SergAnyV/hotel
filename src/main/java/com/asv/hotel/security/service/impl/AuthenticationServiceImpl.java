package com.asv.hotel.security.service.impl;

import com.asv.hotel.entities.User;
import com.asv.hotel.exceptions.MyAuthException;
import com.asv.hotel.security.domain.JWTAuthentication;
import com.asv.hotel.security.domain.RefreshRequest;
import com.asv.hotel.security.service.AuthenticationService;
import com.asv.hotel.security.service.TokenStorageService;
import com.asv.hotel.security.util.JWTUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;

    private final CustomUserDetailsServiceImpl userDetailsService;

    private final JWTUtils jwtUtils;

    private final TokenStorageService tokenStorageService;

    @Override
    public JWTAuthentication signIn(String logIn, String password) {
        //создаю токен аутентификации
        final UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(logIn, password);

        //  аутентифицирую пользователя
        final Authentication authentication = authenticationManager.authenticate(authToken);

        // устанавливаю аутентификацию в контекст (для текущего потока)
//        SecurityContextHolder.getContext().setAuthentication(authentication);

        //  объект User из результата аутентификации
        final User userDetails = (User) authentication.getPrincipal();


        // аксес и рефреш токены
        final String accessToken = jwtUtils.generateAccessToken(userDetails);
        final String refreshToken = jwtUtils.generateRefreshToken(userDetails);
        final Long id = userDetails.getId();
        // ОБА токена в хранилище кладем (чтобы можно было их потом убрать)
        tokenStorageService.removeAllUserTokens(id);
        tokenStorageService.addTokens(id, accessToken);
        // ответ клиенту
        return new JWTAuthentication(accessToken, refreshToken);
    }

    // обновление аксесс с помощью рефреш
    @Override
    public JWTAuthentication refreshAccessToken(String refreshToken) {
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
            Long id=userDetails.getId();
            // проверка что рефреш токен действительно принадлежит этому пользователю
            // (метод isTokenValid с userDetails сделает эту проверку)
            if (!jwtUtils.isTokenValid(refreshToken, userDetails)) {
                throw new MyAuthException("Refresh token does not belong to the user");
            }

            // генерация нового аксес токена
            String newAccessToken = jwtUtils.generateAccessToken(userDetails);

            // сохранение  в хранилище
            tokenStorageService.addTokens(id,newAccessToken);

            // возвращаем новый токен
            return new JWTAuthentication(newAccessToken, refreshToken);

        } catch (MyAuthException e) {

            throw e;
        } catch (Exception e) {
            throw new MyAuthException("Failed to refresh access token: " + e.getMessage());
        }
    }

    @Override
    public void removeTokensFromStorage(HttpServletRequest request) {
        // токен из заголовка
        String authHeader = request.getHeader("Authorization");
        String accessToken = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            accessToken = authHeader.substring(7);
        }
        tokenStorageService.removeToken(accessToken);
    }


}
