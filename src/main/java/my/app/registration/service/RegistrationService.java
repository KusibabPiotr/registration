package my.app.registration.service;

import lombok.RequiredArgsConstructor;
import my.app.registration.dto.RegistrationRequestDto;
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
            EmailAlreadyExistsInDatabaseException {
        passwordEqualityValidator.validate(request.password(), request.repeatPassword());

        String link = linkWithoutToken + appUserService.signUpUser(AppUserMapper.mapToAppUser(request));
        emailService.send(request.email(), EmailBuilder.buildEmail("Stranger", link));
        return EMAIL_WITH_LINK_JUST_SEND;
    }

    @Transactional
    public String confirmToken(final String token)
            throws TokenNotFoundException, EmailAlreadyConfirmedException,
            TokenExpiredException {
        ConfirmationToken confirmationToken = confirmationTokenService
                .getToken(token)
                .orElseThrow(TokenNotFoundException::new);
        tokenValidator.validateIfAlreadyConfirmed(confirmationToken.getConfirmedAt());
        tokenValidator.validateConfirmationTime(confirmationToken.getExpiresAt());

        confirmationTokenService.setConfirmedAt(token);
        appUserService.enableAppUser(confirmationToken.getAppUser().getUsername());
        return EMAIL_SUCCESSFULLY_CONFIRMED;
    }
}
