package ru.practicum.events.model;

public enum EventStateOwnerAction {
    SEND_TO_REVIEW(EventStatus.PENDING),
    CANCEL_REVIEW(EventStatus.CANCELED);
    private final EventStatus eventStatus;

    EventStateOwnerAction(EventStatus eventStatus) {
        this.eventStatus = eventStatus;
    }

    public EventStatus getEventStatus() {
        return eventStatus;
    }
}
