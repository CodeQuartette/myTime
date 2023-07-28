package com.codeQuartette.myTime.controller;

import com.codeQuartette.myTime.controller.dto.UserDTO;
import com.codeQuartette.myTime.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/user")
    public UserDTO.Response getUser(Authentication authentication) {
        return userService.getUser(authentication);
    }

    @PatchMapping("/user")
    public UserDTO.Response updateUser(Authentication authentication, @RequestBody UserDTO.Request userDTO) {
        return userService.updateUser(authentication, userDTO);
    }

    @DeleteMapping("/user")
    public void deleteUser(Authentication authentication, @RequestBody UserDTO.Request userDTO) {
        userService.deleteUser(authentication, userDTO);
    }
}
