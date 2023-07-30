package ru.practicum.categories.dto;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotBlank;

@Value
@Builder(toBuilder = true)
public class CategoryDto {

    Long id;

    @NotBlank(message = "CategoryDto: category name can't be empty")
    String name;
}

