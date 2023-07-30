package ru.practicum.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.time.format.DateTimeParseException;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(final MethodArgumentNotValidException e) {
        log.debug("Получен статус 400 Bad request: {}", e.getMessage(), e);
        return new ErrorResponse(
                "Произошла ошибка валидации запроса."
                        + e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleCustomValidationException(final ValidationException e) {
        log.debug("Validation Exception, статус 400 Bad request: {}", e.getMessage(), e);
        return new ErrorResponse(e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleCustomValidationException(final ConstraintViolationException e) {
        log.debug("Validation Exception, статус 400 Bad request: {}", e.getMessage(), e);
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleDateTimeParseException(final DateTimeParseException e) {
        log.debug("Validation Exception, статус 400 Bad request: {}", e.getMessage(), e);
        return new ErrorResponse("Формат даты должен быть: yyyy-MM-dd HH:mm:ss ." + e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleThrowable(final Throwable e) {
        log.debug("Получен статус 500 Internal Server Error: {}", e.getMessage(), e);
        return new ErrorResponse(e.getMessage());
    }
}
