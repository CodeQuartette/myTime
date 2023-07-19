package com.codeQuartette.myTime.controller;

import com.codeQuartette.myTime.controller.dto.UserDTO;
import com.codeQuartette.myTime.service.UserService;
import lombok.RequiredArgsConstructor;
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
    public UserDTO.Response getUser() {
        return userService.getUser();
    }

    @PatchMapping("/user")
    public UserDTO.Response updateUser(@RequestBody UserDTO.Request userDTO) {
        return userService.updateUser(userDTO);
    }
}
