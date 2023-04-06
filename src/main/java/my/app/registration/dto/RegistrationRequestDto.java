package my.app.registration.dto;

import lombok.Builder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Builder
public record RegistrationRequestDto(
        @Email(regexp = "^(.+)@(\\S+)$", message = "You have to provide right email format!")
        @NotNull String email,
        @NotNull @Size(min = 6, message = "Your password should contains at least 6 signs!") String password,
        @NotNull String repeatPassword
) {}
