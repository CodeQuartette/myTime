package com.codeQuartette.myTime.config;

import com.codeQuartette.myTime.auth.JwtProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

    private static final String USER_ID = "userId";

    private final JwtProvider jwtProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String refreshToken = jwtProvider.resolveRefreshToken(httpRequest);
        String accessToken = jwtProvider.resolveAccessToken(httpRequest);

        if (httpRequest.getRequestURI().equals("/reissueToken")) {
            if(refreshToken != null && accessToken != null && jwtProvider.validateToken(refreshToken)) {
                Authentication authentication = jwtProvider.getAuthenticationByExpiredToken(accessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                httpRequest.setAttribute(USER_ID, jwtProvider.getUserIdByExpiredToken(accessToken));
            }
        } else {
            if (accessToken != null && jwtProvider.validateToken(accessToken)) {
                Authentication authentication = jwtProvider.getAuthentication(accessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                httpRequest.setAttribute(USER_ID, jwtProvider.getUserId(accessToken));
            }
        }

        chain.doFilter(request, response);
    }
}
