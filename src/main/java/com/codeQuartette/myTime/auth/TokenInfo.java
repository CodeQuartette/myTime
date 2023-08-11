package com.codeQuartette.myTime.auth;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TokenInfo {

    private String grantType;
    private String accessToken;
    private String refreshToken;

    public static TokenInfo create(String grantType, String accessToken, String refreshToken) {
        return TokenInfo.builder()
                .grantType(grantType)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
