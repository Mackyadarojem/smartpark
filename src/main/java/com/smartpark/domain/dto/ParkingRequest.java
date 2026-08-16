package com.smartpark.domain.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParkingRequest {

    @Size(max = 50, message = "Name must not exceed 50 characters")
    @Column(length = 50)
    private String lotId;
    private String location;
    private Integer capacity;
    private Integer costPerMinute;
}
