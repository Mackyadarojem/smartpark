package com.smartpark.service;

import com.smartpark.config.InvalidDataException;
import com.smartpark.domain.VehicleTypeEnum;
import com.smartpark.domain.dto.VehicleRequest;
import com.smartpark.domain.dto.VehicleResponse;
import com.smartpark.domain.entity.Vehicle;
import com.smartpark.domain.mapper.VehicleMapper;
import com.smartpark.repository.VehicleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VehicleServiceTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private VehicleMapper vehicleMapper;

    @InjectMocks
    private VehicleService vehicleService;

    @Test
    void registerVehicle_shouldSuccessfullyRegisterVehicle() {
        // Arrange
        VehicleRequest request = new VehicleRequest();
        request.setLicensePlate("ABC-1234");
        request.setType("Car");

        Vehicle vehicle = new Vehicle();
        vehicle.setLicensePlate("ABC-1234");
        vehicle.setType(VehicleTypeEnum.CAR.name());

        Vehicle savedVehicle = new Vehicle();
        savedVehicle.setLicensePlate("ABC-1234");
        savedVehicle.setType(VehicleTypeEnum.CAR.name());

        VehicleResponse expectedResponse = new VehicleResponse();

        when(vehicleMapper.mapToEntity(request))
                .thenReturn(vehicle);

        when(vehicleRepository.existsByLicensePlate("ABC-1234"))
                .thenReturn(false);

        when(vehicleRepository.save(vehicle))
                .thenReturn(savedVehicle);

        when(vehicleMapper.mapToDto(savedVehicle))
                .thenReturn(expectedResponse);

        VehicleResponse result =
                vehicleService.registerVehicle(request);

        assertNotNull(result);
        assertEquals(expectedResponse, result);

        verify(vehicleMapper)
                .mapToEntity(request);

        verify(vehicleRepository)
                .existsByLicensePlate("ABC-1234");

        verify(vehicleRepository)
                .save(vehicle);

        verify(vehicleMapper)
                .mapToDto(savedVehicle);
    }

    @Test
    void registerVehicle_shouldThrowExceptionWhenLicensePlateAlreadyExists() {
        // Arrange
        VehicleRequest request = new VehicleRequest();
        request.setLicensePlate("ABC-1234");
        request.setType("Car");

        Vehicle vehicle = new Vehicle();

        when(vehicleMapper.mapToEntity(request))
                .thenReturn(vehicle);

        when(vehicleRepository.existsByLicensePlate("ABC-1234"))
                .thenReturn(true);

        // Act & Assert
        InvalidDataException exception = assertThrows(
                InvalidDataException.class,
                () -> vehicleService.registerVehicle(request)
        );

        assertTrue(
                exception.getMessage()
                        .contains("ABC-1234")
        );

        verify(vehicleMapper)
                .mapToEntity(request);

        verify(vehicleRepository)
                .existsByLicensePlate("ABC-1234");

        // Vehicle must NOT be saved
        verify(vehicleRepository, never())
                .save(any(Vehicle.class));

        verify(vehicleMapper, never())
                .mapToDto(any(Vehicle.class));
    }

    @Test
    void registerVehicle_shouldThrowExceptionWhenVehicleTypeIsInvalid() {
        // Arrange
        VehicleRequest request = new VehicleRequest();
        request.setLicensePlate("ABC-1234");
        request.setType("Bus");

        Vehicle vehicle = new Vehicle();
        vehicle.setLicensePlate("ABC-1234");

        when(vehicleMapper.mapToEntity(request))
                .thenReturn(vehicle);
        when(vehicleRepository.existsByLicensePlate("ABC-1234"))
                .thenReturn(false);
        InvalidDataException exception = assertThrows(
                InvalidDataException.class,
                () -> vehicleService.registerVehicle(request)
        );

        assertTrue(
                exception.getMessage()
                        .contains("Bus")
        );

        verify(vehicleMapper)
                .mapToEntity(request);

        verify(vehicleRepository)
                .existsByLicensePlate("ABC-1234");

        verify(vehicleRepository, never())
                .save(any(Vehicle.class));

        verify(vehicleMapper, never())
                .mapToDto(any(Vehicle.class));
    }
}