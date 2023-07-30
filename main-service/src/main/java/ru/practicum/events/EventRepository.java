package ru.practicum.events;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.events.model.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
}
