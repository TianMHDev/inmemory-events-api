package com.example.inmemory_events_api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VenueDTO {
    private Long id;

    @NotBlank
    private String name;

    private String location;
}
