package com.smartpark.domain.mapper;

import com.smartpark.domain.dto.ParkingAvailabilityResponse;
import com.smartpark.domain.dto.ParkingRequest;
import com.smartpark.domain.dto.ParkingResponse;
import com.smartpark.domain.entity.Parking;
import org.springframework.stereotype.Component;

@Component
public class ParkingMapper {

    public Parking mapToEntity(ParkingRequest parkingRequest){

        Parking parking = new Parking();
        parking.setLotId(parkingRequest.getLotId());
        parking.setCapacity(parkingRequest.getCapacity());
        parking.setLocation(parkingRequest.getLocation());
        parking.setCostPerMinute(parkingRequest.getCostPerMinute());

        return parking;
    }

    public ParkingResponse mapToDto(Parking parking){

        ParkingResponse parkingResponse = new ParkingResponse();
        parkingResponse.setLotId(parking.getLotId());
        parkingResponse.setCapacity(parking.getCapacity());
        parkingResponse.setLocation(parking.getLocation());
        parkingResponse.setCostPerMinute(parking.getCostPerMinute());

        return parkingResponse;
    }

    public ParkingAvailabilityResponse mapToAvailabilityDTO(Parking parking){

        ParkingAvailabilityResponse parkingAvailabilityResponse = new ParkingAvailabilityResponse();
        parkingAvailabilityResponse.setLotId(parking.getLotId());
        parkingAvailabilityResponse.setCapacity(parking.getCapacity());
        parkingAvailabilityResponse.setOccupiedSpace(parking.getOccupiedSpace());

        return parkingAvailabilityResponse;
    }
}
