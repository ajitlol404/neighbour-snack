package com.neighbour_snack.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.neighbour_snack.dto.SmtpDTO;
import com.neighbour_snack.service.SmtpService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin/smtp")
@RequiredArgsConstructor
public class SmtpRestController {

    private final SmtpService smtpService;

    @GetMapping
    public SmtpDTO.SmtpResponseDTO getSmtp() {
        return smtpService.getSmtp();
    }

    @PutMapping
    public SmtpDTO.SmtpResponseDTO updateSmtp(@Valid @RequestBody SmtpDTO.SmtpRequestDTO request) {
        smtpService.testSmtpConfiguration(request);
        return smtpService.updateSmtp(request);
    }
}
