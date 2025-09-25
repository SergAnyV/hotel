package com.asv.hotel.security.util;

import com.asv.hotel.security.domain.JWTSecrets;
import com.asv.hotel.security.service.TokenStorageService;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.jsonwebtoken.Claims;

import io.jsonwebtoken.Jwts;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.MacAlgorithm;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
@Slf4j
@RequiredArgsConstructor
@Component
public class JWTUtils {

    private final JWTSecrets jwtSecrets;
    private final TokenStorageService  tokenStorageService;
    //кэшируем ключи чтобы не создавать их каждый раз
    private SecretKey cachedAccessKey;
    private SecretKey cachedRefreshKey;

    private static final long EXPIRATION_TIME_FOR_REFRESH_TOKEN = 604_800_000;
    private static final long EXPIRATION_TIME_FOR_ACCESS_TOKEN = 1_800_000;
    private static final long TIME_FOR_CHECKING_TOKEN = 86_400_000;
    private static final MacAlgorithm SIGNATURE_ALGORITHM = Jwts.SIG.HS256;

    // генерирует аксес токен 30 минут живет потом заменить на ссылку в проект в пропертя сейчас 30 минут
    public String generateAccessToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails, EXPIRATION_TIME_FOR_ACCESS_TOKEN);
    }

    // получаем ключ для подписи аксес токена
    SecretKey getAccessSigningKey() {
        if (cachedAccessKey == null) {
            byte[] keyBytes = Decoders.BASE64.decode(jwtSecrets.getAccess());
            cachedAccessKey = Keys.hmacShaKeyFor(keyBytes);
        }
        return cachedAccessKey;
    }

    // получаем обновленный токен  врем так же исправитть сейчас 7 дней
    public String generateRefreshToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails, EXPIRATION_TIME_FOR_REFRESH_TOKEN);
    }

    // получаем ключ для подписи обнов токена
    SecretKey getRefreshSigningKey() {
        if (cachedRefreshKey == null) {
            byte[] keyBytes = Decoders.BASE64.decode(jwtSecrets.getRefresh());
            cachedRefreshKey = Keys.hmacShaKeyFor(keyBytes);
        }
        return cachedRefreshKey;
    }


    // ГЕНЕРИРУЮ ЗДЕСЬ ТОКЕН ВРОДЕ НОРМ
    private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails, long expirationTimeMs
    ) {
        // settSubject(userDetails.getUsername()) здесь userDetails.getUsername() опираюсь на
        // никнейм смотри класс ЮЗЕР никнейм уникален
        return Jwts
                .builder()
                .claims(extraClaims)
                .subject(
                        userDetails.getUsername()
                )
                .issuedAt(
                        new Date(System.currentTimeMillis())
                )
                // срок жизни токена
                .expiration(
                        new Date(System.currentTimeMillis() + expirationTimeMs)
                )
                //  ключ в зависимости от срока жизни токена
                .signWith(
                        getSigningKeyForType(expirationTimeMs)
                        , SIGNATURE_ALGORITHM
                )
                .compact();
    }

    // выбирает ключ аксес или рефреш  на основе срока жизни если больше значит рефреш,  меньше- аксес
    SecretKey getSigningKeyForType(long expirationTimeMs) {
        if (expirationTimeMs > TIME_FOR_CHECKING_TOKEN) {
            return getRefreshSigningKey();
        } else {
            return getAccessSigningKey();
        }
    }

    //  имя пользователя (никнейм так как он уникальный) из токена
    public String extractUsername(String token) {
        return extractClaim(token,
                claims -> claims.getSubject()
        );
    }

    //  любое утверждение  из токена
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // все утверждения из токена
    private Claims extractAllClaims(String token) {
        try {
            return Jwts
                    .parser()
                    .verifyWith(getSigningKeyForToken(token))
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            // Возвращаем утверждение из исключения для просроченных токенов
            return e.getClaims();
        }
    }

    // какой ключ  использовать для проверки токена сначала аксес потом рефреш пробую
    private SecretKey getSigningKeyForToken(String token)  {
        // Разбираем токен без проверки подписи, чтобы получить claims
        String[] parts = token.split("\\.");
        if (parts.length < 2) {
            throw new IllegalArgumentException("Invalid JWT token");
        }

        String payload = new String(Base64.getUrlDecoder().decode(parts[1]));
        JsonObject jsonPayload = JsonParser.parseString(payload).getAsJsonObject();

        long exp = jsonPayload.get("exp").getAsLong() * 1000;
        long iat = jsonPayload.get("iat").getAsLong() * 1000;
        long tokenLifetime = exp - iat;

        // Определяем тип ключа на основе времени жизни токена
        if (tokenLifetime > TIME_FOR_CHECKING_TOKEN) {
            return getRefreshSigningKey();
        } else {
            return getAccessSigningKey();
        }
    }

    //действителен ли токен проверка на  подпись + срок действия + наличие в хранилище
    public boolean isTokenValid(String token, UserDetails userDetails) {
        if (userDetails == null) {
            return false;
        }
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) &&
                !isTokenExpired(token) &&
                tokenStorageService.isTokenActive(token);
    }

    // истек ли срок действия токена
    boolean isTokenExpired(String token) {
        try {
            return extractExpiration(token).before(new Date());
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            return true; // Токен точно просрочен
        }
    }

    // Извлекает дату истечения токена
    private Date extractExpiration(String token) {
        try {
            return extractClaim(token, Claims::getExpiration);
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            // Если токен просрочен, возвращаем дату истечения из исключения
            return e.getClaims().getExpiration();
        }
    }
}
