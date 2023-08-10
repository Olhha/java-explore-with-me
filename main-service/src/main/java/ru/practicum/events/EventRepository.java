package ru.practicum.events;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.events.model.Event;
import ru.practicum.events.model.EventStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    Page<Event> findByInitiatorIdOrderByCreatedDesc(Long initiator, Pageable pageable);

    Event findByIdAndInitiatorId(Long eventId, Long initiatorId);

    Event findByIdAndStatus(Long eventId, EventStatus status);

    @Query("SELECT e FROM Event e WHERE (:userIds is null or e.initiator.id IN :userIds) " +
            "and (:states is null or e.status IN :states) " +
            "and (:categories is null or e.category.id IN :categories) " +
            "and (e.eventDate between :rangeStart and :rangeEnd) " +
            "order by e.eventDate desc ")
    List<Event> searchEventByAdmin(@Param("userIds") List<Long> userIds,
                                   @Param("states") List<EventStatus> states,
                                   @Param("categories") List<Long> categories,
                                   @Param("rangeStart") LocalDateTime rangeStart,
                                   @Param("rangeEnd") LocalDateTime rangeEnd,
                                   Pageable page);

    @Query("SELECT e FROM Event e WHERE " +
            "(:text is null or lower(e.annotation) like lower(concat('%', :text,'%')) " +
            "or (:text is null or lower(e.description) like lower(concat('%', :text,'%')))) " +
            "and e.status = :state " +
            "and (:paid is null or e.paid = :paid) " +
            "and (:categories is null or e.category.id IN :categories) " +
            "and (e.eventDate between :rangeStart and :rangeEnd) " +
            "order by e.eventDate desc ")
    List<Event> searchEventPublic(@Param("text") String text,
                                  @Param("categories") List<Long> categories,
                                  @Param("paid") Boolean paid,
                                  @Param("state") EventStatus state,
                                  @Param("rangeStart") LocalDateTime rangeStart,
                                  @Param("rangeEnd") LocalDateTime rangeEnd,
                                  Pageable page);

    List<Event> findByIdIn(List<Long> eventIds);
}
