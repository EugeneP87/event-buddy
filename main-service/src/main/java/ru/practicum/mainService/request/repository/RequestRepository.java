package ru.practicum.mainService.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.mainService.event.dto.ConfirmedRequestCountProjection;
import ru.practicum.mainService.request.Request;
import ru.practicum.mainService.request.RequestStatus;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {

    List<Request> findAllByRequesterId(Long requesterId);

    boolean existsByRequesterIdAndEventId(Long requesterId, Long eventId);

    Long countByEventIdAndStatus(Long eventId, RequestStatus status);

    List<Request> findAllByEventId(Long eventId);

    @Query("SELECT new ru.practicum.mainService.event.dto.ConfirmedRequestCountProjection(COUNT(r), r.event.id) " +
            "FROM Request r " +
            "WHERE r.event.id IN :eventIds " +
            "AND r.status = :status " +
            "GROUP BY r.event.id")
    List<ConfirmedRequestCountProjection> countConfirmedRequestsByEventIdInAndStatus(List<Long> eventIds, RequestStatus status);

}