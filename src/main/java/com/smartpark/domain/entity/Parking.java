package com.smartpark.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Parking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String location;

    @NotBlank(message = "Lot ID is required")
    @Size(max = 50, message = "Lot ID must not exceed 50 characters")
    @Column(nullable = false, unique = true, length = 50)
    private String lotId;

    private Integer capacity;

    @Column(nullable = false)
    private Integer occupiedSpace = 0;

    private Integer costPerMinute;
}
