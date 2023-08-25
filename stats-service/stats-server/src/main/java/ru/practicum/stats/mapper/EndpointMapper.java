package ru.practicum.stats.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.common.EndpointHitDto;
import ru.practicum.stats.EndpointHit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@UtilityClass
public final class EndpointMapper {

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public EndpointHitDto toEndpointHitDto(EndpointHit endpointHit) {
        return new EndpointHitDto(
                endpointHit.getApp(),
                endpointHit.getUri(),
                endpointHit.getIp(),
                endpointHit.getTimestamp().format(dateTimeFormatter)
        );
    }

    public EndpointHit toEndpointHit(EndpointHitDto endpointHitDto) {
        return new EndpointHit(
                endpointHitDto.getApp(),
                endpointHitDto.getUri(),
                endpointHitDto.getIp(),
                LocalDateTime.parse(endpointHitDto.getTimestamp(), dateTimeFormatter)
        );
    }

}