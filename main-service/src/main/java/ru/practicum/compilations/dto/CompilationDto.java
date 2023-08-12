package ru.practicum.compilations.dto;

import lombok.Builder;
import lombok.Value;
import ru.practicum.events.dto.EventShortDto;

import java.util.List;

@Value
@Builder(toBuilder = true)
public class CompilationDto {
    Long id;
    String title;
    List<EventShortDto> events;
    Boolean pinned;
}
