package ru.practicum.exception;

import lombok.*;

@AllArgsConstructor
@Getter
@Builder
@Setter
class ErrorResponse {
    private final String error;
    private final String message;
    private final String reason;
    private final String status;
    private final String timestamp;

}

