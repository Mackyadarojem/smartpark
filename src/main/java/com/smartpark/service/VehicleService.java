package com.smartpark.service;

import com.smartpark.config.InvalidDataException;
import com.smartpark.domain.dto.VehicleRequest;
import com.smartpark.domain.dto.VehicleResponse;
import com.smartpark.domain.entity.Vehicle;
import com.smartpark.domain.mapper.VehicleMapper;
import com.smartpark.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VehicleService {

    public static final String VEHICLE_ALREADY_REGISTERED = "Vehicle Already Registered ";
    private final VehicleMapper vehicleMapper;
    private final VehicleRepository vehicleRepository;

    public VehicleResponse registerVehicle(VehicleRequest vehicleRequest) {

        Vehicle vehicle = vehicleMapper.mapToEntity(vehicleRequest);

        if (vehicleRepository.existsByLicensePlate(vehicleRequest.getLicensePlate())) {
            throw new InvalidDataException(
                    VEHICLE_ALREADY_REGISTERED + vehicleRequest.getLicensePlate()
            );
        }

        Vehicle saveVehicle = vehicleRepository.save(vehicle);

        return vehicleMapper.mapToDto(saveVehicle);
    }
}
