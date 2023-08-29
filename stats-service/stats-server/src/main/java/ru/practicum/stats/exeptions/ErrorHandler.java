package ru.practicum.stats.exeptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

import static ru.practicum.common.DateAndTimeFormatter.DATE_TIME_FORMATTER;

@RestControllerAdvice
public class ErrorHandler {

    private final int uriPrefixLength = 4;

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIllegalArgumentException(final IllegalArgumentException e, final WebRequest request) {
        String path = request.getDescription(false).substring(uriPrefixLength);
        return createErrorResponse(HttpStatus.BAD_REQUEST, path, e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMissingServletRequestParameterException(final MissingServletRequestParameterException e,
                                                                       final WebRequest request) {
        String path = request.getDescription(false).substring(uriPrefixLength);
        return createErrorResponse(HttpStatus.BAD_REQUEST, path, e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleThrowable(final Throwable e, final WebRequest request) {
        String path = request.getDescription(false).substring(uriPrefixLength);
        return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, path, e.getMessage());
    }

    private ErrorResponse createErrorResponse(HttpStatus status, String path, String message) {
        return new ErrorResponse(
                status.value(),
                LocalDateTime.now().format(DATE_TIME_FORMATTER),
                path,
                message
        );
    }

}