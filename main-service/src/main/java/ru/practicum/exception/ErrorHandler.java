package ru.practicum.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import static ru.practicum.util.DateTimePattern.DATE_TIME_PATTERN;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(final MethodArgumentNotValidException e) {
        log.debug("ValidationException, status 400 Bad request: {}", e.getMessage(), e);
        return getErrorResponse(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(final ValidationException e) {
        log.debug("ValidationException, status 400 Bad request: {}", e.getMessage(), e);
        return getErrorResponse(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleCustomValidationException(final CustomValidationException e) {
        log.debug("CustomValidationException, status 400 Bad request: {}", e.getMessage(), e);
        return getErrorResponse(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleDateTimeParseException(final DateTimeParseException e) {
        log.debug("DateTimeParseException, status 400 Bad request: {}", e.getMessage(), e);
        return getErrorResponse(e, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMissingParamException(final MissingServletRequestParameterException e) {
        log.debug("DateTimeParseException, status 400 Bad request: {}", e.getMessage(), e);
        return getErrorResponse(e, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleConstraintViolationException(final ConstraintViolationException e) {
        log.debug("ConstraintViolationException, status 409 CONFLICT: {}", e.getMessage(), e);
        return getErrorResponse(e, HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleDataIntegrityViolationException(final DataIntegrityViolationException e) {
        log.debug("ConstraintViolationException, status 409 CONFLICT: {}", e.getMessage(), e);
        return getErrorResponse(e, HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleCustomConflictException(final CustomConflictException e) {
        log.debug("ConstraintViolationException, status 409 CONFLICT: {}", e.getMessage(), e);
        return getErrorResponse(e, HttpStatus.CONFLICT);
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(final NotFoundException e) {
        log.debug("NotFoundException, status 404 NOT_FOUND: {}", e.getMessage(), e);
        return getErrorResponse(e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleThrowable(final Throwable e) {
        log.debug("Status 500 Internal Server Error: {}", e.getMessage(), e);
        return getErrorResponse(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleForbiddenException(final ForbiddenException e) {
        log.debug("ForbiddenException, status 403 FORBIDDEN: {}", e.getMessage(), e);
        return getErrorResponse(e, HttpStatus.FORBIDDEN);
    }

    private ErrorResponse getErrorResponse(Throwable e, HttpStatus status) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);

        return ErrorResponse.builder()
                .error(e.toString())
                .message(e.getMessage())
                .reason(e.getLocalizedMessage())
                .status(status.toString())
                .timestamp(LocalDateTime.now().format(format))
                .build();
    }
}
