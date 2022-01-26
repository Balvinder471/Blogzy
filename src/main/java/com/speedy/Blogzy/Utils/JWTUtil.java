package com.speedy.Blogzy.Utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import javax.validation.constraints.Null;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class JWTUtil {
    public static String generateToken(UserDetails userDetails) {
        SecretKey key = Keys.hmacShaKeyFor(JWTConstants.JWT_SECRET.getBytes(StandardCharsets.UTF_8));
        return Jwts.builder().setIssuer("Blogzy").setIssuedAt(new Date()).setSubject("JWT Token")
                .claim("username", userDetails.getUsername())
                .claim("authorities", populateAuthorities(userDetails.getAuthorities()))
                .setExpiration(new Date(new Date().getTime() + JWTConstants.JWT_EXPIRATION_IN_MS))
                .signWith(key).compact();
    }

    public static String generateToken(Authentication authentication) {
        SecretKey key = Keys.hmacShaKeyFor(JWTConstants.JWT_SECRET.getBytes(StandardCharsets.UTF_8));
        return Jwts.builder().setIssuer("Blogzy").setIssuedAt(new Date()).setSubject("JWT Token")
                .claim("username", authentication.getName())
                .claim("authorities", populateAuthorities(authentication.getAuthorities()))
                .setExpiration(new Date(new Date().getTime() + JWTConstants.JWT_EXPIRATION_IN_MS))
                .signWith(key).compact();
    }
    private static String populateAuthorities(Collection<? extends GrantedAuthority> collection) {
        Set<String> authoritiesSet = new HashSet<>();
        for (GrantedAuthority authority : collection) {
            authoritiesSet.add(authority.getAuthority());
        }
        return String.join(",", authoritiesSet);
    }
}
