package my.app.registration.repository;

import my.app.registration.model.AppUser;
import my.app.registration.model.AppUserRole;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest
class AppUserRepositoryTest {
    @Autowired
    private AppUserRepository appUserRepository;

    @Test
    void shouldReturnAdminUsernameAlreadySavedInDb() {
        //given
        String email = "marian@kowal.com";
        AppUser user = AppUser.builder()
                .appUserRole(AppUserRole.USER)
                .password("password")
                .username(email)
                .enabled(false)
                .build();
        AppUser saved = appUserRepository.save(user);
        //when
        Optional<AppUser> appUserFromDb = appUserRepository.findByUsername(email);
        //then
        assertThat(appUserFromDb).isPresent();
        //cleanUp
        try {
            appUserRepository.deleteById(saved.getId());
        } catch (IllegalArgumentException e) {

        }
    }
}