package ru.practicum.events.model;

import lombok.*;
import ru.practicum.categories.model.Category;
import ru.practicum.users.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "events")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String annotation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", referencedColumnName = "id", nullable = false)
    @ToString.Exclude
    private Category category;

    @Column(name = "event_date")
    private LocalDateTime eventDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "initiator_id", referencedColumnName = "id", nullable = false)
    @ToString.Exclude
    private User initiator;

    private double lat;

    private double lon;

    private boolean paid;

    private String title;

    @Column(name = "confirmed_requests")
    private long confirmedRequests;

    private int views;

    private String description;

    @Column(name = "participant_limit")
    private int participantLimit;

    private LocalDateTime published;

    private LocalDateTime created;

    @Column(name = "request_moderation")
    private boolean requestModeration;

    @Enumerated(EnumType.STRING)
    private EventStatus status;
}