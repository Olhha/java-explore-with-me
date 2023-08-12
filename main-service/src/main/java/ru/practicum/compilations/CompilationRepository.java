package ru.practicum.compilations;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.compilations.model.Compilation;

import java.util.List;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {

    @Query("SELECT c FROM Compilation c WHERE " +
            "(:pinned is null or c.pinned = :pinned) " +
            "order by c.id asc ")
    List<Compilation> findAllPinned(@Param("pinned") Boolean pinned, Pageable page);
}
