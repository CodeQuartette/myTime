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

    public static final String AUTHORIZATION = "Authorization";
    public static final String REFRESH_TOKEN = "Refresh_Token";
    public static final String BEARER = "Bearer";

    private final String ISSURE = "codeQuartette";
    private final String ROLES = "roles";
    private final int ACCESS_TOKEN_VALID_TIME = 30 * 60 * 1000; // 30분
    private final int REFRESH_TOKEN_VALID_TIME = 14 * 24 * 60 * 60 * 1000; // 14일
    private final Key SECRET_KEY;

    public JwtProvider(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.SECRET_KEY = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createRefreshToken() {
        return Jwts.builder()
                .setIssuer(ISSURE)
                .setExpiration(new Date(new Date().getTime() + REFRESH_TOKEN_VALID_TIME))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    public String createAccessToken(User user) {
        String roles = String.join(",", user.getRoles());

        return Jwts.builder()
                .setIssuer(ISSURE)
                .setId(user.getId().toString())
                .setSubject(user.getEmail())
                .claim(ROLES, roles)
                .setExpiration(new Date(new Date().getTime() + ACCESS_TOKEN_VALID_TIME))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    // 토큰에 저장되어 있는 정보를 통해 인증되지 않은 Authentication 객체를 생성
    // 실제 인증은 service 단에서 진행
    public Authentication getAuthentication(String token) {
        Claims claims = getUserClaimsByToken(token);
        return getAuthenticationByClaims(claims);
    }

    public Authentication getAuthenticationByExpiredToken(String token) {
        Claims claims;

        try {
            claims = getUserClaimsByToken(token);
        } catch (ExpiredJwtException e) {
            claims = e.getClaims();
        }

        return getAuthenticationByClaims(claims);
    }

    public Long getUserId(String token) {
        Claims claims = getUserClaimsByToken(token);
        return Long.parseLong(claims.getId());
    }

    public Long getUserIdByExpiredToken(String token) {
        Claims claims;

        try {
            claims = getUserClaimsByToken(token);
        } catch (ExpiredJwtException e) {
            claims = e.getClaims();
        }

        return Long.parseLong(claims.getId());
    }

    private Claims getUserClaimsByToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Authentication getAuthenticationByClaims(Claims claims) {
        if (claims.get(ROLES) == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }
        List<String> roles = Arrays.stream(claims.get(ROLES).toString().split(","))
                .collect(Collectors.toList());
        UserDetails userDetails = User.createDetails(claims.getSubject(), roles);
        // SecurityContext 가 Authentication 객체를 저장
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String resolveAccessToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER + " ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public String resolveRefreshToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(REFRESH_TOKEN);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER + " ")) {
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
            log.info("만료된 JWT입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT가 잘못되었습니다.");
        }
        return false;
    }
}
