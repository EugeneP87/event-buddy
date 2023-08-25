package ru.practicum.mainService.location.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.mainService.location.Location;
import ru.practicum.mainService.location.dto.LocationDto;

@UtilityClass
public final class LocationMapper {

    public LocationDto toLocationDto(Location location) {
        return new LocationDto(
                location.getLat(),
                location.getLon()
        );
    }

    public Location toLocation(LocationDto locationDto) {
        return new Location(
                locationDto.getLat(),
                locationDto.getLon()
        );
    }

}