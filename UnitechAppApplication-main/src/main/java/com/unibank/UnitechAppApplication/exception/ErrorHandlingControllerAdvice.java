package com.unibank.UnitechAppApplication.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class ErrorHandlingControllerAdvice {

    public ErrorHandlingControllerAdvice() {
    }

    public static class ErrorResponseBody {
        private HttpStatus status;
        private String message;

        public ErrorResponseBody(HttpStatus status, String message) {
            this.status = status;
            this.message = message;
        }

        public HttpStatus getStatus() {
            return status;
        }

        public String getMessage() {
            return message;
        }
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseBody> handleException(Exception ex) {
        ErrorResponseBody errorResponseBody = new ErrorResponseBody(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred.");
        return new ResponseEntity<>(errorResponseBody, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponseBody> authenticationException(AuthenticationException ex) {
        String errorMessage = ex.getMessage();

        ErrorResponseBody errorResponse = new ErrorResponseBody(HttpStatus.BAD_REQUEST, errorMessage);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidJwtAuthenticationException.class)
    public ResponseEntity<ErrorResponseBody> handleJwtException(InvalidJwtAuthenticationException ex) {
        String errorMessage = ex.getMessage();

        ErrorResponseBody errorResponse = new ErrorResponseBody(HttpStatus.BAD_REQUEST, errorMessage);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponseBody> handleBadRequestException(BadRequestException ex) {
        String errorMessage = ex.getMessage();

        ErrorResponseBody errorResponseBody = new ErrorResponseBody(HttpStatus.BAD_REQUEST, errorMessage);
        return new ResponseEntity<>(errorResponseBody, HttpStatus.BAD_REQUEST);
    }

}
