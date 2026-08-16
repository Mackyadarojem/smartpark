package com.smartpark.service;

import com.smartpark.domain.entity.Parking;
import com.smartpark.domain.entity.ParkingSession;
import com.smartpark.domain.entity.Vehicle;
import com.smartpark.repository.ParkingRepository;
import com.smartpark.repository.ParkingSessionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ParkingCronServiceTest {


    @Mock
    private ParkingSessionRepository parkingSessionRepository;

    @Mock
    private ParkingRepository parkingRepository;

    @InjectMocks
    private ParkingCronService parkingCronService;

    @Test
    void processParkingSessions_shouldAutomaticallyCheckoutVehicle() {

        // Arrange
        Parking parking = new Parking();
        parking.setId(1L);
        parking.setCostPerMinute(5);

        Vehicle vehicle = new Vehicle();
        vehicle.setLicensePlate("ABC-1234");

        ParkingSession parkingSession = new ParkingSession();
        parkingSession.setId(1L);
        parkingSession.setCheckInTime(
                LocalDateTime.now().minusMinutes(20)
        );
        parkingSession.setParking(parking);
        parkingSession.setVehicle(vehicle);

        when(parkingSessionRepository.findVehiclesCheckIn())
                .thenReturn(List.of(parkingSession));

        parkingCronService.processParkingSessions();

        assertNotNull(parkingSession.getCheckOutTime());
        assertNotNull(parkingSession.getTotalCost());

        assertEquals(100, parkingSession.getTotalCost());

        verify(parkingSessionRepository)
                .findVehiclesCheckIn();

        verify(parkingRepository)
                .decrementOccupiedSpace(1L);

        verify(parkingSessionRepository)
                .save(parkingSession);
    }

    @Test
    void processParkingSessions_shouldNotCheckoutVehicleWhenLessThan15Minutes() {

        // Arrange
        Parking parking = new Parking();
        parking.setId(1L);
        parking.setCostPerMinute(5);

        Vehicle vehicle = new Vehicle();
        vehicle.setLicensePlate("ABC-1234");

        ParkingSession parkingSession = new ParkingSession();
        parkingSession.setId(1L);
        parkingSession.setCheckInTime(
                LocalDateTime.now().minusMinutes(10)
        );
        parkingSession.setParking(parking);
        parkingSession.setVehicle(vehicle);

        when(parkingSessionRepository.findVehiclesCheckIn())
                .thenReturn(List.of(parkingSession));

        // Act
        parkingCronService.processParkingSessions();

        // Assert
        assertNull(parkingSession.getCheckOutTime());
        assertNull(parkingSession.getTotalCost());

        verify(parkingSessionRepository)
                .findVehiclesCheckIn();

        verify(parkingSessionRepository, never())
                .save(any(ParkingSession.class));

        verify(parkingRepository, never())
                .decrementOccupiedSpace(anyLong());
    }

    @Test
    void processParkingSessions_shouldDoNothingWhenNoActiveSessions() {
        when(parkingSessionRepository.findVehiclesCheckIn())
                .thenReturn(Collections.emptyList());

        parkingCronService.processParkingSessions();

        verify(parkingSessionRepository)
                .findVehiclesCheckIn();

        verify(parkingSessionRepository, never())
                .save(any(ParkingSession.class));

        verify(parkingRepository, never())
                .decrementOccupiedSpace(anyLong());
    }
}
