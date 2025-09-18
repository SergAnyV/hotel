package com.asv.hotel.security.util;

import com.asv.hotel.security.domain.JWTSecrets;
import com.asv.hotel.security.service.TokenStorageService;
import io.jsonwebtoken.Claims;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.MacAlgorithm;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@RequiredArgsConstructor
@Component
public class JWTUtils {

    private final JWTSecrets jwtSecrets;
    private final TokenStorageService<String> tokenStorageService;
    private static final long EXPIRATION_TIME_FOR_REFRESH_TOKEN = 604_800_000;
    private static final long EXPIRATION_TIME_FOR_ACCESS_TOKEN = 1_800_000;
    private static final long TIME_FOR_CHECKING_TOKEN = 86_400_000;
    private static final MacAlgorithm SIGNATURE_ALGORITHM = Jwts.SIG.HS256;

    // генерирует аксес токен 30 минут живет потом заменить на ссылку в проект в пропертя сейчас 30 минут
    public String generateAccessToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails, EXPIRATION_TIME_FOR_ACCESS_TOKEN);
    }

    // получаем ключ для подписи аксес токена токена
    private SecretKey getAccessSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecrets.getAccessSecret());
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // получаем обновленный токен  врем так же исправитть сейчас 7 дней
    public String generateRefreshToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails, EXPIRATION_TIME_FOR_REFRESH_TOKEN);
    }

    // получаем ключ для подписи обнов токена
    private SecretKey getRefreshSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecrets.getRefreshSecret());
        return Keys.hmacShaKeyFor(keyBytes);
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
    private SecretKey getSigningKeyForType(long expirationTimeMs) {
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
        return Jwts
                .parser()
                .verifyWith(getSigningKeyForToken(token))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // какой ключ  использовать для проверки токена сначала аксес потом рефреш пробую
    private SecretKey getSigningKeyForToken(String token) {
        try {
            Jwts.parser().verifyWith(getAccessSigningKey()).build().parseSignedClaims(token);
            return getAccessSigningKey();
        } catch (Exception e) {
            return getRefreshSigningKey();
        }
    }

    //действителен ли токен проверка на  подпись + срок действия + наличие в хранилище
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) &&
                !isTokenExpired(token) &&
                tokenStorageService.isTokenActive(token);
    }

    // истек ли срок действия токена
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Извлекает дату истечения токена
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
