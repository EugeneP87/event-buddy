package ru.practicum.mainService.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.mainService.category.dto.CategoryDto;
import ru.practicum.mainService.event.EventState;
import ru.practicum.mainService.location.dto.LocationDto;
import ru.practicum.mainService.user.dto.PartialUserDto;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventDto {

    private Long id;
    private String annotation;
    private CategoryDto category;
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    private LocationDto location;
    private Boolean paid;
    private Long participantLimit;
    private Boolean requestModeration;
    private EventState state;
    private String title;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishedOn;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;
    private PartialUserDto initiator;
    private Long confirmedRequests;
    private Long views;

    public EventDto(Long id, String annotation, CategoryDto category, String description, LocalDateTime eventDate,
                    LocationDto location, Boolean paid, Long participantLimit, Boolean requestModeration, EventState state,
                    String title, LocalDateTime publishedOn, LocalDateTime createdOn, PartialUserDto initiator, Long views) {
        this.id = id;
        this.annotation = annotation;
        this.category = category;
        this.description = description;
        this.eventDate = eventDate;
        this.location = location;
        this.paid = paid;
        this.participantLimit = participantLimit;
        this.requestModeration = requestModeration;
        this.state = state;
        this.title = title;
        this.publishedOn = publishedOn;
        this.createdOn = createdOn;
        this.initiator = initiator;
        this.views = views;
    }

}