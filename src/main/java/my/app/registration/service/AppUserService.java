package my.app.registration.service;

import lombok.RequiredArgsConstructor;
import my.app.registration.exception.EmailAlreadyExistsInDatabaseException;
import my.app.registration.model.AppUser;
import my.app.registration.model.ConfirmationToken;
import my.app.registration.repository.AppUserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class AppUserService implements UserDetailsService {
    private final AppUserRepository appUserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        return appUserRepository.findByUsername(email)
                .orElseThrow(() -> new UsernameNotFoundException("There is no user with given email in database!"));
    }

    @Transactional
    public void enableAppUser(final String email) {
        appUserRepository.findByUsername(email)
                .orElseThrow(() -> new UsernameNotFoundException("There is no user with given email in database!"))
                .setEnabled(true);
    }

    @Transactional
    public String signUpUser(final AppUser appUser)
            throws EmailAlreadyExistsInDatabaseException {
        boolean alreadyExists = appUserRepository
                .findByUsername(appUser.getUsername())
                .isPresent();
        if (alreadyExists) throw new EmailAlreadyExistsInDatabaseException();

        appUser.setPassword(bCryptPasswordEncoder.encode(appUser.getPassword()));
        appUserRepository.save(appUser);

        ConfirmationToken confirmationToken = confirmationTokenService.createConfirmationToken(appUser);
        confirmationTokenService.saveConfirmationToken(confirmationToken);

        return confirmationToken.getToken();
    }

    public AppUser getCurrentLoggedInAppUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        return  (AppUser)loadUserByUsername(username);
    }

}
