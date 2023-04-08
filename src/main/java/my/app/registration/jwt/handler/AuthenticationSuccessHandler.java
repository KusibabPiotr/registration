package my.app.registration.jwt.handler;

import com.auth0.jwt.JWT;
import lombok.RequiredArgsConstructor;
import my.app.registration.jwt.settings.JwtAlgorithm;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.Date;
import java.util.stream.Collectors;

import static my.app.registration.jwt.settings.JwtConstants.*;

@Component
@RequiredArgsConstructor
public class AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtAlgorithm algorithm;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication){
        UserDetails principal = (UserDetails) authentication.getPrincipal();

        String token = JWT.create()
                .withSubject(principal.getUsername())
                .withClaim("roles", principal.getAuthorities().stream().map(e -> new SimpleGrantedAuthority(e.toString())).collect(Collectors.toList()).get(0).toString())
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(java.sql.Date.valueOf(LocalDate.now().plusDays(TOKEN_EXPIRATION_TIME_DAYS)))
                .sign(algorithm.getAlgorithm());

        response.setHeader(ACCESS_TOKEN_HEADER, PREFIX.concat(token));
    }
}
