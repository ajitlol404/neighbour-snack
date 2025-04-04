package com.neighbour_snack.service.impl;

import java.util.Properties;

import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import com.neighbour_snack.constant.AppConstant;
import com.neighbour_snack.dto.SmtpDTO;
import com.neighbour_snack.dto.SmtpDTO.SmtpRequestDTO;
import com.neighbour_snack.dto.SmtpDTO.SmtpResponseDTO;
import com.neighbour_snack.exception.SmtpException;
import com.neighbour_snack.helper.AppUtil;
import com.neighbour_snack.service.SmtpService;
import com.neighbour_snack.service.SystemSettingService;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SmtpServiceImpl implements SmtpService {

    private final SystemSettingService systemSettingService;

    @Override
    public SmtpResponseDTO getSmtp() {
        String portValue = systemSettingService.getSystemSettingValueByKey(AppConstant.SMTP_PORT_KEY);
        Integer port = (portValue != null && !portValue.isEmpty()) ? Integer.parseInt(portValue) : null;
        return new SmtpDTO.SmtpResponseDTO(
                systemSettingService.getSystemSettingValueByKey(AppConstant.SMTP_HOST_KEY),
                port,
                Boolean.parseBoolean(systemSettingService.getSystemSettingValueByKey(AppConstant.SMTP_IS_SSL_KEY)),
                systemSettingService.getSystemSettingValueByKey(AppConstant.SMTP_USERNAME_KEY));
    }

    @Override
    public SmtpResponseDTO updateSmtp(SmtpRequestDTO request) {
        systemSettingService.updateSystemSettingValue(AppConstant.SMTP_HOST_KEY, request.host());
        systemSettingService.updateSystemSettingValue(AppConstant.SMTP_PORT_KEY, request.port().toString());
        systemSettingService.updateSystemSettingValue(AppConstant.SMTP_IS_SSL_KEY, request.isSsl().toString());
        systemSettingService.updateSystemSettingValue(AppConstant.SMTP_USERNAME_KEY, request.username());
        systemSettingService.updateSystemSettingValue(AppConstant.SMTP_PASSWORD_KEY,
                AppUtil.encode(request.password()));

        return new SmtpResponseDTO(request.host(), request.port(), request.isSsl(), request.username());
    }

    @Override
    public void testSmtpConfiguration(SmtpRequestDTO request) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(request.host());
        mailSender.setPort(request.port());
        mailSender.setUsername(request.username());
        mailSender.setPassword(request.password());

        Properties properties = mailSender.getJavaMailProperties();
        properties.put("mail.smtp.auth", Boolean.TRUE.toString());
        properties.put("mail.smtp.starttls.enable", !request.isSsl());
        properties.put("mail.smtp.ssl.enable", request.isSsl());

        try {
            mailSender.testConnection();
        } catch (MessagingException e) {
            throw new SmtpException("SMTP connection failed");
        }
    }

}
