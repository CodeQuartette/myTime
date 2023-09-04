package com.codeQuartette.myTime.service.impl;

import com.codeQuartette.myTime.auth.TokenInfo;
import com.codeQuartette.myTime.controller.dto.UserDTO;
import com.codeQuartette.myTime.domain.User;
import com.codeQuartette.myTime.repository.UserRepository;
import com.codeQuartette.myTime.service.UserService;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.InjectSoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static com.codeQuartette.myTime.auth.JwtProvider.BEARER;

@ExtendWith(SoftAssertionsExtension.class)
@SpringBootTest
class UserServiceImplTest {

    @Autowired
    private UserService userServiceImpl;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder bCryptPasswordEncoder;

    @InjectSoftAssertions
    private SoftAssertions softly;

    @Test
    @Transactional
    @DisplayName("회원가입 서비스 로직 테스트, 전달 받은 유저 정보대로 DB에 저장 되어야 한다.")
    void signup() {
        UserDTO.Request userDTO = UserDTO.Request.builder()
                .name("testUser")
                .nickname("testUserNickname")
                .email("testUser@gmail.com")
                .birthday(LocalDate.of(2000, 1,11))
                .password("1234")
                .profileImage("http://testUserProfileImage.jpg")
                .gender(false)
                .build();
        userServiceImpl.signup(userDTO);
        User user = userRepository.findByEmail(userDTO.getEmail()).get();

        softly.assertThat(user.getName()).isEqualTo(userDTO.getName());
        softly.assertThat(user.getNickname()).isEqualTo(userDTO.getNickname());
        softly.assertThat(user.getEmail()).isEqualTo(userDTO.getEmail());
        softly.assertThat(user.getBirthday()).isEqualTo(userDTO.getBirthday());
        softly.assertThat(bCryptPasswordEncoder.matches(userDTO.getPassword(), user.getPassword())).isTrue();
        softly.assertThat(user.getProfileImage()).isEqualTo(userDTO.getProfileImage());
        softly.assertThat(user.getGender()).isEqualTo(userDTO.getGender());
    }

    @Test
    @Transactional
    @DisplayName("로그인 서비스 로직 테스트, 토큰을 생성하여 DB에 저장해야 한다.")
    void login() {
        UserDTO.Request userDTO = UserDTO.Request.builder()
                .email("enolj76@gmail.com")
                .password("1234")
                .build();
        UserDTO.Response responseUserDTO = userServiceImpl.login(userDTO);
        User user = userRepository.findByEmail(userDTO.getEmail()).get();

        softly.assertThat(user.getToken()).isEqualTo(responseUserDTO.getTokenInfo().getRefreshToken());
    }

    @Test
    @Transactional
    @DisplayName("로그아웃 서비스 로직 테스트, 토큰의 해당하는 유저의 토큰을 DB에서 삭제해야 한다.")
    void logout() {
        UserDTO.Request userDTO = UserDTO.Request.builder()
                .email("enolj76@gmail.com")
                .password("1234")
                .build();
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDTO.getEmail(), userDTO.getPassword());
        userServiceImpl.logout(authentication);
        User user = userRepository.findByEmail(userDTO.getEmail()).get();

        softly.assertThat(user.getToken()).isNull();
    }

    @Test
    @Transactional
    @DisplayName("토큰 재발급 서비스 로직 테스트, refreshToken을 확인하여 accessToken을 재발급 해야 한다.")
    void reissueToken() {
        String refreshToken = "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJjb2RlUXVhcnRldHRlIn0.ysfQimdEO_LZwRgZEEPDI0dxQKlvnIXSWQgpZHnJqRg";
        UserDTO.Request userDTO = UserDTO.Request.builder()
                .email("enolj76@gmail.com")
                .password("1234")
                .build();
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDTO.getEmail(), userDTO.getPassword());
        TokenInfo tokenInfo = userServiceImpl.reissueToken(refreshToken, authentication);

        softly.assertThat(tokenInfo.getGrantType()).isEqualTo(BEARER);
        softly.assertThat(tokenInfo.getRefreshToken()).isEqualTo(refreshToken);
        softly.assertThat(tokenInfo.getAccessToken()).isNotNull();
    }

    @Test
    @Transactional
    @DisplayName("유저정보 조회 서비스 로직 테스트, 로그인 된 유저에 해당하는 정보를 조회해야 한다.")
    void getUser() {
        UserDTO.Request userDTO = UserDTO.Request.builder()
                .email("enolj76@gmail.com")
                .password("1234")
                .build();
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDTO.getEmail(), userDTO.getPassword());
        User user = userServiceImpl.getUser(authentication);
        User targetUser = userRepository.findByEmail(authentication.getName()).get();

        softly.assertThat(user.getName()).isEqualTo(targetUser.getName());
        softly.assertThat(user.getNickname()).isEqualTo(targetUser.getNickname());
        softly.assertThat(user.getEmail()).isEqualTo(targetUser.getEmail());
        softly.assertThat(user.getBirthday()).isEqualTo(targetUser.getBirthday());
        softly.assertThat(user.getProfileImage()).isEqualTo(targetUser.getProfileImage());
        softly.assertThat(user.getGender()).isEqualTo(targetUser.getGender());
    }

    @Test
    @Transactional
    @DisplayName("회정정보 수정 서비스 로직 테스트, 요청에 따라 해당 유저에 대한 정보를 변경하여 DB에 저장해야 한다.")
    void updateUser() {
        UserDTO.Request userDTO = UserDTO.Request.builder()
                .email("enolj76@gmail.com")
                .password("1234")
                .newPassword("5678")
                .nickname("eno")
                .name("정호인")
                .build();
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDTO.getEmail(), userDTO.getPassword());
        userServiceImpl.updateUser(authentication, userDTO);
        User user = userRepository.findByEmail(authentication.getName()).get();

        softly.assertThat(user.getName()).isEqualTo(userDTO.getName());
        softly.assertThat(user.getNickname()).isEqualTo(userDTO.getNickname());
        softly.assertThat(bCryptPasswordEncoder.matches(userDTO.getNewPassword(), user.getPassword())).isTrue();
    }

    @Test
    @Transactional
    @DisplayName("유저 삭제 서비스 로직 테스트, 해당 유저를 DB에서 삭제해야 한다.")
    void deleteUser() {
        UserDTO.Request userDTO = UserDTO.Request.builder()
                .email("enolj76@gmail.com")
                .password("1234")
                .build();
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDTO.getEmail(), userDTO.getPassword());
        userServiceImpl.deleteUser(authentication, userDTO);
        User user = userRepository.findByEmail(authentication.getName()).orElse(null);

        softly.assertThat(user).isNull();
    }
}
