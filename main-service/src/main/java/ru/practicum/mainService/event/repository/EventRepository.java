package ru.practicum.mainService.event.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.mainService.category.Category;
import ru.practicum.mainService.event.Event;
import ru.practicum.mainService.event.EventState;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface EventRepository extends JpaRepository<Event, Long> {

    boolean existsByCategory(Category category);

    Set<Event> findAllByIdIn(Set<Long> eventIds);

    List<Event> findAllByInitiatorId(Long initiatorId, PageRequest pageRequest);

    @Query("SELECT e " +
            "FROM Event e " +
            "WHERE (:users IS NULL OR e.initiator.id in :users) " +
            " AND (:states IS NULL OR e.state in :states) " +
            " AND (:categories IS NULL OR e.category.id in :categories) " +
            " AND (cast(:rangeStart AS java.time.LocalDateTime) IS NULL OR e.eventDate >= :rangeStart) " +
            " AND (cast(:rangeEnd AS java.time.LocalDateTime) IS NULL OR e.eventDate <= :rangeEnd) ")
    List<Event> getEventsWithUsersStatesCategoriesDateTime(List<Long> users, List<EventState> states, List<Long> categories,
                                                           LocalDateTime rangeStart, LocalDateTime rangeEnd, PageRequest pageRequest);

    @Query("SELECT e " +
            "FROM Event e " +
            "WHERE ((:text IS NULL OR LOWER(e.annotation) like LOWER(concat('%', :text, '%'))) " +
            " OR (:text IS NULL OR LOWER(e.description) like LOWER(concat('%', :text, '%')))) " +
            " AND (:state IS NULL OR e.state = :state) " +
            " AND (:categories IS NULL OR e.category.id in :categories) " +
            " AND (:paid IS NULL OR e.paid = :paid) " +
            " AND (cast(:rangeStart AS java.time.LocalDateTime) IS NULL OR e.eventDate >= :rangeStart) " +
            " AND (cast(:rangeEnd AS java.time.LocalDateTime) IS NULL OR e.eventDate <= :rangeEnd) ")
    List<Event> getAvailableEventsWithFilters(String text, EventState state, List<Long> categories, Boolean paid,
                                              LocalDateTime rangeStart, LocalDateTime rangeEnd, PageRequest pageRequest);

    @Query("SELECT e " +
            "FROM Event e " +
            "WHERE ((:text IS NULL OR LOWER(e.annotation) like LOWER(concat('%', :text, '%'))) " +
            " OR (:text IS NULL OR LOWER(e.description) like LOWER(concat('%', :text, '%')))) " +
            " AND (:state IS NULL OR e.state = :state) " +
            " AND (:categories IS NULL OR e.category.id in :categories) " +
            " AND (:paid IS NULL OR e.paid = :paid) " +
            " AND (cast(:rangeStart AS java.time.LocalDateTime) IS NULL OR e.eventDate >= :rangeStart) " +
            " AND (cast(:rangeEnd AS java.time.LocalDateTime) IS NULL OR e.eventDate <= :rangeEnd) ")
    List<Event> getAllEventsWithFilters(String text, EventState state, List<Long> categories, Boolean paid,
                                        LocalDateTime rangeStart, LocalDateTime rangeEnd, PageRequest pageRequest);

}