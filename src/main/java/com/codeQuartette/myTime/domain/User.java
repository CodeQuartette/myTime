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
                .gender(userDTO.isGender())
                .build();
    }

    public void updateToken(String token) {
        this.token = token;
    }
}


