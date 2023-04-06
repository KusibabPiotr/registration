package my.app.registration.controller;

import lombok.RequiredArgsConstructor;
import my.app.registration.dto.RegistrationRequestDto;
import my.app.registration.exception.*;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/registration")
@RequiredArgsConstructor
public class RegistrationController {
    private final RegistrationService registrationService;

    @PostMapping
    public String register(@RequestBody @Valid RegistrationRequestDto request)
            throws EmailNotValidException, PasswordNotMatchException,
            EmailAlreadyExistsInDatabaseException {
        return registrationService.register(request);
    }

    @GetMapping("/confirm")
    public String confirmEmail(@RequestParam String token)
            throws TokenNotFoundException, EmailAlreadyConfirmedException,
            TokenExpiredException {
        return registrationService.confirmToken(token);
    }
}
