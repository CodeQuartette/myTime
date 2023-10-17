package com.codeQuartette.myTime.service.impl;

import com.codeQuartette.myTime.auth.JwtProvider;
import com.codeQuartette.myTime.auth.TokenInfo;
import com.codeQuartette.myTime.controller.dto.UserDTO;
import com.codeQuartette.myTime.domain.User;
import com.codeQuartette.myTime.exception.DuplicateNicknameException;
import com.codeQuartette.myTime.exception.DuplicateUserException;
import com.codeQuartette.myTime.exception.TokenNotMatchException;
import com.codeQuartette.myTime.exception.UserNotFoundException;
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
        String refreshToken = jwtProvider.createRefreshToken();
        String accessToken = jwtProvider.createAccessToken(authentication);
        User user = (User) authentication.getPrincipal();
        user.updateToken(refreshToken);
        userRepository.save(user);
        UserDTO.Response responseUserDTO = UserDTO.Response.of(user);
        TokenInfo tokenInfo = TokenInfo.create(BEARER, refreshToken, accessToken);
        responseUserDTO.setTokenInfo(tokenInfo);
        return responseUserDTO;
    }

    @Override
    public void logout(Authentication authentication) {
        User user = findUser(authentication.getName());
        user.updateToken(null);
        userRepository.save(user);
    }

    @Override
    public TokenInfo reissueToken(String refreshToken, Authentication authentication) {
        User user = findUser(authentication.getName());
        if (!user.matchToken(refreshToken)) {
            throw new TokenNotMatchException();
        }
        String accessToken = jwtProvider.createAccessToken(authentication);
        return TokenInfo.create(BEARER, user.getToken(), accessToken);
    }

    @Override
    public User getUser(Authentication authentication) {
        return findUser(authentication.getName());
    }

    @Override
    public User updateUser(Authentication authentication, UserDTO.Request userDTO) {
        Authentication targetUserAuthentication = getAuthentication(authentication.getName(), userDTO.getPassword());
        User user = (User) targetUserAuthentication.getPrincipal();
        doubleCheckNickname(userDTO);
        user.updateInfo(userDTO, bCryptPasswordEncoder);
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Authentication authentication, UserDTO.Request userDTO) {
        Authentication targetUserAuthentication = getAuthentication(authentication.getName(), userDTO.getPassword());
        User user = (User) targetUserAuthentication.getPrincipal();
        userRepository.delete(user);
    }

    // 이메일과 패스워드를 통해 인증 객체 생성
    // 인증 매니저의 authenticate 메소드를 통해 인증 진행
    // authenticate 메소드는 내부적으로 loadUserByUsername 메소드를 사용
    private Authentication getAuthentication(String email, String password) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);
        return authenticationManagerBuilder.getObject().authenticate(authenticationToken);
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
