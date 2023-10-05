package com.backend.ecommerce.service;

import com.backend.ecommerce.model.LocalUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.web.util.WebUtils;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Service
public class JWTService {

    @Value("${jwt.access.cookieName}")
    private String jwtCookie;
    @Value("${jwt.refresh.cookieName}")
    private String jwtRefreshCookie;
    @Value("${jwt.secret.key}")
    private String SECRET_KEY;
    @Value("${jwt.expiration}")
    private Long JWT_EXPIRATION_DATE;
    @Value("${jwt.refresh-token.expiration}")
    private Long JWT_REFRESH_EXPIRATION_DATE;




    public ResponseCookie generateJwtCookie(LocalUser user){
        String jwt = generateToken(user);
        return generateCookie(jwtCookie, jwt, "/");
    }

    public ResponseCookie generateJwtRefreshCookie(String refreshToken){
        return generateCookie(jwtRefreshCookie, refreshToken, "/");
    }

    public String getJwtFromCookie(HttpServletRequest request){
        return getCookieValueByName(request, jwtCookie);
    }

    public String getJwtRefreshFromCookie(HttpServletRequest request){
        return getCookieValueByName(request, jwtRefreshCookie);
    }

    public ResponseCookie getCleanJwtCookie(){
        return ResponseCookie.from(jwtCookie, null).path("/").build();
    }

    public ResponseCookie getCleanRefreshCookie(){
        return ResponseCookie.from(jwtRefreshCookie, null).path("/").build();
    }

    private String getCookieValueByName(HttpServletRequest request, String name) {
        Cookie cookie = WebUtils.getCookie(request, name);
        if(cookie != null){
            return cookie.getValue();
        }
        return null;
    }


    private ResponseCookie generateCookie(String name, String value, String path) {
        return ResponseCookie
                .from(name, value)
                .path(path)
                .maxAge(JWT_REFRESH_EXPIRATION_DATE / 1000)
                .httpOnly(true)
                .secure(true)
                .build();
    }

    public String generateToken(LocalUser user){
        return Jwts
                .builder()
                .setIssuer("Ecommerce App")
                .setSubject(user.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION_DATE))
                .signWith(getSingInKey(), SignatureAlgorithm.HS256).compact();
    }

    public String extractUserEmail(String token) {
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(getSingInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public boolean validateToken(String token){
        try{
            Jwts
                    .parserBuilder()
                    .setSigningKey(getSingInKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return true;
        }catch (Exception e){
            return false;
        }
    }


    private Key getSingInKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }


}
