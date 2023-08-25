package ru.practicum.mainService.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.mainService.request.Request;
import ru.practicum.mainService.request.RequestStatus;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {

    List<Request> findAllByRequesterId(Long requesterId);

    boolean existsByRequesterIdAndEventId(Long requesterId, Long eventId);

    Long countByEventIdAndStatus(Long eventId, RequestStatus status);

    List<Request> findAllByEventIdInAndStatus(List<Long> eventIds, RequestStatus requestStatus);

    List<Request> findAllByEventId(Long eventId);

}