package ru.practicum.compilations;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.compilations.dto.CompilationDto;
import ru.practicum.compilations.dto.NewCompilationDto;
import ru.practicum.compilations.dto.UpdateCompilationRequest;
import ru.practicum.compilations.model.Compilation;
import ru.practicum.events.EventRepository;
import ru.practicum.events.model.Event;
import ru.practicum.exception.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.util.PageCreator.getPage;

@Service
@Transactional
@RequiredArgsConstructor
public class CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    public CompilationDto addCompilation(NewCompilationDto newCompilationDto) {
        Compilation compilation = CompilationMapper.toCompilation(newCompilationDto);
        List<Event> events = getEventsByIds(newCompilationDto.getEvents());
        compilation.setEvents(events);
        return CompilationMapper.compilationDto(compilationRepository.save(compilation));
    }

    public CompilationDto updateCompilation(Long compId, UpdateCompilationRequest updateCompRequest) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() ->
                new NotFoundException("CompilationService: compilation Not Found, compId=" + compId));

        List<Event> events = getEventsByIds(updateCompRequest.getEvents());
        CompilationMapper.toCompilationUpdated(updateCompRequest, compilation, events);
        return CompilationMapper.compilationDto(compilationRepository.save(compilation));
    }


    public void deleteCompilation(Long compId) {
        getCompilationOrThrow(compId);
        compilationRepository.deleteById(compId);
    }

    public List<CompilationDto> getAllCompilationsPublic(Boolean pinned, Integer from, Integer size) {
        List<Compilation> compilations = compilationRepository.findAllPinned(pinned,
                getPage(from, size));

        return compilations.stream()
                .map(CompilationMapper::compilationDto)
                .collect(Collectors.toList());
    }

    public CompilationDto getCompilationById(Long compId) {
        Compilation compilation = getCompilationOrThrow(compId);
        return CompilationMapper.compilationDto(compilation);
    }

    private Compilation getCompilationOrThrow(Long compId) {
        return compilationRepository.findById(compId).orElseThrow(() ->
                new NotFoundException("Compilation not found, compId=" + compId));
    }

    private List<Event> getEventsByIds(List<Long> eventIds) {
        if (eventIds != null && !eventIds.isEmpty()) {
            return eventRepository.findByIdIn(eventIds);
        }
        return List.of();
    }
}
