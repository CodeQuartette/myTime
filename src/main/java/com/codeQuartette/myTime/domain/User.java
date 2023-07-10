package com.codeQuartette.myTime.domain;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String nickName;

    private String password;

    @Column(unique = true)
    private String email;

    private String token;

    private LocalDate birthday;

    private String profileImage;

    private Boolean gender;
}
