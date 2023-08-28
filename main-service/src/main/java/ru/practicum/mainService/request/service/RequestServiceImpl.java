package ru.practicum.mainService.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mainService.event.Event;
import ru.practicum.mainService.event.EventState;
import ru.practicum.mainService.event.repository.EventRepository;
import ru.practicum.mainService.exception.ConflictException;
import ru.practicum.mainService.exception.NotFoundException;
import ru.practicum.mainService.request.Request;
import ru.practicum.mainService.request.RequestStatus;
import ru.practicum.mainService.request.dto.ParticipationRequestDto;
import ru.practicum.mainService.request.mapper.RequestMapper;
import ru.practicum.mainService.request.repository.RequestRepository;
import ru.practicum.mainService.user.User;
import ru.practicum.mainService.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class RequestServiceImpl {

    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;

    @Transactional
    public ParticipationRequestDto addUserRequest(Long userId, Long eventId) {
        User requester = getUserById(userId);
        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException("Событие с ID " + eventId + " не найдено"));
        validateRequestAddition(userId, event);
        Request request = buildRequest(requester, event);
        return RequestMapper.toParticipationRequestDto(requestRepository.save(request));
    }

    @Transactional
    public List<ParticipationRequestDto> getUserRequests(Long userId) {
        getUserById(userId);
        return requestRepository.findAllByRequesterId(userId)
                .stream()
                .map(RequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public ParticipationRequestDto cancelUserRequest(Long userId, Long requestId) {
        getUserById(userId);
        Request request = requestRepository.findById(requestId).orElseThrow(
                () -> new NotFoundException("Запрос с ID " + requestId + " не найден"));
        request.setStatus(RequestStatus.CANCELED);
        return RequestMapper.toParticipationRequestDto(requestRepository.save(request));
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь с ID " + userId + " не найден"));
    }

    private Request buildRequest(User requester, Event event) {
        RequestStatus status = event.getRequestModeration() && event.getParticipantLimit() > 0
                ? RequestStatus.PENDING
                : RequestStatus.CONFIRMED;
        return new Request(LocalDateTime.now(), event, requester, status);
    }

    private void validateRequestAddition(Long userId, Event event) {
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new ConflictException("Событие еще не было опубликовано");
        }
        if (userId.equals(event.getInitiator().getId())) {
            throw new ConflictException("Инициатор события не может создавать запросы на свое событие");
        }
        if (requestRepository.existsByRequesterIdAndEventId(userId, event.getId())) {
            throw new ConflictException("Запрос участника с ID " + userId + "на событие с ID" + event.getId() + " уже существует");
        }
        if (event.getParticipantLimit() > 0 && event.getParticipantLimit()
                <= requestRepository.countByEventIdAndStatus(event.getId(), RequestStatus.CONFIRMED)) {
            throw new ConflictException("Достигнут лимит участников события с ID " + event.getId());
        }
    }

}