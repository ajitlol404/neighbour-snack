package com.neighbour_snack.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.neighbour_snack.service.SmtpService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin/smtp")
@RequiredArgsConstructor
public class SmtpRestController {

    private final SmtpService smtpService;

}
