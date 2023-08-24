package com.codeQuartette.myTime.domain;

import com.codeQuartette.myTime.controller.dto.UserDTO;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails { // spring security 자체적으로 UserDetails 구현체인 유저를 사용

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String nickname;

    private String password;

    @Column(unique = true)
    private String email;

    @Column(length = 1000)
    private String token;

    private LocalDate birthday;

    private String profileImage;

    private Boolean gender;

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>();

    public static User create(UserDTO.Request userDTO, PasswordEncoder passwordEncoder) {
        return User.builder()
                .name(userDTO.getName())
                .nickname(userDTO.getNickname())
                .email(userDTO.getEmail())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .birthday(userDTO.getBirthday())
                .profileImage(userDTO.getProfileImage())
                .gender(userDTO.getGender())
                .roles(Collections.singletonList("ROLE_USER"))
                .build();
    }

    public static UserDetails createDetails(String email, List<String> roles) {
        return User.builder()
                .email(email)
                .roles(roles)
                .build();
    }

    public void updateInfo(UserDTO.Request userDTO, PasswordEncoder passwordEncoder) {
        this.name = userDTO.getName() == null ? this.name : userDTO.getName();
        this.nickname = userDTO.getNickname() == null ? this.nickname : userDTO.getNickname();
        this.password = userDTO.getNewPassword() == null ? this.password : passwordEncoder.encode(userDTO.getNewPassword());
        this.birthday = userDTO.getBirthday() == null ? this.birthday : userDTO.getBirthday();
        this.profileImage = userDTO.getProfileImage() == null ? this.profileImage : userDTO.getProfileImage();
        this.gender = userDTO.getGender() == null ? this.gender : userDTO.getGender();
    }

    public void updateToken(String token) {
        this.token = token;
    }

    public boolean matchToken(String token) {
        return this.token.equals(token);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}


