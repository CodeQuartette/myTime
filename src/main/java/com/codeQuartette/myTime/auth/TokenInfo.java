package com.codeQuartette.myTime.auth;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TokenInfo {

    private String grantType;
    private String refreshToken;
    private String accessToken;

    public static TokenInfo create(String grantType, String refreshToken, String accessToken) {
        return TokenInfo.builder()
                .grantType(grantType)
                .refreshToken(refreshToken)
                .accessToken(accessToken)
                .build();
    }
}
