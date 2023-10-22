package com.codeQuartette.myTime.controller;

import com.codeQuartette.myTime.annotation.UserId;
import com.codeQuartette.myTime.auth.TokenInfo;
import com.codeQuartette.myTime.controller.dto.UserDTO;
import com.codeQuartette.myTime.controller.globalResponse.ResponseDTO;
import com.codeQuartette.myTime.controller.globalResponse.ResponseType;
import com.codeQuartette.myTime.domain.User;
import com.codeQuartette.myTime.service.UserService;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import static com.codeQuartette.myTime.auth.JwtProvider.REFRESH_TOKEN;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseDTO<?> signup(@RequestBody UserDTO.Request userDTO) {
        userService.signup(userDTO);
        return ResponseDTO.from(ResponseType.CREATED);
    }

    @PostMapping("/api/login")
    public ResponseDTO<UserDTO.Response> login(@RequestBody UserDTO.Request userDTO) {
        UserDTO.Response response = userService.login(userDTO);
        return ResponseDTO.from(ResponseType.SUCCESS, response);
    }

    @GetMapping("/api/logout")
    public ResponseDTO<?> logout(@UserId Long userId) {
        userService.logout(userId);
        return ResponseDTO.from(ResponseType.SUCCESS);
    }

    @GetMapping("/reissueToken")
    public ResponseDTO<TokenInfo> reissueToken(ServletRequest request, Authentication authentication) {
        String bearerToken = ((HttpServletRequest) request).getHeader(REFRESH_TOKEN);
        String refreshToken = bearerToken.substring(7);
        TokenInfo response = userService.reissueToken(refreshToken, authentication);
        return ResponseDTO.from(ResponseType.SUCCESS, response);
    }

    @GetMapping("/user")
    public ResponseDTO<UserDTO.Response> getUser(@UserId Long userId) {
        User user = userService.getUser(userId);
        UserDTO.Response response = UserDTO.Response.of(user);
        return ResponseDTO.from(ResponseType.SUCCESS, response);
    }

    @PatchMapping("/user")
    public ResponseDTO<UserDTO.Response> updateUser(@UserId Long userId, @RequestBody UserDTO.Request userDTO) {
        User user = userService.updateUser(userId, userDTO);
        UserDTO.Response response = UserDTO.Response.of(user);
        return ResponseDTO.from(ResponseType.CREATED, response);
    }

    @DeleteMapping("/user")
    public ResponseDTO<?> deleteUser(@UserId Long userId, @RequestBody UserDTO.Request userDTO) {
        userService.deleteUser(userId, userDTO);
        return ResponseDTO.from(ResponseType.SUCCESS);
    }
}
