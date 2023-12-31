package com.codeQuartette.myTime.controller.dto;

import com.codeQuartette.myTime.auth.TokenInfo;
import com.codeQuartette.myTime.domain.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDate;


public class UserDTO {

    @Getter
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class Request {

        private String name;
        private String nickname;
        private String email;
        private LocalDate birthday;
        private String password;
        private String newPassword;
        private String profileImage;
        private Boolean gender;

        public boolean nicknameNonNull() {
            return this.nickname != null;
        }
    }

    @Getter
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Response {

        private Long id;
        private String name;
        private String nickname;
        private String email;
        private LocalDate birthday;
        private String profileImage;
        private Boolean gender;
        private TokenInfo tokenInfo;

        public static UserDTO.Response of(User user) {
            return Response.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .nickname(user.getNickname())
                    .email(user.getEmail())
                    .birthday(user.getBirthday())
                    .profileImage(user.getProfileImage())
                    .gender(user.getGender())
                    .build();
        }

        public void setTokenInfo(TokenInfo tokenInfo) {
            this.tokenInfo = tokenInfo;
        }
    }
}
