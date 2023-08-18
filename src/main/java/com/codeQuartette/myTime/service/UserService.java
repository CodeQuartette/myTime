package com.codeQuartette.myTime.service;

import com.codeQuartette.myTime.domain.User;

import com.codeQuartette.myTime.controller.dto.UserDTO;
import org.springframework.security.core.Authentication;

public interface UserService {

    void signup(UserDTO.Request userInfoRequestDTO);

    UserDTO.Response login(UserDTO.Request userDTO);

    UserDTO.Response getUser(Authentication authentication);

    UserDTO.Response updateUser(Authentication authentication, UserDTO.Request userDTO);

    void deleteUser(Authentication authentication, UserDTO.Request userDTO);

    User findUser(Long userId);
}
