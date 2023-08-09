package ru.practicum.compilations;

import org.springframework.beans.factory.annotation.Autowired;
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
public class CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Autowired
    public CompilationService(CompilationRepository compilationRepository, EventRepository eventRepository) {
        this.compilationRepository = compilationRepository;
        this.eventRepository = eventRepository;
    }

    public CompilationDto addCompilation(NewCompilationDto newCompilationDto) {
        Compilation compilation = CompilationMapper.toCompilation(newCompilationDto);
        List<Event> events = getEventsByIds(newCompilationDto.getEvents());
        compilation.setEvents(events);
        return CompilationMapper.compilationDto(compilationRepository.save(compilation));
    }

    public CompilationDto updateCompilation(Long compId, UpdateCompilationRequest updateCompRequest) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() ->
                new NotFoundException("CompilationService: compilation Not Found, compId=" + compId));

        if (updateCompRequest.getPinned() != null)
            compilation.setPinned(updateCompRequest.getPinned());

        if (updateCompRequest.getTitle() != null) {
            compilation.setTitle(updateCompRequest.getTitle());
        }

        if (updateCompRequest.getEvents() != null) {
            compilation.setEvents(getEventsByIds(updateCompRequest.getEvents()));
        }

        return CompilationMapper.compilationDto(compilationRepository.save(compilation));
    }

    public void deleteCompilation(Long compId) {
        getCompilationOrThrow(compId);
        compilationRepository.deleteById(compId);
    }

    public List<CompilationDto> getAllCompilationsPublic(Boolean pinned, Integer from, Integer size) {
        List<Compilation> compilations = compilationRepository.findAllPinned(pinned,
                getPage(from, size)).getContent();

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
