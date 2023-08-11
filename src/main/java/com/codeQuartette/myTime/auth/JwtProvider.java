package com.codeQuartette.myTime.auth;

import com.codeQuartette.myTime.domain.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtProvider {

    private final String ISSURE = "codeQuartette";
    private final String ROLES = "roles";
    private final int ACCESS_TOKEN_VALID_TIME = 30 * 60 * 1000; // 30분
    private final int REFRESH_TOKEN_VALID_TIME = 14 * 24 * 60 * 60 * 1000; // 14일
    private final Key SECRET_KEY;

    public JwtProvider(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.SECRET_KEY = Keys.hmacShaKeyFor(keyBytes);
    }

    public TokenInfo createToken(Authentication authentication) {
        String roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        String accessToken = Jwts.builder()
                .setIssuer(ISSURE)
                .setSubject(authentication.getName())
                .claim(ROLES, roles)
                .setExpiration(new Date(new Date().getTime() + ACCESS_TOKEN_VALID_TIME))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();

        String refreshToken = Jwts.builder()
                .setIssuer(ISSURE)
                .setExpiration(new Date(new Date().getTime() + REFRESH_TOKEN_VALID_TIME))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();

        return TokenInfo.create("Bearer", accessToken, refreshToken);
    }

    // 토큰에 저장되어 있는 정보를 통해 인증되지 않은 Authentication 객체를 생성
    // 실제 인증은 service 단에서 진행
    public Authentication getAuthentication(String token) {
        Claims claims = getUserClaims(token);
        if (claims.get(ROLES) == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }
        List<String> roles = Arrays.stream(claims.get(ROLES).toString().split(","))
                        .collect(Collectors.toList());
        UserDetails userDetails = User.createDetails(claims.getSubject(), roles);
        // SecurityContext 가 Authentication 객체를 저장
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public Claims getUserClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }
}
