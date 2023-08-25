package ru.practicum.mainService.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler({MissingServletRequestParameterException.class, BadRequestException.class, MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequestException(final RuntimeException e) {
        log.debug("Получен статус 400 Not found {}", e.getMessage(), e);
        return new ErrorResponse(
                "BAD_REQUEST",
                LocalDateTime.now(),
                e.toString(),
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(final NotFoundException e) {
        log.debug("Получен статус 404 Not found {}", e.getMessage(), e);
        return new ErrorResponse(
                "NOT_FOUND",
                LocalDateTime.now(),
                e.toString(),
                e.getMessage()
        );
    }

    @ExceptionHandler({DataIntegrityViolationException.class, ConflictException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleConflictException(final RuntimeException e) {
        log.debug("Получен статус 409 Not found {}", e.getMessage(), e);
        return new ErrorResponse(
                "CONFLICT",
                LocalDateTime.now(),
                e.toString(),
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleThrowable(final Throwable e) {
        log.debug("Получен статус 500 Not found {}", e.getMessage(), e);
        return new ErrorResponse(
                "INTERNAL_SERVER_ERROR",
                LocalDateTime.now(),
                e.toString(),
                e.getMessage()
        );
    }

}