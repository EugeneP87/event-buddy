package ru.practicum.mainService.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainService.common.DateAndTimeFormatter;
import ru.practicum.mainService.event.dto.EventDto;
import ru.practicum.mainService.event.service.EventServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class PublicEventController {

    private final EventServiceImpl eventServiceImpl;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<EventDto> getAllEvents(@RequestParam(required = false) String text,
                                             @RequestParam(required = false) List<Long> categories,
                                             @RequestParam(required = false) Boolean paid,
                                             @RequestParam(required = false)
                                             @DateTimeFormat(pattern = DateAndTimeFormatter.DATE_TIME_PATTERN) LocalDateTime rangeStart,
                                             @RequestParam(required = false)
                                             @DateTimeFormat(pattern = DateAndTimeFormatter.DATE_TIME_PATTERN) LocalDateTime rangeEnd,
                                             @RequestParam(required = false) boolean onlyAvailable,
                                             @RequestParam(required = false) String sort,
                                             @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                             @RequestParam(defaultValue = "10") @Positive Integer size,
                                             HttpServletRequest request) {
        log.info("Получение событий по фильтру");
        return eventServiceImpl.getAllEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort,
                from, size, request);
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public EventDto getCompleteEvent(@PathVariable Long id, HttpServletRequest httpRequest) {
        log.info("Получение полной информации о событии с ID {}", id);
        return eventServiceImpl.getCompleteEvent(id, httpRequest);
    }

}