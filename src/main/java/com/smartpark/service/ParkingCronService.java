package com.smartpark.service;

import com.smartpark.domain.entity.ParkingSession;
import com.smartpark.repository.ParkingRepository;
import com.smartpark.repository.ParkingSessionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ParkingCronService {
    public static final String PARKING_CRON_SERVICE_LOG = "Parking Cron Service Log";
    private final ParkingSessionRepository parkingSessionRepository;
    private final ParkingRepository parkingRepository;

    @Scheduled(cron = "0 * * * * *")
    @Transactional
    public void processParkingSessions() {

        log.info(PARKING_CRON_SERVICE_LOG);

        List<ParkingSession> parkingSessions =
                parkingSessionRepository.findVehiclesCheckIn();

        LocalDateTime now = LocalDateTime.now();

        for (ParkingSession parkingSession : parkingSessions) {

            LocalDateTime checkInTime =
                    parkingSession.getCheckInTime();

            long totalMinutes = Duration
                    .between(checkInTime, now)
                    .toMinutes();

            if (totalMinutes >= 15) {

                parkingSession.setCheckOutTime(now);

                Integer costPerMinute =
                        parkingSession
                                .getParking()
                                .getCostPerMinute();

                Integer totalCost =
                        Math.toIntExact(
                                totalMinutes * costPerMinute
                        );

                parkingSession.setTotalCost(totalCost);

                parkingRepository.decrementOccupiedSpace(
                        parkingSession.getParking().getId()
                );

                parkingSessionRepository.save(parkingSession);

                log.info(
                        "Automatically checked out vehicle: "
                                + parkingSession.getVehicle().getLicensePlate()
                                + " | Parking time: "
                                + totalMinutes
                                + " minutes"
                                + " | Cost: "
                                + totalCost
                );
            }
        }
    }
}
