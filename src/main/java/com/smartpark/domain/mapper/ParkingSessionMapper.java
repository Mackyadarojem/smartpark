package com.smartpark.domain.mapper;

import com.smartpark.domain.dto.ParkingRequest;
import com.smartpark.domain.dto.ParkingSessionRequest;
import com.smartpark.domain.dto.ParkingSessionResponse;
import com.smartpark.domain.entity.Parking;
import com.smartpark.domain.entity.ParkingSession;
import com.smartpark.domain.entity.Vehicle;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ParkingSessionMapper {

    public ParkingSession mapToEntity(Parking parking, Vehicle vehicle) {

        ParkingSession parkingSession = new ParkingSession();
        parkingSession.setParking(parking);
        parkingSession.setVehicle(vehicle);
        parkingSession.setCheckInTime(LocalDateTime.now());

        return parkingSession;
    }

    public ParkingSessionResponse mapToDTO(ParkingSession parkingSession) {

        ParkingSessionResponse parkingSessionResponse = new ParkingSessionResponse();
        parkingSessionResponse.setLotId(parkingSession.getParking().getLotId());
        parkingSessionResponse.setLicensePlate(parkingSession.getVehicle().getLicensePlate());
        parkingSessionResponse.setCheckInTime(parkingSession.getCheckInTime());
        parkingSessionResponse.setCheckOutTime(parkingSession.getCheckOutTime());
        parkingSessionResponse.setTotalCost(parkingSession.getTotalCost());

        return parkingSessionResponse;
    }
}
