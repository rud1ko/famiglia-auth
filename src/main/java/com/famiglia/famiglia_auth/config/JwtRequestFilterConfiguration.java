package com.famiglia.famiglia_auth.config;

import com.famiglia.famiglia_auth.service.UserService;
import com.famiglia.famiglia_auth.utils.JwtCore;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class JwtRequestFilterConfiguration extends OncePerRequestFilter {
    private final JwtCore jwtCore;
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = null;
        String username = null;
        UserDetails userDetails = null;
        UsernamePasswordAuthenticationToken auth = null;

        try {
            String headerAuth = request.getHeader("Authorization");
            if (headerAuth != null && headerAuth.startsWith("Bearer ")){
                jwt = headerAuth.substring(7);
            }

            if (jwt != null) {
                try {
                    username = jwtCore.getNameFromJwt(jwt);
                } catch (ExpiredJwtException e) {
                    log.debug("Время жизни токена вышло");
                } catch (SignatureException e){
                    log.debug("Подпись неправильная");
                }

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    userDetails = userService.loadUserByUsername(username);
                    auth = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            jwtCore.getRoles(jwt).stream().map(
                                    SimpleGrantedAuthority::new
                            ).collect(Collectors.toList())
                    );
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            };
        } catch (Exception e) {
            log.error("Операция с токеном закончилась неудачей");
        }

        filterChain.doFilter(request, response);
    }
}
