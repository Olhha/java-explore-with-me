package ru.practicum.compilations.dto;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Value
@Builder(toBuilder = true)
public class NewCompilationDto {
    @NotBlank(message = "NewCompilationDto: title can't be blank")
    @Size(min = 1, max = 50, message = "NewCompilationDto: title length should be from {min} to {max}")
    String title;

    List<Long> events;
    Boolean pinned;
}
