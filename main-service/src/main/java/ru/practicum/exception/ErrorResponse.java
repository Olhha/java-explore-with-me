package ru.practicum.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
class ErrorResponse {
    private final String error;
    private final String message;
    private final String reason;
    private final String status;
    private final String timestamp;

}

