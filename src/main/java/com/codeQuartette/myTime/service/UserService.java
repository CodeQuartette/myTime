package com.codeQuartette.myTime.service;

import com.codeQuartette.myTime.controller.dto.UserDTO;

public interface UserService {

    void signup(UserDTO.Request userInfoRequestDTO);

    UserDTO.Response login(UserDTO.Request userDTO);

    UserDTO.Response getUser();

    UserDTO.Response updateUser(UserDTO.Request userDTO);
}
