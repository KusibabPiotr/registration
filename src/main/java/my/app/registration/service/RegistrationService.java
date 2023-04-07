package my.app.registration.service;

import lombok.RequiredArgsConstructor;
import my.app.registration.dto.RegistrationRequestDto;
import my.app.registration.email.EmailBuilder;
import my.app.registration.email.EmailService;
import my.app.registration.exception.*;
import my.app.registration.mapper.AppUserMapper;
import my.app.registration.model.ConfirmationToken;
import my.app.registration.validator.PasswordEqualityValidator;
import my.app.registration.validator.TokenValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static my.app.registration.Constants.EMAIL_SUCCESSFULLY_CONFIRMED;
import static my.app.registration.Constants.EMAIL_WITH_LINK_JUST_SEND;

@RequiredArgsConstructor
@Service
public class RegistrationService {
    private final PasswordEqualityValidator passwordEqualityValidator;
    private final AppUserService appUserService;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailService emailService;
    private final TokenValidator tokenValidator;
    @Value("${confirm_registration_link}")
    private String linkWithoutToken;


    public String register(final RegistrationRequestDto request)
            throws EmailNotValidException, PasswordNotMatchException,
            EmailAlreadyExistsInDatabaseException{
        validateIfPasswordsAreTheSame(request);
        String token = signUpUserAndGetTokenForConfirmation(request);
        sendEmailWithConfirmationEmail(request, token);
        return EMAIL_WITH_LINK_JUST_SEND;
    }

    private void sendEmailWithConfirmationEmail(RegistrationRequestDto request, String token) {
        emailService.send(request.email(), EmailBuilder.buildEmail("Stranger", linkWithoutToken + token));
    }

    private String signUpUserAndGetTokenForConfirmation(RegistrationRequestDto request) {
        return appUserService.signUpUser(AppUserMapper.mapToAppUser(request));
    }

    private void validateIfPasswordsAreTheSame(RegistrationRequestDto request) {
        passwordEqualityValidator.validate(request.password(), request.repeatPassword());
    }

    @Transactional
    public String confirmRegistration(final String token)
            throws TokenNotFoundException, EmailAlreadyConfirmedException,
            TokenExpiredException {
        ConfirmationToken confirmationToken = obtainConfirmationToken(token);
        validateToken(confirmationToken);
        setConfirmationTime(token);
        enableUser(confirmationToken);
        return EMAIL_SUCCESSFULLY_CONFIRMED;
    }

    private void enableUser(ConfirmationToken confirmationToken) {
        appUserService.enableAppUser(confirmationToken.getAppUser().getUsername());
    }

    private void setConfirmationTime(String token) {
        confirmationTokenService.setConfirmedAt(token);
    }

    private void validateToken(ConfirmationToken confirmationToken) {
        tokenValidator.validateIfAlreadyConfirmed(confirmationToken.getConfirmedAt());
        tokenValidator.validateConfirmationTime(confirmationToken.getExpiresAt());
    }

    private ConfirmationToken obtainConfirmationToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService
                .getToken(token)
                .orElseThrow(TokenNotFoundException::new);
        return confirmationToken;
    }
}
