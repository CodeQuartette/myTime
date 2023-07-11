package com.codeQuartette.myTime.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
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
}
