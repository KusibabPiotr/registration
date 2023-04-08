package my.app.registration.dto;

import lombok.Builder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Builder
public record RegistrationRequestDto(
        @Email(regexp = "^(.+)@(\\S+)$", message = "You have to provide right email format!")
        @NotNull
        String email,
        @NotNull
        @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{6,}$", message = "Password must be at least 6 characters long and contain at least 1 digit, 1 lowercase letter, 1 uppercase letter, 1 special character, and no whitespaces.")
        String password,
        @NotNull
        String repeatPassword
) {}
