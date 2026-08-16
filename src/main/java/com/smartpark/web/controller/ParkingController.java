package com.smartpark.web.controller;

import com.smartpark.domain.dto.ParkingResponse;
import com.smartpark.domain.dto.ParkingRequest;
import com.smartpark.domain.dto.ParkingSessionRequest;
import com.smartpark.service.ParkingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/parking")
@RequiredArgsConstructor
public class ParkingController {

    public static final String PARKING_LOT_REGISTERED_SUCCESSFULLY = "Parking lot registered successfully.";
    public static final String STATUS = "status";
    public static final String MESSAGE = "message";
    public static final String DATA = "data";
    public static final int STATUS_200 = 200;
    public static final String VEHICLE_SUCCESSFULLY_CHECKED_IN = "Vehicle successfully checked in";
    public static final String VEHICLE_SUCCESSFULLY_CHECKED_OUT = "Vehicle successfully checked out";
    private final ParkingService parkingService;

    @PostMapping("/register")
    public ResponseEntity<?> registerParkingLot(@Valid @RequestBody ParkingRequest parkingRequest) {

        return ResponseEntity.ok(
                Map.of(
                        STATUS, STATUS_200,
                        MESSAGE, PARKING_LOT_REGISTERED_SUCCESSFULLY,
                        DATA, parkingService.registerParking(parkingRequest)
                )
        );
    }

    @PostMapping("/checkIn")
    public ResponseEntity<?> checkIn(@Valid @RequestBody ParkingSessionRequest parkingSessionRequest) {

        return ResponseEntity.ok(
                Map.of(
                        STATUS, STATUS_200,
                        MESSAGE, VEHICLE_SUCCESSFULLY_CHECKED_IN,
                        DATA, parkingService.checkIn(parkingSessionRequest)
                )
        );
    }

    @PostMapping("/checkOut")
    public ResponseEntity<?> checkOut(@Valid @RequestBody ParkingSessionRequest parkingSessionRequest) {

        return ResponseEntity.ok(
                Map.of(
                        STATUS, STATUS_200,
                        MESSAGE, VEHICLE_SUCCESSFULLY_CHECKED_OUT,
                        DATA, parkingService.checkOut(parkingSessionRequest)
                )
        );
    }

    @GetMapping("/availability")
    public ResponseEntity<?> getParkingAvailability() {

        return ResponseEntity.ok(
                Map.of(
                        STATUS, STATUS_200,
                        MESSAGE, "Parking availability retrieved successfully.",
                        DATA, parkingService.getParkingAvailability()
                )
        );
    }

    @GetMapping("/vehicles")
    public ResponseEntity<?> getVehiclesCheckedIn() {

        return ResponseEntity.ok(
                Map.of(
                        STATUS, STATUS_200,
                        MESSAGE, "Checked-in vehicles retrieved successfully.",
                        DATA, parkingService.getVehiclesCheckedIn()
                )
        );
    }
}
