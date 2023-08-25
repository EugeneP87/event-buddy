package ru.practicum.mainService.location.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationDto {

    @NotNull
    private Double lat;
    @NotNull
    private Double lon;

}