package ru.practicum.mainService.compilation.mapper;

import ru.practicum.mainService.compilation.Compilation;
import ru.practicum.mainService.compilation.dto.CompilationDto;
import ru.practicum.mainService.compilation.dto.NewCompilationDto;
import ru.practicum.mainService.event.Event;
import ru.practicum.mainService.event.dto.PartialEventDto;
import ru.practicum.mainService.event.mapper.EventMapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public final class CompilationMapper {

    private CompilationMapper() {
    }

    public static Compilation toCompilation(NewCompilationDto newCompilationDto, Set<Event> events) {
        return new Compilation(
                newCompilationDto.isPinned(),
                newCompilationDto.getTitle(),
                events
        );
    }

    public static CompilationDto toCompilationDto(Compilation compilation) {
        return new CompilationDto(
                compilation.getId(),
                compilation.getPinned(),
                compilation.getTitle(),
                compilation.getEvents().stream()
                        .map(EventMapper::toPartialEventDto)
                        .collect(Collectors.toList())
        );
    }

    public static CompilationDto toCompilationDto(Compilation compilation, List<PartialEventDto> partialEventDtos) {
        return new CompilationDto(
                compilation.getId(),
                compilation.getPinned(),
                compilation.getTitle(),
                partialEventDtos
        );
    }

}