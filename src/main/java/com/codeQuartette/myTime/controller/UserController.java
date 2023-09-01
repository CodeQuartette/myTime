package com.codeQuartette.myTime.controller;

import com.codeQuartette.myTime.auth.TokenInfo;
import com.codeQuartette.myTime.controller.dto.UserDTO;
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
    public void signup(@RequestBody UserDTO.Request userDTO) {
        userService.signup(userDTO);
    }

    @PostMapping("/login")
    public UserDTO.Response login(@RequestBody UserDTO.Request userDTO) {
        return userService.login(userDTO);
    }

    @GetMapping("/logout")
    public void logout(Authentication authentication) {
        userService.logout(authentication);
    }

    @GetMapping("/reissueToken")
    public TokenInfo reissueToken(ServletRequest request, Authentication authentication) {
        String bearerToken = ((HttpServletRequest) request).getHeader(REFRESH_TOKEN);
        String refreshToken = bearerToken.substring(7);
        return userService.reissueToken(refreshToken, authentication);
    }

    @GetMapping("/user")
    public UserDTO.Response getUser(Authentication authentication) {
        User user = userService.getUser(authentication);
        return UserDTO.Response.of(user);
    }

    @PatchMapping("/user")
    public UserDTO.Response updateUser(Authentication authentication, @RequestBody UserDTO.Request userDTO) {
        User user = userService.updateUser(authentication, userDTO);
        return UserDTO.Response.of(user);
    }

    @DeleteMapping("/user")
    public void deleteUser(Authentication authentication, @RequestBody UserDTO.Request userDTO) {
        userService.deleteUser(authentication, userDTO);
    }
}
