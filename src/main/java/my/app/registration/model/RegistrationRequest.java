package my.app.registration.model;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class RegistrationRequest {

        @Email(message = "Provide right email format!")
        @NotBlank(message = "Email is mandatory!")
        private final String login;
        @NotBlank(message = "Password is mandatory!")
        private final String password;
        @NotBlank(message = "Repeat your password")
        private final String repeatPassword;
}
