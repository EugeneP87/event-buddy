package ru.practicum.stats.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.common.EndpointHitDto;
import ru.practicum.common.ViewStats;
import ru.practicum.stats.service.StatsServiceImpl;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
public class StatsController {

    private final StatsServiceImpl statsService;
    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public EndpointHitDto addHit(@Valid @RequestBody EndpointHitDto endpointHitDto) {
        log.info("Добавление Hit " + endpointHitDto);
        return statsService.addHit(endpointHitDto);
    }

    @GetMapping("/stats")
    public List<ViewStats> getViewStats(
            @DateTimeFormat(pattern = DATE_TIME_PATTERN) @RequestParam LocalDateTime start,
            @DateTimeFormat(pattern = DATE_TIME_PATTERN) @RequestParam LocalDateTime end,
            @RequestParam(required = false) List<String> uris,
            @RequestParam(required = false) boolean unique) {
        log.info("Получение статистики " + start + " " + end + " " + uris + " " + unique);
        return statsService.getViewStats(start, end, uris, unique);
    }

}