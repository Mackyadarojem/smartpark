package com.smartpark.service;

import com.smartpark.config.InvalidDataException;
import com.smartpark.domain.dto.ParkingRequest;
import com.smartpark.domain.dto.ParkingResponse;
import com.smartpark.domain.dto.ParkingSessionRequest;
import com.smartpark.domain.dto.ParkingSessionResponse;
import com.smartpark.domain.entity.Parking;
import com.smartpark.domain.entity.ParkingSession;
import com.smartpark.domain.entity.Vehicle;
import com.smartpark.domain.mapper.ParkingMapper;
import com.smartpark.domain.mapper.ParkingSessionMapper;
import com.smartpark.repository.ParkingRepository;
import com.smartpark.repository.ParkingSessionRepository;
import com.smartpark.repository.VehicleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ParkingServiceTest {
    @Mock
    private ParkingRepository parkingRepository;

    @Mock
    private ParkingMapper parkingMapper;

    @Mock
    private VehicleRepository vehicleRepository;

    @InjectMocks
    private ParkingService parkingService;

    @Mock
    private ParkingSessionMapper parkingSessionMapper;

    @Mock
    private ParkingSessionRepository parkingSessionRepository;

    @Test
    void registerParking_shouldSaveAndReturnParking() {
        ParkingRequest request = new ParkingRequest();
        request.setLotId("LOT-001");

        Parking parking = new Parking();
        parking.setLotId("LOT-001");

        Parking savedParking = new Parking();
        savedParking.setLotId("LOT-001");

        ParkingResponse expectedResponse = new ParkingResponse();
        expectedResponse.setLotId("LOT-001");

        when(parkingMapper.mapToEntity(request))
                .thenReturn(parking);

        when(parkingRepository.existsByLotId("LOT-001"))
                .thenReturn(false);

        when(parkingRepository.save(parking))
                .thenReturn(savedParking);

        when(parkingMapper.mapToDto(savedParking))
                .thenReturn(expectedResponse);

        ParkingResponse result = parkingService.registerParking(request);

        assertNotNull(result);
        assertEquals("LOT-001", result.getLotId());

        verify(parkingMapper).mapToEntity(request);
        verify(parkingRepository).existsByLotId("LOT-001");
        verify(parkingRepository).save(parking);
        verify(parkingMapper).mapToDto(savedParking);
    }

    @Test
    void registerParking_shouldThrowExceptionWhenLotIdAlreadyExists() {
        ParkingRequest request = new ParkingRequest();
        request.setLotId("LOT-001");

        Parking parking = new Parking();
        parking.setLotId("LOT-001");

        when(parkingMapper.mapToEntity(request))
                .thenReturn(parking);

        when(parkingRepository.existsByLotId("LOT-001"))
                .thenReturn(true);

        InvalidDataException exception = assertThrows(
                InvalidDataException.class,
                () -> parkingService.registerParking(request)
        );

        assertTrue(
                exception.getMessage().contains("LOT-001")
        );

        verify(parkingMapper).mapToEntity(request);
        verify(parkingRepository).existsByLotId("LOT-001");

        verify(parkingRepository, never()).save(any(Parking.class));

        verify(parkingMapper, never()).mapToDto(any(Parking.class));
    }

    @Test
    void checkIn_shouldSuccessfullyCheckInVehicle() {
        ParkingSessionRequest request = new ParkingSessionRequest();
        request.setLotId("LOT-001");
        request.setLicensePlate("ABC-1234");

        Parking parking = new Parking();
        parking.setId(1L);
        parking.setLotId("LOT-001");
        parking.setCapacity(50);
        parking.setOccupiedSpace(10);

        Vehicle vehicle = new Vehicle();
        vehicle.setId(1L);
        vehicle.setLicensePlate("ABC-1234");

        ParkingSession parkingSession = new ParkingSession();
        ParkingSession savedParkingSession = new ParkingSession();
        ParkingSessionResponse expectedResponse = new ParkingSessionResponse();

        when(parkingRepository.existsByLotId("LOT-001"))
                .thenReturn(true);

        when(parkingRepository.findByLotId("LOT-001"))
                .thenReturn(parking);

        when(vehicleRepository.existsByLicensePlate("ABC-1234"))
                .thenReturn(true);

        when(vehicleRepository.findByLicensePlate("ABC-1234"))
                .thenReturn(vehicle);

        when(parkingSessionMapper.mapToEntity(parking, vehicle))
                .thenReturn(parkingSession);

        when(parkingSessionRepository.save(parkingSession))
                .thenReturn(savedParkingSession);

        when(parkingSessionMapper.mapToDTO(savedParkingSession))
                .thenReturn(expectedResponse);

        ParkingSessionResponse result =
                parkingService.checkIn(request);

        assertNotNull(result);
        assertEquals(expectedResponse, result);

        verify(parkingRepository)
                .existsByLotId("LOT-001");

        verify(parkingRepository)
                .findByLotId("LOT-001");

        verify(vehicleRepository)
                .existsByLicensePlate("ABC-1234");

        verify(vehicleRepository)
                .findByLicensePlate("ABC-1234");

        verify(parkingSessionRepository)
                .save(parkingSession);

        verify(parkingRepository)
                .incrementOccupiedSpace(1L);

        verify(parkingSessionMapper)
                .mapToDTO(savedParkingSession);
    }

    @Test
    void checkOut_shouldSuccessfullyCheckOutVehicle() {
        String licensePlate = "ABC-1234";
        String lotId = "LOT-001";

        ParkingSessionRequest request = new ParkingSessionRequest();
        request.setLicensePlate(licensePlate);
        request.setLotId(lotId);

        Parking parking = new Parking();
        parking.setId(1L);
        parking.setLotId(lotId);
        parking.setCostPerMinute(5);

        ParkingSession parkingSession = new ParkingSession();
        parkingSession.setId(1L);
        Vehicle vehicle = new Vehicle();
        vehicle.setLicensePlate(licensePlate);
        parkingSession.setVehicle(vehicle);
        parkingSession.setParking(parking);

        parkingSession.setCheckInTime(
                LocalDateTime.now().minusMinutes(10)
        );

        ParkingSessionResponse expectedResponse =
                new ParkingSessionResponse();

        when(parkingSessionRepository
                .checkByLicensePlateAndLotId(licensePlate, lotId))
                .thenReturn(true);

        when(parkingSessionRepository
                .findByLicensePlateAndLotId(licensePlate, lotId))
                .thenReturn(parkingSession);

        when(parkingSessionRepository.save(parkingSession))
                .thenReturn(parkingSession);

        when(parkingSessionMapper.mapToDTO(parkingSession))
                .thenReturn(expectedResponse);

        ParkingSessionResponse result =
                parkingService.checkOut(request);

        assertNotNull(result);
        assertEquals(50, parkingSession.getTotalCost());

        verify(parkingSessionRepository)
                .checkByLicensePlateAndLotId(
                        licensePlate,
                        lotId
                );

        verify(parkingSessionRepository)
                .findByLicensePlateAndLotId(
                        licensePlate,
                        lotId
                );

        verify(parkingSessionRepository)
                .save(parkingSession);

        verify(parkingRepository)
                .decrementOccupiedSpace(1L);

        verify(parkingSessionMapper)
                .mapToDTO(parkingSession);
    }
}
