package ru.practicum.mainService.location.mapper;

import ru.practicum.mainService.location.Location;
import ru.practicum.mainService.location.dto.LocationDto;

public final class LocationMapper {

    private LocationMapper() {
    }

    public static LocationDto toLocationDto(Location location) {
        return new LocationDto(
                location.getLat(),
                location.getLon()
        );
    }

    public static Location toLocation(LocationDto locationDto) {
        return new Location(
                locationDto.getLat(),
                locationDto.getLon()
        );
    }

}