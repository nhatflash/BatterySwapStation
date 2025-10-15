package com.swd392.BatterySwapStation.infrastructure.security;

import com.swd392.BatterySwapStation.infrastructure.security.jwt.JwtUtil;
import com.swd392.BatterySwapStation.infrastructure.service.RedisSessionService;
import com.swd392.BatterySwapStation.infrastructure.security.session.SessionData;
import com.swd392.BatterySwapStation.infrastructure.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    private final CustomUserDetailsService userDetailsService;

    private final RedisSessionService redisSessionService;

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    public JwtAuthenticationFilter(JwtUtil jwtUtil, CustomUserDetailsService userDetailsService,  RedisSessionService redisSessionService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.redisSessionService = redisSessionService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            logger.info("Incoming request: {}", request.getRequestURI());
            String jwtToken = getTokenFromRequest(request);

            if (jwtToken != null && jwtUtil.validateToken(jwtToken)) {
                String email = jwtUtil.getEmailFromToken(jwtToken);

                SessionData sessionData = redisSessionService.getSession(jwtToken);
                if (sessionData != null && sessionData.getExpiresAt() > System.currentTimeMillis()) {
                    UserDetails userDetails =  userDetailsService.loadUserByUsername(email);
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication: ", e);
        }
        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
