package ru.practicum.mainService.event.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.mainService.category.Category;
import ru.practicum.mainService.category.mapper.CategoryMapper;
import ru.practicum.mainService.event.Event;
import ru.practicum.mainService.event.EventState;
import ru.practicum.mainService.event.dto.EventDto;
import ru.practicum.mainService.event.dto.NewEventDto;
import ru.practicum.mainService.event.dto.PartialEventDto;
import ru.practicum.mainService.location.mapper.LocationMapper;
import ru.practicum.mainService.user.User;
import ru.practicum.mainService.user.mapper.UserMapper;

import java.time.LocalDateTime;

@UtilityClass
public final class EventMapper {

    public EventDto toEventDto(Event event) {
        return new EventDto(
                event.getId(),
                event.getAnnotation(),
                CategoryMapper.toCategoryDto(event.getCategory()),
                event.getDescription(),
                event.getEventDate(),
                LocationMapper.toLocationDto(event.getLocation()),
                event.isPaid(),
                event.getParticipantLimit(),
                event.getRequestModeration(),
                event.getState(),
                event.getTitle(),
                event.getCreatedOn(),
                (event.getPublishedOn() != null) ? event.getPublishedOn() : null,
                UserMapper.toPartialUserDto(event.getInitiator()),
                0L
        );
    }

    public Event toEvent(NewEventDto newEventDto, Category category, User user) {
        return new Event(
                newEventDto.getAnnotation(),
                category,
                LocalDateTime.now(),
                newEventDto.getDescription(),
                newEventDto.getEventDate(),
                user,
                LocationMapper.toLocation(newEventDto.getLocation()),
                newEventDto.isPaid(),
                newEventDto.getParticipantLimit(),
                newEventDto.isRequestModeration(),
                EventState.PENDING,
                newEventDto.getTitle()
        );
    }

    public PartialEventDto toPartialEventDto(Event event) {
        return new PartialEventDto(
                event.getId(),
                event.getAnnotation(),
                CategoryMapper.toCategoryDto(event.getCategory()),
                event.getEventDate(),
                UserMapper.toPartialUserDto(event.getInitiator()),
                event.isPaid(),
                event.getTitle(),
                0L
        );
    }

}