package com.smartpark.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParkingResponse {

    private String lotId;
    private String location;
    private Integer capacity;
    private Integer costPerMinute;
}
