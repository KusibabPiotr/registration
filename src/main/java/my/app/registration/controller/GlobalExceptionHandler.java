package my.app.registration.controller;

import my.app.registration.exception.*;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EmailAlreadyExistsInDatabaseException.class)
    public ResponseEntity<String> handleEmailAlreadyExistsInDatabaseException() {
        return new ResponseEntity<>("Email already exists in database! Try to register using different one!", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PasswordNotMatchException.class)
    public ResponseEntity<String> handlePasswordNotMatchException() {
        return new ResponseEntity<>("Password in both fields should match!", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmailAlreadyConfirmedException.class)
    public ResponseEntity<String> handleEmailAlreadyConfirmedException() {
        return new ResponseEntity<>("You have confirmed your email already!", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<String> handleTokenExpiredException() {
        return new ResponseEntity<>("Your token already expired! Register again!", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TokenNotFoundException.class)
    public ResponseEntity<String> handleTokenNotFoundException() {
        return new ResponseEntity<>("Invalid token! Regester again!", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<String> handleUsernameNotFoundException() {
        return new ResponseEntity<>("There is no user with given username!", HttpStatus.BAD_REQUEST);
    }

    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {

        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList())
                .get(0);

        return new ResponseEntity<>(message, status);
    }
}
