package com.enifl33fi.lab1.api.exception;

import com.enifl33fi.lab1.api.dto.response.ErrorResponseDto;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionApiHandler {

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler({
            UsernameNotFoundException.class,
            EmailNotUniqueException.class,
            ConstraintViolationException.class,
            MethodArgumentNotValidException.class,
            EmailOtpException.class,
            NotFoundException.class,
            IllegalStateException.class
    })
    public ResponseEntity<ErrorResponseDto> badRequestHandler(Exception e) {
        return ResponseEntity.badRequest()
                .body(
                        ErrorResponseDto.builder()
                                .status(HttpStatus.BAD_REQUEST.value())
                                .statusText(HttpStatus.BAD_REQUEST.getReasonPhrase())
                                .message(e.getMessage())
                                .build());
    }

    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    @ResponseBody
    @ExceptionHandler({BadCredentialsException.class})
    public ResponseEntity<ErrorResponseDto> unauthorizedHandler(Exception e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(
                        ErrorResponseDto.builder()
                                .status(HttpStatus.UNAUTHORIZED.value())
                                .statusText(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                                .message(e.getMessage())
                                .build());
    }

    @ResponseStatus(code = HttpStatus.METHOD_NOT_ALLOWED)
    @ResponseBody
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public ResponseEntity<ErrorResponseDto> methodNotAllowedHandler(Exception e) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(
                        ErrorResponseDto.builder()
                                .status(HttpStatus.METHOD_NOT_ALLOWED.value())
                                .statusText(HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase())
                                .message(e.getMessage())
                                .build());
    }

    @ResponseStatus(code = HttpStatus.FORBIDDEN)
    @ResponseBody
    @ExceptionHandler({RefreshTokenException.class, AccessDeniedException.class})
    public ResponseEntity<ErrorResponseDto> forbiddenHandler(Exception e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(
                        ErrorResponseDto.builder()
                                .status(HttpStatus.FORBIDDEN.value())
                                .statusText(HttpStatus.FORBIDDEN.getReasonPhrase())
                                .message(e.getMessage())
                                .build());
    }
}

