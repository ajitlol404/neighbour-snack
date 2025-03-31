package com.neighbour_snack.controller;

import static com.neighbour_snack.constant.AppConstant.SIGN_UP_BASE_URL;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.neighbour_snack.dto.SignUpDTO;

import jakarta.validation.Valid;

@RestController
public class AuthRestController {

    @PostMapping(SIGN_UP_BASE_URL)
    public String createUser(@Valid @RequestBody SignUpDTO signUpDTO) {
        System.out.println(signUpDTO);
        return "";
    }

}
