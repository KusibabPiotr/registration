package my.app.registration.validator;

import my.app.registration.exception.PasswordNotMatchException;
import org.springframework.stereotype.Service;

@Service
public class PasswordEqualityValidator {
    public void validate(final String password, final String repeatedPassword)
            throws PasswordNotMatchException {
        if (!test(password, repeatedPassword)) {
            throw new PasswordNotMatchException();
        }
    }
    public boolean test(final String password,final String repeatedPassword) {
        return password.equals(repeatedPassword);
    }
}
