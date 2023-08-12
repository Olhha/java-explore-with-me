package ru.practicum.events.model;

public enum EventStateAdminAction {
    PUBLISH_EVENT(EventStatus.PUBLISHED),
    REJECT_EVENT(EventStatus.CANCELED);

    private final EventStatus eventStatus;

    EventStateAdminAction(EventStatus eventStatus) {
        this.eventStatus = eventStatus;
    }

    public EventStatus getEventStatus() {
        return eventStatus;
    }
}
