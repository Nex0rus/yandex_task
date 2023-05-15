package ru.yandex.yandexlavka.common.advices;

import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.yandex.yandexlavka.common.exceptions.*;
import ru.yandex.yandexlavka.common.types.ApiError;
import ru.yandex.yandexlavka.common.types.ErrorResponse;

import java.util.*;

@RestControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {
        List<String> errors = new ArrayList<String>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }

        ApiError apiError =
                new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors);
        return handleExceptionInternal(
                ex, apiError, headers, apiError.getStatus(), request);
    }

    @ResponseBody
    @ExceptionHandler({ ConstraintViolationException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleConstraintViolation(
            ConstraintViolationException ex, WebRequest request) {
        List<String> errors = new ArrayList<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.add(violation.getRootBeanClass().getName() + " " +
                    violation.getPropertyPath() + ": " + violation.getMessage());
        }

        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors);
        return new ErrorResponse(
                apiError, new HttpHeaders());
    }

    @ResponseBody
    @ExceptionHandler({CourierException.class, OrderException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleDomainException(DomainException ex) {
        return getGeneralResponse(ex, ex.getClass(), HttpStatus.BAD_REQUEST);
    }

    @ResponseBody
    @ExceptionHandler({TimeException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleTimeException(TimeException ex) {
        return getGeneralResponse(ex, TimeException.class, HttpStatus.BAD_REQUEST);
    }

    @ResponseBody
    @ExceptionHandler({NotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(NotFoundException ex) {
        return getGeneralResponse(ex, NotFoundException.class, HttpStatus.NOT_FOUND);
    }

    @ResponseBody
    @ExceptionHandler({RequestNotPermitted.class})
    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    public ErrorResponse handleTooManyRequests(RequestNotPermitted ex) {
        return getGeneralResponse(ex, RequestNotPermitted.class, HttpStatus.TOO_MANY_REQUESTS);
    }

    private ErrorResponse getGeneralResponse(Exception ex, Class<?> exceptionClass, HttpStatus status) {
        ApiError apiError =
                new ApiError(status, ex.getLocalizedMessage(), exceptionClass.getName());
        return new ErrorResponse(
                apiError, new HttpHeaders());
    }
}
