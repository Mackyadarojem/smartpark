package com.smartpark.domain.mapper;

import com.smartpark.domain.dto.VehicleRequest;
import com.smartpark.domain.dto.VehicleResponse;
import com.smartpark.domain.entity.Vehicle;
import org.springframework.stereotype.Component;

@Component
public class VehicleMapper {

    public Vehicle mapToEntity(VehicleRequest vehicleRequest) {

        Vehicle vehicle = new Vehicle();
        vehicle.setType(vehicleRequest.getType());
        vehicle.setLicensePlate(vehicleRequest.getLicensePlate());
        vehicle.setOwnerName(vehicleRequest.getOwnerName());

        return vehicle;
    }

    public VehicleResponse mapToDto(Vehicle Vehicle) {

        VehicleResponse vehicleResponse = new VehicleResponse();
        vehicleResponse.setType(Vehicle.getType());
        vehicleResponse.setLicensePlate(Vehicle.getLicensePlate());
        vehicleResponse.setOwnerName(Vehicle.getOwnerName());

        return vehicleResponse;
    }
}
