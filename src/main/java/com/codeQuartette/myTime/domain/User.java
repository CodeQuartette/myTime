package com.codeQuartette.myTime.domain;

import com.codeQuartette.myTime.controller.dto.UserDTO;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String nickname;

    private String password;

    @Column(unique = true)
    private String email;

    private String token;

    private LocalDate birthday;

    private String profileImage;

    private Boolean gender;

    public static User create(UserDTO.Request userDTO) {
        return User.builder()
                .name(userDTO.getName())
                .nickname(userDTO.getNickname())
                .email(userDTO.getEmail())
                .password(userDTO.getPassword())
                .birthday(userDTO.getBirthday())
                .profileImage(userDTO.getProfileImage())
                .gender(userDTO.getGender())
                .build();
    }

    public void updateIngo(UserDTO.Request userDTO) {
        this.name = userDTO.getName() == null ? this.name : userDTO.getName();
        this.nickname = userDTO.getNickname() == null ? this.nickname : userDTO.getNickname();
        this.password = userDTO.getNewPassword() == null ? this.password : userDTO.getNewPassword();
        this.birthday = userDTO.getBirthday() == null ? this.birthday : userDTO.getBirthday();
        this.profileImage = userDTO.getProfileImage() == null ? this.profileImage : userDTO.getProfileImage();
        this.gender = userDTO.getGender() == null ? this.gender : userDTO.getGender();
    }

    public void updateToken(String token) {
        this.token = token;
    }

    public boolean matchPassword(String password) {
        return this.password.equals(password);
    }
}


