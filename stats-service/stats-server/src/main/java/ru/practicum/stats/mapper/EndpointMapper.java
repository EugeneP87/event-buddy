package ru.practicum.stats.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.common.EndpointHitDto;
import ru.practicum.stats.EndpointHit;

import java.time.LocalDateTime;

import static ru.practicum.common.DateAndTimeFormatter.DATE_TIME_FORMATTER;

@UtilityClass
public final class EndpointMapper {

    public EndpointHitDto toEndpointHitDto(EndpointHit endpointHit) {
        return new EndpointHitDto(
                endpointHit.getApp(),
                endpointHit.getUri(),
                endpointHit.getIp(),
                endpointHit.getTimestamp().format(DATE_TIME_FORMATTER)
        );
    }

    public EndpointHit toEndpointHit(EndpointHitDto endpointHitDto) {
        return new EndpointHit(
                endpointHitDto.getApp(),
                endpointHitDto.getUri(),
                endpointHitDto.getIp(),
                LocalDateTime.parse(endpointHitDto.getTimestamp(), DATE_TIME_FORMATTER)
        );
    }

}