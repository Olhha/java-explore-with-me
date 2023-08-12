package ru.practicum.categories.dto;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Value
@Builder(toBuilder = true)
public class CategoryDto {

    Long id;

    @Size(min = 1, max = 50, message = "CategoryDto: name length should be from {min} to {max}")
    @NotBlank(message = "CategoryDto: category name can't be empty")
    String name;
}

