package com.neighbour_snack.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.neighbour_snack.dto.AuthDTO.SignUpDTO;
import com.neighbour_snack.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthRestController {

    private final UserService userService;

    @PostMapping("/signup")
    public String createUser(@Valid @RequestBody SignUpDTO signUpDTO) {
        System.out.println(signUpDTO);
        return "";
    }

}
