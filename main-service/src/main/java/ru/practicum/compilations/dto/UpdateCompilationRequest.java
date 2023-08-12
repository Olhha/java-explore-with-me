package ru.practicum.compilations.dto;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.Size;
import java.util.List;

@Value
@Builder(toBuilder = true)
public class UpdateCompilationRequest {
    @Size(min = 1, max = 50, message = "UpdateCompilationRequest: title length should be from {min} to {max}")
    String title;
    Boolean pinned;
    List<Long> events;
}
