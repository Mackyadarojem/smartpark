package com.smartpark.domain.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ParkingSessionRequest {

    @NotBlank(message = "License plate is required")
    @Size(max = 50, message = "License plate must not exceed 50 characters")
    @Pattern(
            regexp = "^[A-Za-z0-9-]+$",
            message = "License plate may contain only letters, numbers, and dashes"
    )
    private String licensePlate;

    @Size(max = 50, message = "Name must not exceed 50 characters")
    @Column(length = 50)
    private String lotId;
}
