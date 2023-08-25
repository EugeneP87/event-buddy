package ru.practicum.mainService.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainService.request.dto.ParticipationRequestDto;
import ru.practicum.mainService.request.service.RequestServiceImpl;

import javax.validation.constraints.Positive;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class RequestController {

    private final RequestServiceImpl requestService;

    @PostMapping("{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto addUserRequest(@PathVariable(name = "userId") @Positive Long userId,
                                                  @RequestParam(name = "eventId") Long eventId) {
        log.info("Создание запроса пользователем с ID {} на участие в событии с ID {}", +userId, eventId);
        return requestService.addUserRequest(userId, eventId);
    }

    @GetMapping("{userId}/requests")
    public List<ParticipationRequestDto> getUserRequests(@PathVariable(name = "userId") @Positive Long userId) {
        log.info("Получение запросов пользователя с ID {}", userId);
        return requestService.getUserRequests(userId);
    }

    @PatchMapping("{userId}/requests/{requestId}/cancel")
    public ParticipationRequestDto cancelUserRequest(@PathVariable(name = "userId") @Positive Long userId,
                                                     @PathVariable(name = "requestId") @Positive Long requestId) {
        log.info("Отмена запроса на участие в событии с ID {} пользователем с ID {}", requestId, userId);
        return requestService.cancelUserRequest(userId, requestId);
    }

}