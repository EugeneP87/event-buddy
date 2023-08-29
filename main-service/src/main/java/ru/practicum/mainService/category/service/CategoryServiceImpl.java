package ru.practicum.mainService.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mainService.category.Category;
import ru.practicum.mainService.category.dto.CategoryDto;
import ru.practicum.mainService.category.dto.NewCategoryDto;
import ru.practicum.mainService.category.mapper.CategoryMapper;
import ru.practicum.mainService.category.repository.CategoryRepository;
import ru.practicum.mainService.event.repository.EventRepository;
import ru.practicum.mainService.exception.ConflictException;
import ru.practicum.mainService.exception.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class CategoryServiceImpl {

    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Transactional
    public CategoryDto addCategory(NewCategoryDto newCategoryDto) {
        Category newCategory = CategoryMapper.toCategory(newCategoryDto);
        return CategoryMapper.toCategoryDto(categoryRepository.save(newCategory));
    }

    @Transactional
    public CategoryDto updateCategory(NewCategoryDto categoryDto, Long catId) {
        Category existingCategory = getCategoryById(catId);
        Category updatedCategory = updateCategoryFromDto(existingCategory, categoryDto);
        return CategoryMapper.toCategoryDto(categoryRepository.save(updatedCategory));
    }

    @Transactional
    public void deleteCategory(Long catId) {
        Category category = getCategoryById(catId);
        if (eventRepository.existsByCategory(category)) {
            throw new ConflictException("Удаление категории невозможно, т.к. имеются связанные с ней события");
        }
        categoryRepository.delete(category);
    }

    public List<CategoryDto> getAll(PageRequest pageRequest) {
        return categoryRepository.findAll(pageRequest)
                .stream()
                .map(CategoryMapper::toCategoryDto)
                .collect(Collectors.toUnmodifiableList());
    }

    public CategoryDto getCategoryDtoById(Long catId) {
        Category category = getCategoryById(catId);
        return CategoryMapper.toCategoryDto(category);
    }

    private Category getCategoryById(Long catId) {
        return categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Категория не найдена " + catId));
    }

    private Category updateCategoryFromDto(Category category, NewCategoryDto categoryDto) {
        category.setName(categoryDto.getName());
        return category;
    }

}