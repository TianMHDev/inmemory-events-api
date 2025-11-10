package com.example.inmemory_events_api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@Entity
@Table(name = "venues")
public class VenueDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre del venue no puede estar vacío")
    private String name;

    @NotBlank(message = "La dirección del venue no puede estar vacía")
    private String address;

    @Positive(message = "La capacidad debe ser mayor que 0")
    private int capacity;
}
