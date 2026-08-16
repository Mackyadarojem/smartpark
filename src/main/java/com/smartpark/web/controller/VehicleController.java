package com.smartpark.web.controller;

import com.smartpark.domain.dto.ParkingRequest;
import com.smartpark.domain.dto.VehicleRequest;
import com.smartpark.service.VehicleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/vehicle")
@RequiredArgsConstructor
public class VehicleController {

    public static final String STATUS = "status";
    public static final String MESSAGE = "message";
    public static final String DATA = "data";
    public static final int STATUS_200 = 200;
    public static final String VEHICLE_REGISTERED_SUCCESSFULLY = "Vehicle registered successfully.";
    private final VehicleService vehicleService;

    @PostMapping("/register")
    public ResponseEntity<?> registerParkingLot(@Valid @RequestBody VehicleRequest vehicleRequest) {

        return ResponseEntity.ok(
                Map.of(
                        STATUS, STATUS_200,
                        MESSAGE, VEHICLE_REGISTERED_SUCCESSFULLY,
                        DATA, vehicleService.registerVehicle(vehicleRequest)
                )
        );
    }


}
