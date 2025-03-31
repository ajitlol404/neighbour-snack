package com.neighbour_snack.runner;

import com.neighbour_snack.dto.SystemSettingDTO;
import com.neighbour_snack.entity.SystemSetting;
import com.neighbour_snack.service.SystemSettingService;
import com.neighbour_snack.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.neighbour_snack.constant.AppConstant.*;

@Order(1)
@Component
@RequiredArgsConstructor
public class ApplicationStartupRunner implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(ApplicationStartupRunner.class);

    private final UserService userService;
    private final SystemSettingService systemSettingService;


    private static final List<String> SMTP_SETTINGS_KEYS = List.of(
            SMTP_HOST_KEY,
            SMTP_PORT_KEY,
            SMTP_IS_SSL_KEY,
            SMTP_USERNAME_KEY,
            SMTP_PASSWORD_KEY,
            SMTP_STATUS_KEY
    );

    @Override
    public void run(String... args) throws Exception {
        createAdminUserIfNotExists();
        initializeSMTPSettings();
        initializeAppVersion();
    }

    private void createAdminUserIfNotExists() {
        if (userService.areThereAdminUser()) {
            logger.info("Admin user already exists.");
        } else {
            userService.createAdminUser();
            logger.info("Admin user created successfully.");
        }
    }

    private void initializeSMTPSettings() {
        SMTP_SETTINGS_KEYS.forEach(this::initializeSystemSetting);
    }

    private void initializeAppVersion() {
        initializeSystemSetting(APPLICATION_VERSION_KEY, APPLICATION_VERSION, SystemSetting.SystemSettingCategory.APP_VERSION);
    }

    private void initializeSystemSetting(String key) {
        initializeSystemSetting(key, null, SystemSetting.SystemSettingCategory.SMTP);
    }

    private void initializeSystemSetting(String key, String defaultValue, SystemSetting.SystemSettingCategory category) {
        try {
            systemSettingService.getSystemSettingByKey(key);
            logger.info("System setting '{}' already exists.", key);
        } catch (Exception e) {
            SystemSettingDTO newSetting = new SystemSettingDTO(key, defaultValue, category);
            systemSettingService.saveSystemSetting(newSetting);
            logger.info("System setting '{}' created with value: {}", key, defaultValue);
        }
    }
}
