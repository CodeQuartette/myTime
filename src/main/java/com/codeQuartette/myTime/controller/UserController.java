package com.codeQuartette.myTime.controller;

import com.codeQuartette.myTime.controller.dto.UserDTO;
import com.codeQuartette.myTime.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
}
