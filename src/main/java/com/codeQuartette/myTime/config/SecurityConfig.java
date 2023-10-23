package com.codeQuartette.myTime.config;

import com.codeQuartette.myTime.auth.JwtProvider;
import com.codeQuartette.myTime.controller.HabitController;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class SecurityConfig {

    @Configuration
    @EnableWebSecurity
    @RequiredArgsConstructor
    public static class SecurityFilterConfig {

        private final JwtProvider jwtProvider;

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http
                    .httpBasic(HttpBasicConfigurer::disable) //Http basic Auth 기반으로 로그인 인증창이 뜨지 않게 설정
                    .csrf(AbstractHttpConfigurer::disable) // html tag를 통한 공격 (rest controller 사용 시 불필요)
                    .sessionManagement(sessionManagement ->
                            sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 토큰을 통한 인증을 하기 때문에 session은 불필요, stateless 설정
                    )
                    .authorizeHttpRequests(authorizeRequests ->
                            authorizeRequests
                                    .requestMatchers("/user", "/reissueToken", "/habit", "/habit/**").hasRole("USER")
                                    .anyRequest().permitAll()
                    )
                    .addFilterBefore(new JwtAuthenticationFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class);
            // 기본적으로 Spring security에서 인증 관련 요청을 처리하는 필터는 UsernamePasswordAuthenticationFilter
            // 기본 필터 대신 별도의 커스텀 필터(JwtAuthenticationFilter)를 사용
            return http.build();
        }
    }

    @Configuration
    static class EncryptionConfig {

        @Bean
        public BCryptPasswordEncoder bCryptPasswordEncoder() {
            return new BCryptPasswordEncoder();
        }
    }
}
