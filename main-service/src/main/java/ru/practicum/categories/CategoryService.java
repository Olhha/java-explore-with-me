package ru.practicum.categories;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.categories.model.Category;
import ru.practicum.exception.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public CategoryDto addCategory(CategoryDto categoryDto) {
        Category category = CategoryMapper.toCategory(categoryDto);
        return CategoryMapper.toCategoryDto(categoryRepository.save(category));
    }

    public void deleteCategory(Long catId) {
        if (categoryRepository.findById(catId).isEmpty()) {
            throw new NotFoundException("Category can't be deleted: Category with id = " + catId + " doesn't exist");
        }
        categoryRepository.deleteById(catId);
    }

    public CategoryDto updateCategory(CategoryDto categoryDto) {
        Category category = getCategoryByIdOrThrow(categoryDto.getId());
        return CategoryMapper.toCategoryDto(categoryRepository.save(category));
    }

    public List<CategoryDto> getAllCategories(int from, int size) {
        Pageable page = PageRequest.of(from / size, size);
        List<Category> categories = categoryRepository.findAll(page).getContent();
        return categories.stream()
                .map(CategoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }

    public CategoryDto getCategoryById(Long catId) {
        return CategoryMapper.toCategoryDto(getCategoryByIdOrThrow(catId));
    }

    private Category getCategoryByIdOrThrow(Long catId) {
        return categoryRepository.findById(catId).orElseThrow(
                () -> new NotFoundException("Category can't be patched: " +
                        "Category with id = " + catId + " doesn't exist")
        );
    }
}
