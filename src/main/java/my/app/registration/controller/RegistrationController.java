package my.app.registration.controller;

import lombok.RequiredArgsConstructor;
import my.app.registration.dto.RegistrationRequestDto;
import my.app.registration.exception.*;
import my.app.registration.service.RegistrationService;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.IOException;
import java.security.GeneralSecurityException;

@RestController
@RequestMapping("/api/v1/registration")
@RequiredArgsConstructor
public class RegistrationController {
    private final RegistrationService registrationService;

    @PostMapping
    public String register(@RequestBody @Valid RegistrationRequestDto request)
            throws EmailNotValidException, PasswordNotMatchException,
            EmailAlreadyExistsInDatabaseException, MessagingException, GeneralSecurityException, IOException {
        return registrationService.register(request);
    }

    @GetMapping("/confirm")
    public String confirmEmail(@RequestParam String token)
            throws TokenNotFoundException, EmailAlreadyConfirmedException,
            TokenExpiredException {
        return registrationService.confirmToken(token);
    }
}
