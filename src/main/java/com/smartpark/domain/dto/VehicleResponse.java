package com.smartpark.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VehicleResponse {

    private String licensePlate;
    private String type;
    private String ownerName;
}
