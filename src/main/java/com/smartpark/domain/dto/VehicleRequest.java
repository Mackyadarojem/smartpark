package com.smartpark.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VehicleRequest {

    @NotBlank(message = "License plate is required")
    @Size(max = 50, message = "License plate must not exceed 50 characters")
    @Pattern(
            regexp = "^[A-Za-z0-9-]+$",
            message = "License plate may contain only letters, numbers, and dashes"
    )
    private String licensePlate;
    private String type;

    @NotBlank(message = "Owner name is required")
    @Size(max = 100, message = "Owner name must not exceed 100 characters")
    @Pattern(
            regexp = "^[A-Za-z ]+$",
            message = "Owner name may contain only letters and spaces"
    )
    private String ownerName;
}
