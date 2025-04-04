package com.neighbour_snack.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalRestExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalRestExceptionHandler.class);

    @ExceptionHandler(NoSuchElementException.class)
    public ProblemDetail handleNoSuchElementException(NoSuchElementException nsee) {
        logger.warn("NoSuchElementException occurred", nsee);
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND,
                nsee.getLocalizedMessage());
    }

    @ExceptionHandler(SmtpException.class)
    public ProblemDetail handleSmtpException(SmtpException se) {
        logger.warn("SmtpException occurred", se);
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
                se.getLocalizedMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleMethodArgumentNotValidException(MethodArgumentNotValidException manve) {
        logger.warn("MethodArgumentNotValidException occurred", manve);

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);

        List<Map<String, Object>> errors = manve.getBindingResult().getFieldErrors().stream()
                .map(error -> Map.of(
                        "field", error.getField(),
                        "message", error.getDefaultMessage(),
                        "rejectedValue", error.getRejectedValue()))
                .collect(Collectors.toList());

        problemDetail.setProperty("errors", errors);

        // problemDetail.setProperty("errors",
        // manve.getBindingResult().getFieldErrors().stream()
        // .collect(Collectors.toMap(FieldError::getField,
        // FieldError::getDefaultMessage)));

        return problemDetail;
    }

    @ExceptionHandler(AuthenticationException.class)
    public ProblemDetail handleAuthenticationException(AuthenticationException ae) {
        logger.warn("AuthenticationException occurred", ae);
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED,
                ae.getLocalizedMessage());
        problemDetail.setProperty("timestamp", ZonedDateTime.now());
        return problemDetail;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handleIllegalArgumentException(IllegalArgumentException iae) {
        logger.warn("IllegalArgumentException occurred", iae);
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, iae.getLocalizedMessage());
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleException(Exception e) {
        logger.warn("Exception occurred", e);
        return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR,
                Objects.requireNonNullElse(e.getLocalizedMessage(), "Something went wrong"));
    }

}
