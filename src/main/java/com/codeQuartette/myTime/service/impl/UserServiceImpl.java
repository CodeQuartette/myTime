package com.codeQuartette.myTime.service.impl;

import com.codeQuartette.myTime.controller.dto.UserDTO;
import com.codeQuartette.myTime.domain.User;
import com.codeQuartette.myTime.exception.DuplicateUserException;
import com.codeQuartette.myTime.exception.UserNotFoundException;
import com.codeQuartette.myTime.repository.UserRepository;
import com.codeQuartette.myTime.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public void signup(UserDTO.Request userDTO) {
        if (verifyUser(userDTO.getEmail())) {
            throw new DuplicateUserException();
        }
        userRepository.save(User.create(userDTO));
    }

    @Override
    public UserDTO.Response login(UserDTO.Request userDTO) {
        User user = findUserByEmail(userDTO.getEmail());
        if (!user.getPassword().equals(userDTO.getPassword())) {
            throw new UserNotFoundException();
        }

        String token = "new token"; // 토큰을 만드는 로직 구현
        user.updateToken(token);
        userRepository.save(user);
        return UserDTO.Response.of(user);
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
    }

    private boolean verifyUser(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
}
