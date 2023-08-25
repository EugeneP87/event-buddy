package ru.practicum.mainService.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainService.compilation.dto.CompilationDto;
import ru.practicum.mainService.compilation.dto.NewCompilationDto;
import ru.practicum.mainService.compilation.dto.UpdateCompilationDto;
import ru.practicum.mainService.compilation.service.CompilationServiceImpl;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "admin/compilations")
public class AdminCompilationController {

    private final CompilationServiceImpl compilationServiceImpl;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto addCompilation(@Valid @RequestBody NewCompilationDto newCompilationDto) {
        log.info("Добавление новой подборки");
        return compilationServiceImpl.addCompilation(newCompilationDto);
    }

    @PatchMapping("{compId}")
    public CompilationDto updateCompilation(@PathVariable @Positive Long compId,
                                            @Valid @RequestBody UpdateCompilationDto updateCompilationDto) {
        log.info("Обновление информации о подборке с ID {}", compId);
        return compilationServiceImpl.updateCompilation(compId, updateCompilationDto);
    }

    @DeleteMapping("{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable @Positive Long compId) {
        log.info("Удаление подборки с ID {}", compId);
        compilationServiceImpl.deleteCompById(compId);
    }

}