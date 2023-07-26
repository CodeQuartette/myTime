package com.codeQuartette.myTime.service.impl;

import com.codeQuartette.myTime.controller.dto.UserDTO;
import com.codeQuartette.myTime.domain.User;
import com.codeQuartette.myTime.exception.DuplicateNicknameException;
import com.codeQuartette.myTime.exception.DuplicateUserException;
import com.codeQuartette.myTime.exception.PasswordNotMatchException;
import com.codeQuartette.myTime.exception.UserNotFoundException;
import com.codeQuartette.myTime.repository.UserRepository;
import com.codeQuartette.myTime.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public void signup(UserDTO.Request userDTO) {
        if (verifyUser(userDTO.getEmail())) {
            throw new DuplicateUserException();
        }

        doubleCheckNickname(userDTO);

        // 패스워드 암호화
        userRepository.save(User.create(userDTO));
    }

    @Override
    public UserDTO.Response login(UserDTO.Request userDTO) {
        User user = findUserByEmail(userDTO.getEmail());
        checkPassword(user, userDTO);

        // 토큰을 만드는 로직 구현
        String token = "new token";
        user.updateToken(token);
        userRepository.save(user);
        return UserDTO.Response.of(user);
    }

    @Override
    public UserDTO.Response getUser() {
        // id와 토큰 비교 확인
        // 토큰의 id를 통해 유저 조회
        User user = findUserById(1L);
        return UserDTO.Response.of(user);
    }

    @Override
    public UserDTO.Response updateUser(UserDTO.Request userDTO) {
        // id와 토큰 비교 확인
        // 토큰의 id를 통해 유저 조회
        User user = findUserById(1L);
        doubleCheckNickname(userDTO);
        checkPassword(user, userDTO);
        user.updateInfo(userDTO);
        userRepository.save(user);
        return UserDTO.Response.of(user);
    }

    @Override
    public void deleteUser(UserDTO.Request userDTO) {
        // id와 토큰 비교 확인
        // 토큰의 id를 통해 유저 조회
        User user = findUserById(1L);
        checkPassword(user, userDTO);
        userRepository.delete(user);
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
    }

    private boolean verifyUser(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    private User findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    private void doubleCheckNickname(UserDTO.Request userDTO) {
        if (userDTO.nicknameNonNull() && verifyNickname(userDTO.getNickname())) {
            throw new DuplicateNicknameException();
        }
    }

    private boolean verifyNickname(String nickname) {
        return userRepository.findByNickname(nickname).isPresent();
    }

    private void checkPassword(User user, UserDTO.Request userDTO) {
        if (!user.matchPassword(userDTO.getPassword())) {
            throw new PasswordNotMatchException();
        }
    }

    @Override
    public User findUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new RuntimeException("해당 아이디가 없습니다"));
    }

    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("조회하는 유저가 존재하지 않습니다."));
    }

}
