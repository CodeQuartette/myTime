package com.codeQuartette.myTime.controller.dto;

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
}
