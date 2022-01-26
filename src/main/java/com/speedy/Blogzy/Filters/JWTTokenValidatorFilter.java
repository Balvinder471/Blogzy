package com.speedy.Blogzy.Filters;

import com.speedy.Blogzy.Utils.JWTConstants;
import com.speedy.Blogzy.Utils.GetRequestConverter;
import com.speedy.Blogzy.Utils.JWTUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class JWTTokenValidatorFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String jwt = request.getHeader(JWTConstants.JWT_HEADER);
        if(null == jwt && null != request.getCookies()) {
            for (Cookie cookie : request.getCookies()) {
                jwt = cookie.getName().contentEquals("AUTH") ? cookie.getValue() : null;
            }
        }
        if (null != jwt) {
            try {
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
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
            catch (MalformedJwtException | SignatureException | ExpiredJwtException e) {
             if(null !=  request.getHeader(JWTConstants.JWT_HEADER)) {
                 request.getRequestDispatcher("/api/authenticate").forward(new GetRequestConverter(request), response);
                 System.out.println("Redirected!!!");
             }
            }
//            catch (ExpiredJwtException eje) {
//                System.out.println("Token Expired!!!");
//            }
//            catch (Exception e) {
//                throw new BadCredentialsException("Invalid Token received!");
//            }

        }
        else {
        if(request.getRequestURI().contains("/api/") && !request.getRequestURI().contentEquals("/api/authenticate") && (!request.getRequestURI().contentEquals("/api/authors") || !request.getMethod().contentEquals("POST")))
            request.getRequestDispatcher("/api/authenticate").forward(new GetRequestConverter(request), response);
        }
        filterChain.doFilter(request, response);
    }


    @Override protected boolean shouldNotFilter(HttpServletRequest request) {
        return request.getServletPath().equals("/api/authenticate");
    }
}
