package com.smartpark.service;

import com.smartpark.config.InvalidDataException;
import com.smartpark.domain.dto.*;
import com.smartpark.domain.entity.Parking;
import com.smartpark.domain.entity.ParkingSession;
import com.smartpark.domain.entity.Vehicle;
import com.smartpark.domain.mapper.ParkingMapper;
import com.smartpark.domain.mapper.ParkingSessionMapper;
import com.smartpark.domain.mapper.VehicleMapper;
import com.smartpark.repository.ParkingRepository;
import com.smartpark.repository.ParkingSessionRepository;
import com.smartpark.repository.VehicleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParkingService {

    public static final String LOT_ID_ALREADY_EXISTS = "Lot ID already exists: ";
    public static final String LOT_ID_NOT_FOUND_OR_NOT_ALREADY_REGISTERED = "Lot ID not found or not already registered ";
    public static final String LICENSE_PLATE_NOT_FOUND_OR_NOT_ALREADY_REGISTERED = "License Plate not found or not already registered ";
    private final ParkingRepository parkingRepository;
    private final VehicleRepository vehicleRepository;
    private final ParkingSessionRepository parkingSessionRepository;
    private final ParkingSessionMapper parkingSessionMapper;
    private final ParkingMapper parkingMapper;
    private final VehicleMapper vehicleMapper;

    public ParkingResponse registerParking(ParkingRequest parkingRequest) {

        Parking parking = parkingMapper.mapToEntity(parkingRequest);

        if (parkingRepository.existsByLotId(parkingRequest.getLotId())) {
            throw new InvalidDataException(
                    LOT_ID_ALREADY_EXISTS + parkingRequest.getLotId()
            );
        }

        Parking savedParking = parkingRepository.save(parking);

        return parkingMapper.mapToDto(savedParking);
    }

    @Transactional
    public ParkingSessionResponse checkIn(ParkingSessionRequest parkingSessionRequest) {

        validateParking(parkingSessionRequest);

        validateVehicle(parkingSessionRequest);

        Parking parking = parkingRepository.findByLotId(parkingSessionRequest.getLotId());

        validateCapacity(parkingSessionRequest, parking);

        Vehicle vehicle = vehicleRepository.findByLicensePlate(parkingSessionRequest.getLicensePlate());

        ParkingSession parkingSession = parkingSessionMapper.mapToEntity(parking, vehicle);

        ParkingSession savedParkingSession = parkingSessionRepository.save(parkingSession);
        parkingRepository.incrementOccupiedSpace(parking.getId());

        return parkingSessionMapper.mapToDTO(savedParkingSession);
    }

    @Transactional
    public ParkingSessionResponse checkOut(ParkingSessionRequest parkingSessionRequest) {

        validateVehicleCheckout(parkingSessionRequest);

        ParkingSession parkingSession = parkingSessionRepository.findByLicensePlateAndLotId(parkingSessionRequest.getLicensePlate(), parkingSessionRequest.getLotId());
        parkingSession.setCheckOutTime(LocalDateTime.now());
        LocalDateTime checkInTime = parkingSession.getCheckInTime();
        LocalDateTime checkOutTime = parkingSession.getCheckOutTime();
        long totalMinutes = Math.max(
                1,
                Duration.between(
                        parkingSession.getCheckInTime(),
                        parkingSession.getCheckOutTime()
                ).toMinutes()
        );

        Integer costPerMinute = parkingSession
                .getParking()
                .getCostPerMinute();
        Integer totalCost = Math.toIntExact(totalMinutes * costPerMinute);
        parkingSession.setTotalCost(totalCost);
        ParkingSession savedParkingSession = parkingSessionRepository.save(parkingSession);
        parkingRepository.decrementOccupiedSpace(parkingSession.getParking().getId());
        return parkingSessionMapper.mapToDTO(savedParkingSession);
    }

    public List<ParkingAvailabilityResponse> getParkingAvailability() {

        List<Parking> parkings = parkingRepository.findAll();

        if (parkings.isEmpty()) {
            throw new InvalidDataException("No parking lots are currently registered.");
        }

        return parkings
                .stream()
                .map(parkingMapper::mapToAvailabilityDTO)
                .toList();
    }

    public List<VehiclesCheckedInResponse> getVehiclesCheckedIn() {

        List<ParkingSession> parkingSessions =
                parkingSessionRepository.findVehiclesCheckIn();

        if (parkingSessions.isEmpty()) {
            throw new InvalidDataException(
                    "No vehicles are currently checked in."
            );
        }

        Map<String, List<ParkingSession>> groupedByLot =
                parkingSessions.stream()
                        .collect(Collectors.groupingBy(
                                ps -> ps.getParking().getLotId()
                        ));

        return groupedByLot.entrySet()
                .stream()
                .map(entry -> {

                    VehiclesCheckedInResponse result =
                            new VehiclesCheckedInResponse();

                    result.setLotId(entry.getKey());

                    result.setVehicles(
                            entry.getValue()
                                    .stream()
                                    .map(ParkingSession::getVehicle)
                                    .map(vehicleMapper::mapToDto)
                                    .toList()
                    );

                    return result;
                })
                .toList();
    }

    private void validateVehicleCheckout(ParkingSessionRequest parkingSessionRequest) {
        if (!parkingSessionRepository.checkByLicensePlateAndLotId(parkingSessionRequest.getLicensePlate(), parkingSessionRequest.getLotId())) {
            throw new InvalidDataException(
                    "Vehicle with license plate '" +
                            parkingSessionRequest.getLicensePlate() +
                            "' not found in parking lot: " + parkingSessionRequest.getLotId()
            );
        }
    }

    private static void validateCapacity(ParkingSessionRequest parkingSessionRequest, Parking parking) {

        System.out.println(parking.getOccupiedSpace());
        System.out.println(parking.getCapacity());

        if (parking.getOccupiedSpace() != null && parking.getOccupiedSpace() >= parking.getCapacity()) {
            throw new InvalidDataException(
                    "Parking lot '" + parking.getLotId() + "' has no available capacity."
            );
        }
    }

    private void validateParking(ParkingSessionRequest parkingSessionRequest) {
        if (!parkingRepository.existsByLotId(parkingSessionRequest.getLotId())) {
            throw new InvalidDataException(
                    LOT_ID_NOT_FOUND_OR_NOT_ALREADY_REGISTERED + parkingSessionRequest.getLotId()
            );
        }
    }

    private void validateVehicle(ParkingSessionRequest parkingSessionRequest) {
        if (!vehicleRepository.existsByLicensePlate(parkingSessionRequest.getLicensePlate())) {
            throw new InvalidDataException(
                    LICENSE_PLATE_NOT_FOUND_OR_NOT_ALREADY_REGISTERED + parkingSessionRequest.getLotId()
            );
        }

        if (parkingSessionRepository.checkByLicensePlate(parkingSessionRequest.getLicensePlate())) {
            throw new InvalidDataException(
                    "Vehicle with license plate '" +
                            parkingSessionRequest.getLicensePlate() +
                            "' is already checked in."
            );
        }
    }
}
