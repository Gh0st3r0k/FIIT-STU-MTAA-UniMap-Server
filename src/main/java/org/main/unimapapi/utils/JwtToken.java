package org.main.unimapapi.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.main.unimapapi.configs.AppConfig;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class JwtToken {
    private final long EXPIRATION_TIME_ACCESS = AppConfig.getEXPIRATION_TIME_ACCESS();
    private final long EXPIRATION_TIME_REFRESH = AppConfig.getEXPIRATION_TIME_REFRESH();

    private Key accessSigningKey;
    private Key refreshSigningKey;

    @PostConstruct
    public void init() {
        accessSigningKey = Keys.hmacShaKeyFor(AppConfig.getAccessKey().getBytes());
        refreshSigningKey = Keys.hmacShaKeyFor(AppConfig.getRefreshKey().getBytes());
    }


    public String generateAccessToken(String login) {
        return Jwts.builder()
                .setSubject(login)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_ACCESS))
                .signWith(accessSigningKey, SignatureAlgorithm.HS256)
                .compact();
    }


    public String generateRefreshToken(String login) {
        return Jwts.builder()
                .setSubject(login)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_REFRESH))
                .signWith(refreshSigningKey, SignatureAlgorithm.HS256)
                .compact();
    }


    public String extractUsernameFromAccessToken(String token) {
        try {
            return extractClaims(token, accessSigningKey).getSubject();
        } catch (JwtException e) {
            ServerLogger.logServer(ServerLogger.Level.WARNING, "Invalid access token: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid access token");
        }
    }


    public String extractUsernameFromRefreshToken(String token) {
        try {
            return extractClaims(token, refreshSigningKey).getSubject();
        } catch (JwtException e) {
            ServerLogger.logServer(ServerLogger.Level.WARNING, "Invalid refresh token: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token");
        }
    }

    private Claims extractClaims(String token, Key key) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


    public boolean validateAccessToken(String token, String username) {
        try {
            Claims claims = extractClaims(token, accessSigningKey);
            return claims.getSubject().equals(username) &&
                    !claims.getExpiration().before(new Date());
        } catch (JwtException e) {
            ServerLogger.logServer(ServerLogger.Level.WARNING, "Error validating access token for user " + username + ": " + e.getMessage());
            return false;
        }
    }


    public boolean validateAccessToken(String token) {
        try {
            String username = extractUsernameFromAccessToken(token);
            Claims claims = extractClaims(token, accessSigningKey);
            return claims.getSubject().equals(username) &&
                    !claims.getExpiration().before(new Date());
        } catch (JwtException e) {
            ServerLogger.logServer(ServerLogger.Level.WARNING, "Error validating access token: " + e.getMessage());
            return false;
        }
    }


    public boolean validateRefreshToken(String token, String username) {
        try {
            Claims claims = extractClaims(token, refreshSigningKey);
            return claims.getSubject().equals(username) &&
                    !claims.getExpiration().before(new Date());
        } catch (JwtException e) {
            ServerLogger.logServer(ServerLogger.Level.WARNING, "Error validating refresh token for user " + username + ": " + e.getMessage());
            return false;
        }
    }


    public Date extractExpiration(String token) {
        return extractClaims(token, refreshSigningKey).getExpiration();
    }
}