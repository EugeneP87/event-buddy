package ru.practicum.mainService.compilation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.mainService.compilation.Compilation;
import ru.practicum.mainService.compilation.dto.CompilationDto;
import ru.practicum.mainService.compilation.dto.NewCompilationDto;
import ru.practicum.mainService.compilation.dto.UpdateCompilationDto;
import ru.practicum.mainService.compilation.mapper.CompilationMapper;
import ru.practicum.mainService.compilation.repository.CompilationRepository;
import ru.practicum.mainService.event.Event;
import ru.practicum.mainService.event.dto.PartialEventDto;
import ru.practicum.mainService.event.mapper.EventMapper;
import ru.practicum.mainService.event.repository.EventRepository;
import ru.practicum.mainService.exception.NotFoundException;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CompilationServiceImpl {

    private final EventRepository eventRepository;
    private final CompilationRepository compilationRepository;

    public CompilationDto addCompilation(NewCompilationDto compilationDto) {
        Set<Event> events = new HashSet<>();
        Set<Long> eventIds = compilationDto.getEvents();
        if (eventIds != null) {
            events = eventRepository.findAllByIdIn(eventIds);
        }
        Compilation compilation = compilationRepository.save(CompilationMapper.toCompilation(compilationDto, events));
        List<PartialEventDto> eventsShort = events.stream()
                .map(EventMapper::toPartialEventDto)
                .collect(Collectors.toList());
        return CompilationMapper.toCompilationDto(compilation, eventsShort);
    }

    public CompilationDto updateCompilation(Long compId, UpdateCompilationDto updateComp) {
        Compilation compilation = findCompilationById(compId);
        applyCompilationUpdates(compilation, updateComp);
        return CompilationMapper.toCompilationDto(compilationRepository.save(compilation));
    }

    public void deleteCompById(Long compId) {
        Compilation compilation = findCompilationById(compId);
        compilationRepository.delete(compilation);
    }

    public CompilationDto getCompilationById(Long compId) {
        Compilation compilation = findCompilationById(compId);
        return CompilationMapper.toCompilationDto(compilation);
    }

    public List<CompilationDto> getAllCompilations(Boolean pinned, Integer from, Integer size) {
        PageRequest pageRequest = PageRequest.of(from / size, size, Sort.by(Sort.Direction.ASC, "id"));
        List<Compilation> compilations = pinned == null
                ? compilationRepository.findAll(pageRequest).toList()
                : compilationRepository.findAllByPinned(pinned, pageRequest);
        return compilations.stream()
                .map(CompilationMapper::toCompilationDto)
                .collect(Collectors.toList());
    }

    private Compilation findCompilationById(Long compId) {
        return compilationRepository.findById(compId).orElseThrow(() -> new NotFoundException("Подборка не найдена с ID: " + compId));
    }

    private void applyCompilationUpdates(Compilation compilation, UpdateCompilationDto updateComp) {
        if (updateComp.getTitle() != null && !updateComp.getTitle().isBlank()) {
            compilation.setTitle(updateComp.getTitle());
        }
        if (updateComp.getPinned() != null) {
            compilation.setPinned(updateComp.getPinned());
        }
        if (updateComp.getEvents() != null) {
            Set<Event> updatedEvents = eventRepository.findAllByIdIn(updateComp.getEvents());
            compilation.setEvents(Objects.requireNonNull(updatedEvents));
        }
    }

}