package com.code81.library.model;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class ErrorResponse {
    private final LocalDateTime timestamp = LocalDateTime.now();
    private final int status;
    private final String error;
    private final String path;
    private final String trace;

    public ErrorResponse(HttpStatus status, String error, String path, String trace) {
        this.status = status.value();
        this.error = error;
        this.path = path;
        this.trace = trace;
    }

    // Getters only
    public LocalDateTime getTimestamp() { return timestamp; }
    public int getStatus() { return status; }
    public String getError() { return error; }
    public String getPath() { return path; }
    public String getTrace() { return trace; }
}
