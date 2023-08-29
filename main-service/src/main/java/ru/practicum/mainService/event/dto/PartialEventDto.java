package ru.practicum.mainService.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.mainService.category.dto.CategoryDto;
import ru.practicum.mainService.common.DateAndTimeFormatter;
import ru.practicum.mainService.user.dto.PartialUserDto;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PartialEventDto {

    private Long id;
    private String annotation;
    private CategoryDto category;
    private Long confirmedRequests;
    @JsonFormat(pattern = DateAndTimeFormatter.DATE_TIME_PATTERN)
    private LocalDateTime eventDate;
    private PartialUserDto initiator;
    private Boolean paid;
    private String title;
    private Long views;

    public PartialEventDto(Long id, String annotation, CategoryDto category, LocalDateTime eventDate, PartialUserDto initiator, Boolean paid, String title, Long views) {
        this.id = id;
        this.annotation = annotation;
        this.category = category;
        this.eventDate = eventDate;
        this.initiator = initiator;
        this.paid = paid;
        this.title = title;
        this.views = views;
    }

}