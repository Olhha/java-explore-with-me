package ru.practicum.compilations.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilations.CompilationService;
import ru.practicum.compilations.dto.CompilationDto;
import ru.practicum.compilations.dto.NewCompilationDto;
import ru.practicum.compilations.dto.UpdateCompilationRequest;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin/compilations")
@Slf4j
@RequiredArgsConstructor
public class CompilationsAdminController {
    private final CompilationService compilationService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto addCompilation(@Valid @RequestBody NewCompilationDto newCompilationDto) {
        log.info("CompilationsAdminController: Creating new Compilation by Admin");
        return compilationService.addCompilation(newCompilationDto);
    }

    @PatchMapping("/{compId}")
    public CompilationDto cancelRequest(@PathVariable Long compId,
                                        @Valid @RequestBody UpdateCompilationRequest updateCompilationRequest) {
        log.info("CompilationsAdminController: update Compilation id={} by Admin", compId);
        return compilationService.updateCompilation(compId, updateCompilationRequest);
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable Long compId) {
        compilationService.deleteCompilation(compId);
    }
}
