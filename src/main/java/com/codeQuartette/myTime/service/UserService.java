package com.codeQuartette.myTime.service;

import com.codeQuartette.myTime.domain.User;

import com.codeQuartette.myTime.controller.dto.UserDTO;

public interface UserService {
    void testUser();

    User findById(Long userId);

    void signup(UserDTO.Request userInfoRequestDTO);

    UserDTO.Response login(UserDTO.Request userDTO);

    UserDTO.Response getUser();

    UserDTO.Response updateUser(UserDTO.Request userDTO);

    void deleteUser(UserDTO.Request userDTO);

    User findUser(Long userId);
}
