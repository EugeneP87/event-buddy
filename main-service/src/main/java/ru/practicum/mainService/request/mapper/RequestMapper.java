package ru.practicum.mainService.request.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.mainService.request.Request;
import ru.practicum.mainService.request.dto.ParticipationRequestDto;

@UtilityClass
public final class RequestMapper {

    public ParticipationRequestDto toParticipationRequestDto(Request request) {
        return new ParticipationRequestDto(
                request.getId(),
                request.getCreated(),
                request.getEvent().getId(),
                request.getRequester().getId(),
                request.getStatus()
        );
    }

}