package com.neighbour_snack.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.neighbour_snack.dto.SignUpDTO;

import jakarta.validation.Valid;

@RestController
public class AuthRestController {

    @PostMapping("/signup")
    public String createUser(@Valid @RequestBody SignUpDTO signUpDTO) {
        System.out.println(signUpDTO);
        return "";
    }

}
