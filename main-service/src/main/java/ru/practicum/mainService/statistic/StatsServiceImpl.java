package ru.practicum.mainService.statistic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.practicum.client.StatsClient;
import ru.practicum.common.EndpointHitDto;
import ru.practicum.common.ViewStats;
import ru.practicum.mainService.event.Event;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StatsServiceImpl {

    private final String appName;
    private final StatsClient statsClient;

    @Autowired
    public StatsServiceImpl(@Value("${application.name}") String appName, StatsClient statsClient) {
        this.appName = appName;
        this.statsClient = statsClient;
    }

    public void addView(HttpServletRequest request) {
        statsClient.addHit(createEndpointHitDto(request,
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
    }

    public Map<Long, Long> getStatsEvents(List<Event> events) {
        if (events == null || events.isEmpty()) {
            return Collections.emptyMap();
        }
        LocalDateTime start = events.stream()
                .map(Event::getCreatedOn)
                .min(LocalDateTime::compareTo)
                .orElse(null);
        List<Long> ids = events.stream()
                .map(Event::getId)
                .collect(Collectors.toList());
        String eventsUri = "/events/";
        List<String> uris = ids.stream()
                .map(id -> eventsUri + id)
                .collect(Collectors.toList());
        List<ViewStats> views = statsClient.getStats(start, LocalDateTime.now(), uris, true);
        return views.stream()
                .collect(Collectors.toMap(
                        view -> Long.parseLong(view.getUri().substring(eventsUri.length())),
                        ViewStats::getHits
                ));
    }

    private EndpointHitDto createEndpointHitDto(HttpServletRequest request, String formattedDateTime) {
        return new EndpointHitDto(
                appName,
                request.getRequestURI(),
                request.getRemoteAddr(),
                formattedDateTime
        );
    }

}