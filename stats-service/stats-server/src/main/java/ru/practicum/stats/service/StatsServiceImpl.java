package ru.practicum.stats.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.common.EndpointHitDto;
import ru.practicum.common.ViewStats;
import ru.practicum.stats.mapper.EndpointMapper;
import ru.practicum.stats.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.List;

@Transactional()
@Service
public class StatsServiceImpl {

    private final StatsRepository statsRepository;

    public StatsServiceImpl(StatsRepository statsRepository) {
        this.statsRepository = statsRepository;
    }

    @Transactional
    public EndpointHitDto addHit(EndpointHitDto endpointHitDto) {
        return EndpointMapper.toEndpointHitDto(statsRepository.save(EndpointMapper.toEndpointHit(endpointHitDto)));
    }

    @Transactional
    public List<ViewStats> getViewStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        validateTimeInterval(start, end);
        List<ViewStats> viewStats;
        if (uris == null || uris.isEmpty()) {
            if (unique) {
                viewStats = statsRepository.getAllStatsByDistinctIp(start, end);
            } else {
                viewStats = statsRepository.getAllStats(start, end);
            }
        } else {
            if (unique) {
                viewStats = statsRepository.getStatsByUrisDistinctIps(uris, start, end);
            } else {
                viewStats = statsRepository.getStatsByUris(uris, start, end);
            }
        }
        return viewStats;
    }

    private static void validateTimeInterval(LocalDateTime start, LocalDateTime end) {
        LocalDateTime now = LocalDateTime.now();
        if ((start == null && end.isBefore(now)) || (start != null && end.isBefore(start))) {
            throw new IllegalArgumentException("Некорректный временной интервал");
        }
    }

}