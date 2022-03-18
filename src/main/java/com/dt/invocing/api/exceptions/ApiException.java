package com.dt.invocing.api.exceptions;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ApiException {
    private HttpStatus status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;
    private String message;
    private String debugMessage;
    private List<String> nestedExceptionMessages;

    private ApiException() {
        timestamp = LocalDateTime.now();
    }

    ApiException(HttpStatus status) {
        this();
        this.status = status;
    }

    ApiException(HttpStatus status, Throwable ex) {
        this();
        this.status = status;
        this.setMessage("Unexpected error");
        this.setDebugMessage(ex.toString());
    }

    ApiException(HttpStatus status, String message, Throwable ex) {
        this();
        this.status = status;
        this.setMessage(message);
        this.setDebugMessage(ex.toString());
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDebugMessage() {
        return debugMessage;
    }

    public void setDebugMessage(String debugMessage) {
        this.debugMessage = debugMessage;
    }

    public List<String> getNestedExceptionMessages() {
        return nestedExceptionMessages;
    }

    public void setNestedExceptionMessages(List<String> nestedExceptionMessages) {
        this.nestedExceptionMessages = nestedExceptionMessages;
    }

}
