package com.codeQuartette.myTime.service.impl;

import com.codeQuartette.myTime.controller.dto.UserDTO;
import com.codeQuartette.myTime.domain.User;
import com.codeQuartette.myTime.exception.DuplicateUserException;
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

    private boolean verifyUser(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
}
