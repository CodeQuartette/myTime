package com.codeQuartette.myTime.controller;

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
    public ResponseDTO<?> logout(Authentication authentication) {
        userService.logout(authentication);
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
    public ResponseDTO<UserDTO.Response> getUser(Authentication authentication) {
        User user = userService.getUser(authentication);
        UserDTO.Response response = UserDTO.Response.of(user);
        return ResponseDTO.from(ResponseType.SUCCESS, response);
    }

    @PatchMapping("/user")
    public ResponseDTO<UserDTO.Response> updateUser(Authentication authentication, @RequestBody UserDTO.Request userDTO) {
        User user = userService.updateUser(authentication, userDTO);
        UserDTO.Response response = UserDTO.Response.of(user);
        return ResponseDTO.from(ResponseType.CREATED, response);
    }

    @DeleteMapping("/user")
    public ResponseDTO<?> deleteUser(Authentication authentication, @RequestBody UserDTO.Request userDTO) {
        userService.deleteUser(authentication, userDTO);
        return ResponseDTO.from(ResponseType.SUCCESS);
    }
}
