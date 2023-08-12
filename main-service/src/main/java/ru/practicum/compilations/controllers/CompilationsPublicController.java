package ru.practicum.compilations.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilations.CompilationService;
import ru.practicum.compilations.dto.CompilationDto;

import java.util.List;

@RestController
@RequestMapping("/compilations")
@Slf4j
@RequiredArgsConstructor
public class CompilationsPublicController {
    private final CompilationService compilationService;

    @GetMapping()
    public List<CompilationDto> getAllCompilations(
            @RequestParam(required = false) Boolean pinned,
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size) {
        log.info("CompilationsPublicController: get all compilations pinned={} from = {} size = {}",
                pinned, from, size);
        return compilationService.getAllCompilationsPublic(pinned, from, size);
    }

    @GetMapping("/{compId}")
    public CompilationDto getEventById(@PathVariable Long compId) {
        log.info("CompilationsPublicController: get compilation by id = {}", compId);
        return compilationService.getCompilationById(compId);
    }
}
