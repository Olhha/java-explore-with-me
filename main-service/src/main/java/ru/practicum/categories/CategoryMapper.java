package ru.practicum.categories;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.categories.model.Category;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CategoryMapper {
    public static CategoryDto toCategoryDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    public static Category toCategory(CategoryDto categoryDto) {
        return Category.builder()
                .name(categoryDto.getName())
                .build();
    }
}
