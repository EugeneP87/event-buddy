package ru.practicum.mainService.request.mapper;

import ru.practicum.mainService.request.Request;
import ru.practicum.mainService.request.dto.ParticipationRequestDto;

public final class RequestMapper {

    private RequestMapper() {
    }

    public static ParticipationRequestDto toParticipationRequestDto(Request request) {
        return new ParticipationRequestDto(
                request.getId(),
                request.getCreated(),
                request.getEvent().getId(),
                request.getRequester().getId(),
                request.getStatus()
        );
    }

}