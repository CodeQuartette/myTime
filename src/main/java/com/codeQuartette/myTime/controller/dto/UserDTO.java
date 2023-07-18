package com.codeQuartette.myTime.controller.dto;

import com.codeQuartette.myTime.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;


public class UserDTO {

    @Getter
    public static class Request {

        private String name;
        private String nickname;
        private String email;
        private LocalDate birthday;
        private String password;
        private String profileImage;
        private boolean gender;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Response {

        private Long id;
        private String name;
        private String nickname;
        private String email;
        private LocalDate birthday;
        private String profileImage;
        private boolean gender;
        private String token;

        public static UserDTO.Response of(User user) {
            return Response.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .nickname(user.getNickname())
                    .email(user.getEmail())
                    .birthday(user.getBirthday())
                    .profileImage(user.getProfileImage())
                    .gender(user.getGender())
                    .token(user.getToken())
                    .build();
        }
    }
}
