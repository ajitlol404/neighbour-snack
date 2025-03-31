package com.neighbour_snack.dto;

import static com.neighbour_snack.constant.AppConstant.PASSWORD_REGEX;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record SignUpDTO(
                @NotBlank(message = "Full name is required") @Size(min = 3, max = 50, message = "Full name must be between 3 and 50 characters") @Pattern(regexp = "^[a-zA-Z0-9]+( [a-zA-Z0-9]+)*$", message = "Full name can only contain letters, numbers, and a single space between words") String fullName,

                @NotBlank(message = "Email is required") @Size(max = 320, message = "Email must not exceed 320 characters") @Email(message = "Invalid email format") String email,

                @NotBlank(message = "Password is required") @Size(min = 8, max = 32, message = "Password must be between 8 and 32 characters") @Pattern(regexp = PASSWORD_REGEX, message = "Password must be 8-32 characters long, include at least one uppercase letter, one lowercase letter, one digit, and one special character. No spaces allowed") String password) {
}
