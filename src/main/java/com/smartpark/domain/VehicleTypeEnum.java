package com.smartpark.domain;

import com.smartpark.config.InvalidDataException;
import lombok.*;

import java.util.Arrays;

@Getter
public enum VehicleTypeEnum {

    CAR,
    MOTORCYCLE,
    TRUCK;

    public static final String INVALID_VEHICLE_TYPE =
            "Invalid vehicle type: ";

    public static VehicleTypeEnum fromName(String name) {

        try {
            return VehicleTypeEnum.valueOf(
                    name.trim().toUpperCase()
            );
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new InvalidDataException(
                    INVALID_VEHICLE_TYPE + name
            );
        }
    }
}