package ru.practicum.categories;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.categories.dto.CategoryDto;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin/categories")
@Slf4j
public class CategoryAdminController {
    private final CategoryService categoryService;

    @Autowired
    public CategoryAdminController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto addUser(@Valid @RequestBody CategoryDto categoryDto) {
        log.info("CategoryAdminController: Creating category {}", categoryDto);
        return categoryService.addCategory(categoryDto);
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long catId) {
        log.info("CategoryAdminController: Deleting category with ID = {}", catId);
        categoryService.deleteCategory(catId);
    }

    @PatchMapping("/{catId}")
    public CategoryDto updateCategory(@PathVariable Long catId, @Valid @RequestBody CategoryDto categoryDto) {
        log.info("CategoryAdminController: Patching category with ID = {}", catId);
        return categoryService.updateCategory(categoryDto.toBuilder().id(catId).build());
    }


}
