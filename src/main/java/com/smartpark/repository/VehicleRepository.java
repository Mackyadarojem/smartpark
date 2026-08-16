package com.smartpark.repository;

import com.smartpark.domain.entity.Vehicle;
import org.hibernate.generator.internal.VersionGeneration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    boolean existsByLicensePlate(String licensePlate);

    Vehicle findByLicensePlate(String licensePlate);
}
