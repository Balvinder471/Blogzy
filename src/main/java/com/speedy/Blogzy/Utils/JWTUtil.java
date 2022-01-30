package com.speedy.Blogzy.Utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import javax.servlet.http.Cookie;
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

    public static Authentication generateAuthFromJWT(String jwt) {
        SecretKey key = Keys.hmacShaKeyFor(
                JWTConstants.JWT_SECRET.getBytes(StandardCharsets.UTF_8));

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jwt)
                .getBody();
        String username = String.valueOf(claims.get("username"));
        String authorities = (String) claims.get("authorities");
        Authentication auth = new UsernamePasswordAuthenticationToken(username,null,
                AuthorityUtils.commaSeparatedStringToAuthorityList(authorities));
        return auth;
    }

    public static Cookie generateAuthCookie(UserDetails userDetails) {
        Cookie cookie = new Cookie("AUTH", generateToken(userDetails));
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(604800);
        return cookie;
    }

    private static String populateAuthorities(Collection<? extends GrantedAuthority> collection) {
        Set<String> authoritiesSet = new HashSet<>();
        for (GrantedAuthority authority : collection) {
            authoritiesSet.add(authority.getAuthority());
        }
        return String.join(",", authoritiesSet);
    }
}
