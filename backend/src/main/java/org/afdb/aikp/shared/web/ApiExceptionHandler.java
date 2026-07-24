package org.afdb.aikp.shared.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.afdb.aikp.shared.exception.AikpException;
import org.afdb.aikp.shared.exception.ConflictException;
import org.afdb.aikp.shared.exception.InfrastructureException;
import org.afdb.aikp.shared.exception.NotFoundException;
import org.afdb.aikp.shared.exception.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ProblemDetail handleNotFound(
            NotFoundException exception,
            HttpServletRequest request) {

        return buildProblem(
                HttpStatus.NOT_FOUND,
                exception,
                request);
    }

    @ExceptionHandler(ConflictException.class)
    public ProblemDetail handleConflict(
            ConflictException exception,
            HttpServletRequest request) {

        return buildProblem(
                HttpStatus.CONFLICT,
                exception,
                request);
    }

    @ExceptionHandler(ValidationException.class)
    public ProblemDetail handleValidation(
            ValidationException exception,
            HttpServletRequest request) {

        return buildProblem(
                HttpStatus.BAD_REQUEST,
                exception,
                request);
    }

    @ExceptionHandler(InfrastructureException.class)
    public ProblemDetail handleInfrastructure(
            InfrastructureException exception,
            HttpServletRequest request) {

        return buildProblem(
                HttpStatus.INTERNAL_SERVER_ERROR,
                exception,
                request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleBeanValidation(
            MethodArgumentNotValidException exception,
            HttpServletRequest request) {

        ProblemDetail problem =
                ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);

        problem.setTitle("Validation failed");
        problem.setDetail("One or more request fields are invalid.");
        problem.setInstance(URI.create(request.getRequestURI()));

        Map<String, String> errors = new LinkedHashMap<>();

        for (FieldError error : exception.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        problem.setProperty("timestamp", OffsetDateTime.now());
        problem.setProperty("errors", errors);

        return problem;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail handleConstraintViolation(
            ConstraintViolationException exception,
            HttpServletRequest request) {

        ProblemDetail problem =
                ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);

        problem.setTitle("Constraint violation");
        problem.setDetail(exception.getMessage());
        problem.setInstance(URI.create(request.getRequestURI()));
        problem.setProperty("timestamp", OffsetDateTime.now());

        return problem;
    }

    @ExceptionHandler(AikpException.class)
    public ProblemDetail handleAikpException(
            AikpException exception,
            HttpServletRequest request) {

        return buildProblem(
                HttpStatus.INTERNAL_SERVER_ERROR,
                exception,
                request);
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleUnexpected(
            Exception exception,
            HttpServletRequest request) {

        ProblemDetail problem =
                ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);

        problem.setTitle("Unexpected error");
        problem.setDetail(exception.getMessage());
        problem.setInstance(URI.create(request.getRequestURI()));
        problem.setProperty("timestamp", OffsetDateTime.now());

        return problem;
    }

    private ProblemDetail buildProblem(
            HttpStatus status,
            AikpException exception,
            HttpServletRequest request) {

        ProblemDetail problem = ProblemDetail.forStatus(status);

        problem.setTitle(status.getReasonPhrase());
        problem.setDetail(exception.getMessage());
        problem.setInstance(URI.create(request.getRequestURI()));

        problem.setProperty("timestamp", OffsetDateTime.now());
        problem.setProperty("errorCode", exception.getErrorCode());

        return problem;
    }

}