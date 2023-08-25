package ru.practicum.mainService.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mainService.category.Category;
import ru.practicum.mainService.category.repository.CategoryRepository;
import ru.practicum.mainService.event.Event;
import ru.practicum.mainService.event.EventState;
import ru.practicum.mainService.event.EventStateAction;
import ru.practicum.mainService.event.dto.EventDto;
import ru.practicum.mainService.event.dto.NewEventDto;
import ru.practicum.mainService.event.dto.PartialEventDto;
import ru.practicum.mainService.event.dto.UpdateEventRequestDto;
import ru.practicum.mainService.event.mapper.EventMapper;
import ru.practicum.mainService.event.repository.EventRepository;
import ru.practicum.mainService.exception.BadRequestException;
import ru.practicum.mainService.exception.ConflictException;
import ru.practicum.mainService.exception.NotFoundException;
import ru.practicum.mainService.location.Location;
import ru.practicum.mainService.location.dto.LocationDto;
import ru.practicum.mainService.request.Request;
import ru.practicum.mainService.request.RequestStatus;
import ru.practicum.mainService.request.dto.EventRequestStatusUpdate;
import ru.practicum.mainService.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.mainService.request.dto.ParticipationRequestDto;
import ru.practicum.mainService.request.mapper.RequestMapper;
import ru.practicum.mainService.request.repository.RequestRepository;
import ru.practicum.mainService.statistic.StatsServiceImpl;
import ru.practicum.mainService.user.User;
import ru.practicum.mainService.user.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class EventServiceImpl {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final RequestRepository requestRepository;
    private final StatsServiceImpl statisticService;

    public Collection<EventDto> getEvents(List<Long> users, List<String> states,
                                          List<Long> categories, LocalDateTime rangeStart,
                                          LocalDateTime rangeEnd, Integer from, Integer size) {
        List<EventState> eventStates = Optional.ofNullable(states)
                .map(statesList -> statesList.stream()
                        .map(EventState::valueOf)
                        .collect(Collectors.toList()))
                .orElse(null);
        PageRequest page = PageRequest.of(from, size);
        List<Event> events = eventRepository.getEventsWithUsersStatesCategoriesDateTime(
                users, eventStates, categories, rangeStart, rangeEnd, page);
        Map<Long, Long> views = statisticService.getStatsEvents(events);
        List<EventDto> result = events.stream()
                .map(event -> {
                    EventDto eventDto = EventMapper.toEventDto(event);
                    eventDto.setViews(views.get(eventDto.getId()));
                    return eventDto;
                })
                .collect(Collectors.toList());
        updateConfirmedRequests(result);
        return result;
    }

    @Transactional
    public EventDto updateEvent(Long eventId, UpdateEventRequestDto updateEventDto) {
        Event event = getEventById(eventId);
        validateParticipationStatusPending(event.getState());
        validateEditableEventState(event);
        updateEventFields(event, updateEventDto);
        return EventMapper.toEventDto(eventRepository.save(event));
    }

    public List<PartialEventDto> getEventUser(Long userId, Integer from, Integer size) {
        validateUserExists(userId);
        PageRequest page = PageRequest.of(from, size);
        List<Event> events = eventRepository.findAllByInitiatorId(userId, page);
        Map<Long, Long> views = statisticService.getStatsEvents(events);
        return events.stream()
                .map(event -> {
                    PartialEventDto dto = EventMapper.toPartialEventDto(event);
                    dto.setViews(views.getOrDefault(dto.getId(), 0L));
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public EventDto updateEventUser(Long userId, Long eventId, UpdateEventRequestDto updateEventDto) {
        Event event = getEventById(eventId);
        validateUserExists(userId);
        if (event.getState().equals(EventState.PUBLISHED)) {
            throw new ConflictException("Мероприятие уже опубликовано");
        }
        updateEventFields(event, updateEventDto);
        Map<Long, Long> views = statisticService.getStatsEvents(List.of(event));
        EventDto eventDto = EventMapper.toEventDto(eventRepository.save(event));
        eventDto.setViews(views.getOrDefault(event.getId(), 0L));
        updateConfirmedRequests(List.of(eventDto));
        return eventDto;
    }

    private void updateEventFields(Event event, UpdateEventRequestDto updateEventDto) {
        if (updateEventDto.getEventDate() != null) {
            validateEventDate(updateEventDto.getEventDate());
            event.setEventDate(updateEventDto.getEventDate());
        }
        if (updateEventDto.getEventDate() != null) {
            validateEventDateForAdmin(updateEventDto.getEventDate());
            event.setEventDate(updateEventDto.getEventDate());
        }
        if (updateEventDto.getAnnotation() != null && !updateEventDto.getAnnotation().isBlank()) {
            event.setAnnotation(updateEventDto.getAnnotation());
        }
        if (updateEventDto.getDescription() != null && !updateEventDto.getDescription().isBlank()) {
            event.setDescription(updateEventDto.getDescription());
        }
        if (updateEventDto.getLocation() != null) {
            Location location = event.getLocation();
            LocationDto locationDto = updateEventDto.getLocation();
            Location newLocation = new Location(locationDto.getLat(), locationDto.getLon());
            location.setLat(newLocation.getLat());
            location.setLon(newLocation.getLon());
        }
        if (updateEventDto.getPaid() != null) {
            event.setPaid(updateEventDto.getPaid());
        }
        if (updateEventDto.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventDto.getParticipantLimit());
        }
        if (updateEventDto.getRequestModeration() != null) {
            event.setRequestModeration(updateEventDto.getRequestModeration());
        }
        if (updateEventDto.getTitle() != null && !updateEventDto.getTitle().isBlank()) {
            event.setTitle(updateEventDto.getTitle());
        }
        if (updateEventDto.getCategory() != null) {
            Category category = getCategoryById(updateEventDto.getCategory());
            event.setCategory(category);
        }
        if (updateEventDto.getStateAction() != null) {
            updateEventState(event, updateEventDto.getStateAction());
        }
    }

    public EventDto getCompleteEvent(Long id, HttpServletRequest request) {
        Event event = getEventById(id);
        validateEventStatePublished(event);
        statisticService.addView(request);
        Map<Long, Long> views = statisticService.getStatsEvents(List.of(event));
        EventDto eventDto = EventMapper.toEventDto(event);
        eventDto.setViews(views.getOrDefault(event.getId(), 0L));
        updateConfirmedRequests(List.of(eventDto));
        return eventDto;
    }

    public Collection<EventDto> getAllEvents(String text, List<Long> categories, Boolean paid,
                                             LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                             boolean onlyAvailable, String sort, Integer from, Integer size, HttpServletRequest request) {
        validateDateRange(rangeStart, rangeEnd);
        List<Event> events;
        PageRequest pageRequest = PageRequest.of(from, size);
        if (onlyAvailable) {
            events = eventRepository.getAvailableEventsWithFilters(
                    text, EventState.PUBLISHED, categories, paid, rangeStart, rangeEnd, pageRequest);
            statisticService.addView(request);
        } else {
            events = eventRepository.getAllEventsWithFilters(
                    text, EventState.PUBLISHED, categories, paid, rangeStart, rangeEnd, pageRequest);
            statisticService.addView(request);
        }
        Map<Long, Long> view = statisticService.getStatsEvents(events);
        List<EventDto> result = events.stream()
                .map(EventMapper::toEventDto)
                .peek(e -> e.setViews(view.get(e.getId())))
                .collect(Collectors.toList());
        if ("VIEWS".equals(sort)) {
            result.sort(Comparator.comparing(EventDto::getViews));
        }
        return result;
    }

    private void validateDateRange(LocalDateTime rangeStart, LocalDateTime rangeEnd) {
        if (rangeStart != null && rangeEnd != null && (rangeEnd.isBefore(LocalDateTime.now()) || rangeStart.isAfter(rangeEnd))) {
            throw new BadRequestException("Завершение события не может быть позднее настоящего времени, " +
                    "а начало события не может предшествовать окончанию");
        }
    }

    @Transactional
    public EventDto addEventUser(Long userId, NewEventDto newEventDto) {
        validateEventDate(newEventDto.getEventDate());
        User user = getUserById(userId);
        Category category = getCategoryById(newEventDto.getCategory());
        Event event = eventRepository.save(EventMapper.toEvent(newEventDto, category, user));
        Map<Long, Long> views = statisticService.getStatsEvents(List.of(event));
        EventDto eventDto = EventMapper.toEventDto(event);
        eventDto.setViews(views.getOrDefault(event.getId(), 0L));
        updateConfirmedRequests(List.of(eventDto));
        return eventDto;
    }

    public EventDto getUserEventCompleteInfo(Long userId, Long eventId) {
        validateUserExists(userId);
        Event event = getEventById(eventId);
        Map<Long, Long> views = statisticService.getStatsEvents(List.of(event));
        EventDto eventDto = EventMapper.toEventDto(event);
        eventDto.setViews(views.getOrDefault(event.getId(), 0L));
        updateConfirmedRequests(List.of(eventDto));
        return eventDto;
    }

    public List<ParticipationRequestDto> getAllRequestsByEventId(Long userId, Long eventId) {
        validateUserExists(userId);
        return requestRepository.findAllByEventId(eventId)
                .stream()
                .map(RequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public EventRequestStatusUpdateResult changeStatusRequest(Long userId, Long eventId, EventRequestStatusUpdate eventRequestStatusUpdate) {
        validateUserExists(userId);
        Event event = getEventById(eventId);
        List<Request> requestsEvent = requestRepository.findAllByEventId(eventId);
        Long confirmedRequestCount = requestRepository.countByEventIdAndStatus(eventId, RequestStatus.CONFIRMED);
        if (event.getParticipantLimit() <= confirmedRequestCount) {
            throw new ConflictException("Количество участников мероприятия превышено");
        }
        EventRequestStatusUpdateResult eventRequestStatusUpdateResult = new EventRequestStatusUpdateResult(new ArrayList<>(), new ArrayList<>());
        List<Request> requestListUpdateStatus = new ArrayList<>();
        for (Request request : requestsEvent) {
            if (eventRequestStatusUpdate.getRequestIds().contains(request.getId()) && request.getStatus() != RequestStatus.CANCELED) {
                request.setStatus(eventRequestStatusUpdate.getStatus());
                requestListUpdateStatus.add(request);
                updateEventResult(eventRequestStatusUpdateResult, request);
            }
        }
        requestRepository.saveAll(requestListUpdateStatus);
        return eventRequestStatusUpdateResult;
    }

    private void updateEventResult(EventRequestStatusUpdateResult eventRequestStatusUpdateResult, Request request) {
        List<ParticipationRequestDto> resultContainer;
        if (request.getStatus().equals(RequestStatus.CONFIRMED)) {
            resultContainer = eventRequestStatusUpdateResult.getConfirmedRequests();
        } else if (request.getStatus().equals(RequestStatus.REJECTED)) {
            resultContainer = eventRequestStatusUpdateResult.getRejectedRequests();
        } else {
            return;
        }
        resultContainer.add(RequestMapper.toParticipationRequestDto(request));
    }

    private void updateEventState(Event event, EventStateAction stateAction) {
        EventState newState;
        switch (stateAction) {
            case CANCEL_REVIEW:
            case REJECT_EVENT:
                newState = EventState.CANCELED;
                break;
            case SEND_TO_REVIEW:
                newState = EventState.PENDING;
                break;
            case PUBLISH_EVENT:
                newState = EventState.PUBLISHED;
                break;
            default:
                throw new BadRequestException("Некорректный статус");
        }
        event.setState(newState);
        if (stateAction == EventStateAction.PUBLISH_EVENT) {
            event.setPublishedOn(LocalDateTime.now());
        }
    }

    private Event getEventById(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Событие не найдено " + eventId));
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден " + userId));
    }

    private Category getCategoryById(Long catId) {
        return categoryRepository.findById(catId).orElseThrow(() -> new NotFoundException("Категория не найдена " + catId));
    }

    private void validateUserExists(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь не найден " + userId);
        }
    }

    private void validateEventDateForAdmin(LocalDateTime eventDate) {
        if (LocalDateTime.now().plusHours(1).isAfter(eventDate)) {
            throw new BadRequestException("Некорректно задано время мероприятия");
        }
    }

    private void validateEventDate(LocalDateTime eventDate) {
        if (LocalDateTime.now().plusHours(2).isAfter(eventDate)) {
            throw new BadRequestException("Некорректно задано время мероприятия");
        }
    }

    private void validateParticipationStatusPending(EventState state) {
        if (!state.equals(EventState.PENDING)) {
            throw new ConflictException("Ошибка статуса");
        }
    }

    private static void validateEventStatePublished(Event event) {
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new NotFoundException("Событие еще не опубликовано");
        }
    }

    private void validateEditableEventState(Event event) {
        if (event.getState().equals(EventState.PUBLISHED) || event.getState().equals(EventState.CANCELED)) {
            throw new ConflictException("Нельзя редактировать опубликованное или отклоненное событие");
        }
    }

    private void updateConfirmedRequests(List<EventDto> eventDtos) {
        List<Long> eventIds = eventDtos.stream()
                .map(EventDto::getId)
                .collect(Collectors.toList());
        List<Request> confirmedRequests = requestRepository.findAllByEventIdInAndStatus(eventIds, RequestStatus.CONFIRMED);
        Map<Long, Long> eventIdToConfirmedCount = confirmedRequests.stream()
                .collect(Collectors.groupingBy(
                        request -> request.getEvent().getId(),
                        Collectors.counting()
                ));
        for (EventDto eventDto : eventDtos) {
            Long confirmedCount = eventIdToConfirmedCount.getOrDefault(eventDto.getId(), 0L);
            eventDto.setConfirmedRequests(confirmedCount);
        }
    }

}