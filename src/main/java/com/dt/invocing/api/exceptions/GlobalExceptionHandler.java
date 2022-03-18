package com.dt.invocing.api.exceptions;

import java.util.stream.Collectors;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.dt.invocing.business.InvalidInputData;
import com.dt.invocing.business.ValidationException;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {
        String error = "Malformed JSON request";

        return buildResponseEntity(
                new ApiException(HttpStatus.BAD_REQUEST, error, ex));
    }

    @ExceptionHandler(InvalidInputData.class)
    protected ResponseEntity<Object> handleInvalidCSVFileContentException(
            InvalidInputData ex, WebRequest request) {
        String error = "Invalid input data.";
        ApiException exception = new ApiException(HttpStatus.BAD_REQUEST, error,
                ex);
        exception.setNestedExceptionMessages(
                ex.getExceptions().stream().map(ValidationException::getMessage)
                .collect(Collectors.toList()));
        return buildResponseEntity(exception);
    }

    private ResponseEntity<Object> buildResponseEntity(
            ApiException apiException) {
        return new ResponseEntity<>(apiException, apiException.getStatus());
    }
}
