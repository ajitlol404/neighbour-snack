package com.neighbour_snack.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class SmtpDTO {
        public record SmtpRequestDTO(
                        @NotBlank(message = "Host cannot be blank") @Size(max = 255, message = "Host must not exceed 255 characters") String host,

                        @NotNull(message = "Port cannot be null") @Min(value = 1, message = "Port must be at least 1") @Max(value = 65535, message = "Port cannot exceed 65535") Integer port,

                        @NotNull(message = "SSL flag cannot be null") Boolean isSsl,

                        @NotBlank(message = "Username cannot be blank") @Size(max = 255, message = "Username must not exceed 255 characters") String username,

                        @NotBlank(message = "Password cannot be blank") @Size(max = 255, message = "Password must not exceed 255 characters") String password) {
        }

        public record SmtpResponseDTO(
                        String host,
                        Integer port,
                        Boolean isSsl,
                        String username) {
        }
}
