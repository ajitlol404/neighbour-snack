package com.neighbour_snack.service.impl;

import static com.neighbour_snack.entity.Role.ROLE_ADMIN;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.neighbour_snack.dao.UserRepository;
import com.neighbour_snack.entity.User;
import com.neighbour_snack.service.UserService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public boolean areThereAdminUser() {
        return userRepository.existsByRole(ROLE_ADMIN);
    }

    @Override
    @Transactional
    public void createAdminUser() {
        if (areThereAdminUser()) {
            logger.info("Admin user already exists, skipping creation.");
            return;
        }

        User adminUser = User.builder()
                .name("admin")
                .email("admin@mail.com")
                .password(passwordEncoder.encode("Abc@1234"))
                .userData(User.UserData.builder()
                        .secretKey(UUID.randomUUID())
                        .secretKeyStatus(true)
                        .build())
                .enabled(true)
                .image(null)
                .role(ROLE_ADMIN)
                .phoneNumber("9876543210")
                .address("Sector - 9/B, Street - 15, Q.No. - 1075, BKSC JH")
                .pincode("827009")
                .build();

        userRepository.save(adminUser);

        logger.info("Admin user has been created.");

    }
}
