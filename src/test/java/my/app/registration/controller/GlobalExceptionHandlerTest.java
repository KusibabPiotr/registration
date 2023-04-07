package my.app.registration.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.ServletWebRequest;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@WebMvcTest(controllers = GlobalExceptionHandler.class)
@ContextConfiguration(classes = {GlobalExceptionHandler.class})
public class GlobalExceptionHandlerTest {

    @Autowired
    private GlobalExceptionHandler handler;

    @BeforeEach
    public void setUp() {
        // set up any necessary mock objects here
    }

    @Test
    public void testHandleEmailAlreadyExistsInDatabaseException() throws Exception {
        ResponseEntity<String> response = handler.handleEmailAlreadyExistsInDatabaseException();

        assertThat(response.getStatusCodeValue()).isEqualTo(400);
        assertThat(response.getBody()).isEqualTo("Email already exists in database! Try to register using different one!");
    }

    @Test
    public void testHandlePasswordNotMatchException() throws Exception {
        ResponseEntity<String> response = handler.handlePasswordNotMatchException();

        assertThat(response.getStatusCodeValue()).isEqualTo(400);
        assertThat(response.getBody()).isEqualTo("Password in both fields should match!");
    }

    @Test
    public void testHandleEmailAlreadyConfirmedException() throws Exception {
        ResponseEntity<String> response = handler.handleEmailAlreadyConfirmedException();

        assertThat(response.getStatusCodeValue()).isEqualTo(400);
        assertThat(response.getBody()).isEqualTo("You have confirmed your email already!");
    }

    @Test
    public void testHandleTokenExpiredException() throws Exception {
        ResponseEntity<String> response = handler.handleTokenExpiredException();

        assertThat(response.getStatusCodeValue()).isEqualTo(400);
        assertThat(response.getBody()).isEqualTo("Your token already expired! Register again!");
    }

    @Test
    public void testHandleTokenNotFoundException(){
        ResponseEntity<String> response = handler.handleTokenNotFoundException();

        assertThat(response.getStatusCodeValue()).isEqualTo(400);
        assertThat(response.getBody()).isEqualTo("Invalid token! Regester again!");
    }

    @Test
    public void testHandleUsernameNotFoundException(){
        ResponseEntity<String> response = handler.handleUsernameNotFoundException();

        assertThat(response.getStatusCodeValue()).isEqualTo(400);
        assertThat(response.getBody()).isEqualTo("There is no user with given username!");
    }

    @Test
    public void handleMethodArgumentNotValid() {
        GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();
        BindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "object");
        bindingResult.addError(new FieldError("object", "field", "error message"));

        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(
                new MethodParameter(this.getClass().getDeclaredMethods()[0], -1),
                bindingResult);

        ResponseEntity<Object> response = exceptionHandler.handleMethodArgumentNotValid(ex,
                new HttpHeaders(),
                HttpStatus.BAD_REQUEST,
                new ServletWebRequest(new MockHttpServletRequest()));
        assertThat(HttpStatus.BAD_REQUEST).isEqualTo(response.getStatusCode());
        assertThat("error message").isEqualTo( response.getBody());
    }
}