package ru.practicum.stats.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.common.ViewStats;
import ru.practicum.stats.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<EndpointHit, Long> {

    @Query("SELECT NEW ru.practicum.common.ViewStats(eh.app, eh.uri, COUNT(DISTINCT eh.ip)) " +
            "FROM EndpointHit AS eh " +
            "WHERE eh.timestamp BETWEEN :start AND :end " +
            "GROUP BY eh.app, " +
            "         eh.uri " +
            "ORDER BY COUNT(DISTINCT eh.ip) DESC")
    List<ViewStats> getAllStatsByDistinctIp(LocalDateTime start, LocalDateTime end);

    @Query("SELECT NEW ru.practicum.common.ViewStats(eh.app, eh.uri, COUNT(eh.ip)) " +
            "FROM EndpointHit AS eh " +
            "WHERE eh.timestamp BETWEEN :start AND :end " +
            "GROUP BY eh.app, " +
            "         eh.uri " +
            "ORDER BY COUNT(eh.ip) DESC")
    List<ViewStats> getAllStats(LocalDateTime start, LocalDateTime end);

    @Query("SELECT NEW ru.practicum.common.ViewStats(eh.app, eh.uri, COUNT(DISTINCT eh.ip)) " +
            "FROM EndpointHit AS eh " +
            "WHERE eh.uri IN (:uri) " +
            "AND eh.timestamp BETWEEN :start AND :end " +
            "GROUP BY eh.app, " +
            "         eh.uri " +
            "ORDER BY COUNT(DISTINCT eh.ip) DESC")
    List<ViewStats> getStatsByUrisDistinctIps(List<String> uri, LocalDateTime start, LocalDateTime end);

    @Query("SELECT NEW ru.practicum.common.ViewStats(eh.app, eh.uri, COUNT(eh.ip)) " +
            "FROM EndpointHit AS eh " +
            "WHERE eh.uri IN(:uri) " +
            "AND eh.timestamp BETWEEN :start AND :end " +
            "GROUP BY eh.app, " +
            "         eh.uri " +
            "ORDER BY COUNT(eh.ip) DESC")
    List<ViewStats> getStatsByUris(List<String> uri, LocalDateTime start, LocalDateTime end);

}