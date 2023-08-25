package ru.practicum.mainService.category.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainService.category.dto.CategoryDto;
import ru.practicum.mainService.category.dto.NewCategoryDto;
import ru.practicum.mainService.category.service.CategoryServiceImpl;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/categories")
public class AdminCategoryController {

    private final CategoryServiceImpl categoryServiceImpl;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto addCategory(@Valid @RequestBody NewCategoryDto newCategoryDto) {
        log.info("Создание новой категории");
        return categoryServiceImpl.addCategory(newCategoryDto);
    }

    @PatchMapping("{catId}")
    public CategoryDto updateCategory(@Valid @RequestBody NewCategoryDto categoryDto, @PathVariable Long catId) {
        log.info("Изменение категории с ID {}", catId);
        return categoryServiceImpl.updateCategory(categoryDto, catId);
    }

    @DeleteMapping("{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long catId) {
        log.info("Удаление категории с ID {}", catId);
        categoryServiceImpl.deleteCategory(catId);
    }

}