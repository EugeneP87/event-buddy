package ru.practicum.mainService.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainService.event.dto.EventDto;
import ru.practicum.mainService.event.dto.NewEventDto;
import ru.practicum.mainService.event.dto.PartialEventDto;
import ru.practicum.mainService.event.dto.UpdateEventRequestDto;
import ru.practicum.mainService.event.service.EventServiceImpl;
import ru.practicum.mainService.request.dto.EventRequestStatusUpdate;
import ru.practicum.mainService.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.mainService.request.dto.ParticipationRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/events")
public class PrivateEventController {

    private final EventServiceImpl eventServiceImpl;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventDto addEventUser(@PathVariable(name = "userId") @Positive Long userId,
                                 @Valid @RequestBody NewEventDto newEventDto) {
        log.info("Добавление нового события");
        return eventServiceImpl.addEventUser(userId, newEventDto);
    }

    @PatchMapping("{eventId}")
    public EventDto updateEventUser(@PathVariable(name = "userId") @Positive Long userId,
                                    @PathVariable(name = "eventId") @Positive Long eventId,
                                    @Valid @RequestBody UpdateEventRequestDto updateEventUserRequest) {
        log.info("Обновление события с ID {}", eventId);
        return eventServiceImpl.updateEventUser(userId, eventId, updateEventUserRequest);
    }

    @GetMapping
    public List<PartialEventDto> getEventsUser(@PathVariable @Positive Long userId,
                                               @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                               @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("Получение событий пользователя с ID {}", userId);
        return eventServiceImpl.getEventUser(userId, from, size);
    }

    @GetMapping("{eventId}")
    public EventDto getUserEventCompleteInfo(@PathVariable(name = "userId") @Positive Long userId,
                                             @PathVariable(name = "eventId") @Positive Long eventId) {
        log.info("Получение полной информации о событии с ID {}", eventId);
        return eventServiceImpl.getUserEventCompleteInfo(userId, eventId);
    }

    @GetMapping("{eventId}/requests")
    public List<ParticipationRequestDto> getAllRequestsByEventId(@PathVariable(name = "userId") @Positive Long userId,
                                                                 @PathVariable(name = "eventId") @Positive Long eventId) {
        log.info("Получение информации о запросах события с ID {}", eventId);
        return eventServiceImpl.getAllRequestsByEventId(userId, eventId);
    }

    @PatchMapping("{eventId}/requests")
    public EventRequestStatusUpdateResult changeStatusRequest(@PathVariable(name = "userId") @Positive Long userId,
                                                              @PathVariable(name = "eventId") @Positive Long eventId,
                                                              @RequestBody EventRequestStatusUpdate statusUpdate) {
        log.info("Изменение статуса заявки с ID {}", eventId);
        return eventServiceImpl.changeStatusRequest(userId, eventId, statusUpdate);
    }

}