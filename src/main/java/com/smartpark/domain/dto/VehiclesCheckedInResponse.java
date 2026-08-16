package com.smartpark.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class VehiclesCheckedInResponse {

    private String lotId;

    private List<VehicleResponse> vehicles;
}
