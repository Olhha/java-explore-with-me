package ru.practicum.requests.model;

import lombok.*;
import ru.practicum.events.model.Event;
import ru.practicum.users.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "requests")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", referencedColumnName = "id")
    @ToString.Exclude
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id", referencedColumnName = "id")
    @ToString.Exclude
    private User requester;

    private LocalDateTime created;
}
