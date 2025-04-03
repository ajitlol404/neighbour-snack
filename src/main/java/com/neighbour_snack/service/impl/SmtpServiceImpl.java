package com.neighbour_snack.service.impl;

import org.springframework.stereotype.Service;

import com.neighbour_snack.service.SmtpService;
import com.neighbour_snack.service.SystemSettingService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SmtpServiceImpl implements SmtpService {

    private final SystemSettingService systemSettingService;

}
