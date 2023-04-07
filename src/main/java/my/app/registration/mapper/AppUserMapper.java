package my.app.registration.mapper;

import my.app.registration.dto.RegistrationRequestDto;
import my.app.registration.model.AppUser;
import my.app.registration.model.AppUserRole;

public class AppUserMapper {
    public static AppUser mapToAppUser(final RegistrationRequestDto dto) {
        return AppUser.builder()
                .username(dto.email())
                .password(dto.password())
                .appUserRole(AppUserRole.USER)
                .enabled(false)
                .build();
    }
}
