package my.app.registration.model;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public record RegistrationRequest(
        @Email(regexp = "^(.+)@(\\S+)$", message = "You have to provide right email format!")
        @NotBlank(message = "Email is mandatory!")
        @NotNull
        String login,
        @Length(min = 6, message = "Your password should contain at least 6 characters!")
        @NotBlank(message = "Password is mandatory!")
        @NotNull
        String password,
        @NotBlank(message = "Repeat your password")
        @NotNull
        String repeatPassword
) {}
