package ru.practicum.categories;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.categories.dto.CategoryDto;

import java.util.List;

@RestController
@RequestMapping("/categories")
@Slf4j
public class CategoryPublicController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryPublicController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping()
    public List<CategoryDto> getAllCategories(
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size) {
        log.info("CategoryPublicController: get all categories from = {} size = {}", from, size);
        return categoryService.getAllCategories(from, size);
    }

    @GetMapping("/{catId}")
    public CategoryDto getEventById(@PathVariable Long catId) {
        log.info("CategoryPublicController: get category by id = {}", catId);
        return categoryService.getCategoryById(catId);
    }
}
