package my.app.registration.mapper;


import my.app.registration.dto.RegistrationRequestDto;
import my.app.registration.model.AppUser;
import my.app.registration.model.AppUserRole;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class AppUserMapperTest {
    @Test
    void shouldMapRegRequestDtoToAppUser() {
        //given
        var registrationRequestDto =
                RegistrationRequestDto.builder()
                        .email("kruk@kruk.pl")
                        .password("password")
                        .repeatPassword("password")
                        .build();
        //when
        AppUser appUser = AppUserMapper.mapToAppUser(registrationRequestDto);
        //then
        assertThat(appUser.getPassword()).isEqualTo("password");
        assertThat(appUser.getUsername()).isEqualTo("kruk@kruk.pl");
        assertThat(appUser.getAppUserRole()).isEqualTo(AppUserRole.USER);
        assertThat(appUser.isEnabled()).isFalse();
    }
}