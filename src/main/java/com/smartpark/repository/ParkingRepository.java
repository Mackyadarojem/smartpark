package com.smartpark.repository;

import com.smartpark.domain.entity.Parking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ParkingRepository extends JpaRepository<Parking, Long> {

    boolean existsByLotId(String lotId);

    Parking findByLotId(String lotId);

    @Modifying
    @Query("""
                UPDATE Parking p
                SET p.occupiedSpace = p.occupiedSpace + 1
                WHERE p.id = :id
            """)
    void incrementOccupiedSpace(@Param("id") Long id);

    @Modifying
    @Query("""
                UPDATE Parking p
                SET p.occupiedSpace = p.occupiedSpace - 1
                WHERE p.id = :parkingId
                  AND p.occupiedSpace > 0
            """)
    int decrementOccupiedSpace(@Param("parkingId") Long parkingId);
}
