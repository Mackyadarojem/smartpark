package com.smartpark.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParkingAvailabilityResponse {

    private String lotId;
    private Integer occupiedSpace;
    private Integer capacity;
}
