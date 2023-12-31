package com.codeQuartette.myTime.service;

import com.codeQuartette.myTime.auth.TokenInfo;
import com.codeQuartette.myTime.controller.dto.UserDTO;
import com.codeQuartette.myTime.domain.User;

public interface UserService {

    void signup(UserDTO.Request userInfoRequestDTO);

    UserDTO.Response login(UserDTO.Request userDTO);

    void logout(Long userId);

    TokenInfo reissueToken(Long userId, String refreshToken);

    User getUser(Long userId);

    User updateUser(Long userId, UserDTO.Request userDTO);

    void deleteUser(Long userId, UserDTO.Request userDTO);

    User findUser(Long userId);
}
