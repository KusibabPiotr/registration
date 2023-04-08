package my.app.registration.repository;

import my.app.registration.model.AppUser;
import my.app.registration.model.AppUserRole;
import my.app.registration.model.ConfirmationToken;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ConfirmationTokenRepositoryTest {
    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;
    @Autowired
    private AppUserRepository appUserRepository;

    @Test
    void shouldFindConfirmationTokenByToken() {
        //given
        String email = "marian@kowal.com";
        AppUser appUser = AppUser.builder()
                .appUserRole(AppUserRole.USER)
                .password("password")
                .username(email)
                .enabled(false)
                .build();
        AppUser saved = appUserRepository.save(appUser);
        String tokenName = "token";
        ConfirmationToken token = ConfirmationToken.builder()
                .appUser(appUser)
                .token(tokenName)
                .created(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .build();
        ConfirmationToken savedToken = confirmationTokenRepository.save(token);
        //when
        Optional<ConfirmationToken> tokenFromDb = confirmationTokenRepository.findByToken(tokenName);
        //then
        assertThat(tokenFromDb).isPresent();
        //cleanUp
        try {
            confirmationTokenRepository.deleteById(savedToken.getId());
            appUserRepository.deleteById(saved.getId());
        } catch (IllegalArgumentException e) {

        }
    }
}
