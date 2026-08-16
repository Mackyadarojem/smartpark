package com.smartpark.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ParkingSessionResponse {

    private String lotId;

    private String licensePlate;

    private LocalDateTime checkInTime;

    private LocalDateTime checkOutTime;

    private Integer totalCost;
}
