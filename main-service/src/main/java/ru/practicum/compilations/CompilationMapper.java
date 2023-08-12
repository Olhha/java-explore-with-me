package ru.practicum.compilations;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.compilations.dto.CompilationDto;
import ru.practicum.compilations.dto.NewCompilationDto;
import ru.practicum.compilations.dto.UpdateCompilationRequest;
import ru.practicum.compilations.model.Compilation;
import ru.practicum.events.EventMapper;
import ru.practicum.events.model.Event;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CompilationMapper {
    public static Compilation toCompilation(NewCompilationDto newCompilationDto) {
        return Compilation.builder()
                .title(newCompilationDto.getTitle())
                .pinned(newCompilationDto.getPinned() != null && newCompilationDto.getPinned())
                .build();
    }

    public static CompilationDto compilationDto(Compilation compilation) {
        return CompilationDto.builder()
                .id(compilation.getId())
                .title(compilation.getTitle())
                .pinned(compilation.getPinned())
                .events(EventMapper.toEventSortDtoList(compilation.getEvents()))
                .build();
    }

    public static void toCompilationUpdated(UpdateCompilationRequest updateCompRequest,
                                            Compilation compilation, List<Event> events) {
        if (updateCompRequest.getPinned() != null)
            compilation.setPinned(updateCompRequest.getPinned());

        if (updateCompRequest.getTitle() != null) {
            compilation.setTitle(updateCompRequest.getTitle());
        }

        if (updateCompRequest.getEvents() != null) {
            compilation.setEvents(events);
        }
    }
}
