package com.codeQuartette.myTime.service;

import com.codeQuartette.myTime.auth.TokenInfo;
import com.codeQuartette.myTime.domain.User;

import com.codeQuartette.myTime.controller.dto.UserDTO;
import org.springframework.security.core.Authentication;

public interface UserService {

    User findById(Long userId);

    void signup(UserDTO.Request userInfoRequestDTO);

    UserDTO.Response login(UserDTO.Request userDTO);

    void logout(Authentication authentication);

    TokenInfo reissueToken(String refreshToken, Authentication authentication);

    User getUser(Authentication authentication);

    User updateUser(Authentication authentication, UserDTO.Request userDTO);

    void deleteUser(Authentication authentication, UserDTO.Request userDTO);

    User findUser(Long userId);
}
