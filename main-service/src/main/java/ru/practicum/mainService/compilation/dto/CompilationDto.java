package ru.practicum.mainService.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.mainService.event.dto.PartialEventDto;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompilationDto {

    private Long id;
    private Boolean pinned;
    private String title;
    private List<PartialEventDto> events;

}