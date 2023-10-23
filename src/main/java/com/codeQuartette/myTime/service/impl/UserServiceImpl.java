package com.codeQuartette.myTime.service.impl;

import com.codeQuartette.myTime.auth.JwtProvider;
import com.codeQuartette.myTime.auth.TokenInfo;
import com.codeQuartette.myTime.controller.dto.UserDTO;
import com.codeQuartette.myTime.domain.User;
import com.codeQuartette.myTime.exception.*;
import com.codeQuartette.myTime.repository.UserRepository;
import com.codeQuartette.myTime.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.codeQuartette.myTime.auth.JwtProvider.BEARER;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder bCryptPasswordEncoder;
    private final JwtProvider jwtProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @Override
    public void signup(UserDTO.Request userDTO) {
        if (verifyUser(userDTO.getEmail())) {
            throw new DuplicateUserException();
        }
        doubleCheckNickname(userDTO);
        userRepository.save(User.create(userDTO, bCryptPasswordEncoder));
    }

    @Override
    public UserDTO.Response login(UserDTO.Request userDTO) {
        Authentication authentication = getAuthentication(userDTO.getEmail(), userDTO.getPassword());
        User user = (User) authentication.getPrincipal();
        String accessToken = jwtProvider.createAccessToken(user);
        String refreshToken = jwtProvider.createRefreshToken();
        user.updateToken(refreshToken);
        userRepository.save(user);
        UserDTO.Response responseUserDTO = UserDTO.Response.of(user);
        TokenInfo tokenInfo = TokenInfo.create(BEARER, refreshToken, accessToken);
        responseUserDTO.setTokenInfo(tokenInfo);
        return responseUserDTO;
    }

    @Override
    public void logout(Long userId) {
        User user = findUser(userId);
        user.updateToken(null);
        userRepository.save(user);
    }

    @Override
    public TokenInfo reissueToken(Long userId, String refreshToken) {
        User user = findUser(userId);
        if (!user.matchToken(refreshToken)) {
            throw new TokenNotMatchException();
        }
        String accessToken = jwtProvider.createAccessToken(user);
        return TokenInfo.create(BEARER, user.getToken(), accessToken);
    }

    @Override
    public User getUser(Long userId) {
        return findUser(userId);
    }

    @Override
    public User updateUser(Long userId, UserDTO.Request userDTO) {
        User user = findUser(userId);
        getAuthentication(user.getEmail(), userDTO.getPassword());
        matchUser(userId, user);
        doubleCheckNickname(userDTO);
        user.updateInfo(userDTO, bCryptPasswordEncoder);
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long userId, UserDTO.Request userDTO) {
        User user = findUser(userId);
        getAuthentication(user.getEmail(), userDTO.getPassword());
        matchUser(userId, user);
        userRepository.delete(user);
    }

    // 이메일과 패스워드를 통해 인증 객체 생성
    // 인증 매니저의 authenticate 메소드를 통해 인증 진행
    // authenticate 메소드는 내부적으로 loadUserByUsername 메소드를 사용
    private Authentication getAuthentication(String email, String password) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);
        return authenticationManagerBuilder.getObject().authenticate(authenticationToken);
    }

    private void matchUser(Long userId, User user) {
        if (!user.matchUserId(userId)) {
            throw new UserNotMatchException();
        }
    }

    private void doubleCheckNickname(UserDTO.Request userDTO) {
        if (userDTO.nicknameNonNull() && verifyNickname(userDTO.getNickname())) {
            throw new DuplicateNicknameException();
        }
    }

    private boolean verifyNickname(String nickname) {
        return userRepository.findByNickname(nickname).isPresent();
    }

    private User findUser(String email) {
        return userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
    }

    private boolean verifyUser(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    @Override
    public User findUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
    }

    // loadUserByUsername 메소드는 내부적으로 name(email)을 통해 유저를 가져옴(이메일 검증)
    // 또한 내부적으로 authentication 객체와 UserDetails 객체의 PasswordEncoder를 통해 "인코딩" 된 비밀번호를 비교(비밀번호 검증)
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return findUser(email);
    }
}
