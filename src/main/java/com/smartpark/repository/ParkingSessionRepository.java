package com.smartpark.repository;

import com.smartpark.domain.entity.Parking;
import com.smartpark.domain.entity.ParkingSession;
import jakarta.persistence.Entity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParkingSessionRepository extends JpaRepository<ParkingSession, Long> {

    @Query("""
                SELECT COUNT(ps) > 0
                FROM ParkingSession ps
                WHERE ps.vehicle.licensePlate = :licensePlate
                  AND ps.checkOutTime IS NULL
            """)
    boolean checkByLicensePlate(@Param("licensePlate") String licensePlate);

    @Query("""
                SELECT COUNT(ps) > 0
                FROM ParkingSession ps
                WHERE ps.vehicle.licensePlate = :licensePlate
                  AND ps.checkOutTime IS NULL
                  AND ps.parking.lotId = :lotId
            """)
    boolean checkByLicensePlateAndLotId(@Param("licensePlate") String licensePlate, @Param("lotId") String lotId);

    @Query("""
                SELECT ps
                FROM ParkingSession ps
                WHERE ps.vehicle.licensePlate = :licensePlate
                  AND ps.checkOutTime IS NULL
                  AND ps.parking.lotId = :lotId
            """)
    ParkingSession findByLicensePlateAndLotId(@Param("licensePlate") String licensePlate, @Param("lotId") String lotId);

    @Query("""
                SELECT ps
                FROM ParkingSession ps
                WHERE ps.checkOutTime IS NULL
            """)
    List<ParkingSession> findVehiclesCheckIn();
}
