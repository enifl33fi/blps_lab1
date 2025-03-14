package com.enifl33fi.lab1.api.service;

import com.enifl33fi.lab1.api.model.user.Role;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Log4j2
public class JwtService {
    private static final String ROLES_CLAIM_KEY = "role";

    @Value("${jwt.secret.access}")
    private String accessSecretKey;

    @Value("${jwt.secret.refresh}")
    private String refreshSecretKey;

    @Value("${jwt.expiration.access}")
    private int expirationAccessTime;

    @Value("${jwt.expiration.refresh}")
    private int expirationRefreshDays;

    public String extractAccessEmail(String token) {
        return extractAccessClaim(token, Claims::getSubject);
    }

    public Date extractRefreshExpirationDate(String token) {
        return extractRefreshClaim(token, Claims::getExpiration);
    }

    public Role extractRole(String token) {
        String role = extractAccessClaim(token, claims -> claims.get(ROLES_CLAIM_KEY, String.class));
        return Role.valueOf(role);
    }

    public String generateAccessToken(UserDetails userDetails) {
        Set<String> roles =
                userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toSet());
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put(ROLES_CLAIM_KEY, roles);

        Date issuedDate = new Date(System.currentTimeMillis());
        Date expirationDate =
                new Date(System.currentTimeMillis() + Duration.ofMinutes(expirationAccessTime).toMillis());

        return generateToken(userDetails, accessSecretKey, issuedDate, expirationDate, extraClaims);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        Date issuedDate = new Date(System.currentTimeMillis());
        Date expirationDate =
                new Date(System.currentTimeMillis() + Duration.ofDays(expirationRefreshDays).toMillis());

        return generateToken(
                userDetails, refreshSecretKey, issuedDate, expirationDate, new HashMap<>());
    }

    private String generateToken(
            UserDetails userDetails,
            String secretKey,
            Date issuedDate,
            Date expirationDate,
            Map<String, Object> extraClaims) {
        return Jwts.builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(issuedDate)
                .expiration(expirationDate)
                .signWith(getSignInKey(secretKey), Jwts.SIG.HS256)
                .compact();
    }

    public boolean validateAccessToken(String accessToken) {
        return validateToken(accessToken, accessSecretKey);
    }

    public boolean validateRefreshToken(String refreshToken) {
        return validateToken(refreshToken, refreshSecretKey);
    }

    private boolean validateToken(String token, String secretKey) {
        try {
            Jwts.parser().verifyWith(getSignInKey(secretKey)).build().parseSignedClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.error("Token expired", e);
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported jwt", e);
        } catch (MalformedJwtException e) {
            log.error("Malformed jwt", e);
        } catch (SignatureException e) {
            log.error("Invalid signature", e);
        } catch (Exception e) {
            log.error("invalid token", e);
        }
        return false;
    }

    private <T> T extractAccessClaim(String accessToken, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(accessToken, accessSecretKey);
        return claimsResolver.apply(claims);
    }

    private <T> T extractRefreshClaim(String refreshToken, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(refreshToken, refreshSecretKey);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token, String secretKey) {
        return Jwts.parser()
                .verifyWith(getSignInKey(secretKey))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSignInKey(String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
