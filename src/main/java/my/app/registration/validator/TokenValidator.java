package my.app.registration.validator;

import my.app.registration.exception.EmailAlreadyConfirmedException;
import my.app.registration.exception.TokenExpiredException;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class TokenValidator {
    public void validateConfirmationTime(final LocalDateTime expiresAt)
            throws TokenExpiredException {
        if (expiresAt.isBefore(LocalDateTime.now())) {
            throw new TokenExpiredException();
        }
    }
    public void validateIfAlreadyConfirmed(final LocalDateTime confirmedAt)
            throws EmailAlreadyConfirmedException {
        if (confirmedAt != null) {
            throw new EmailAlreadyConfirmedException();
        }
    }
}
