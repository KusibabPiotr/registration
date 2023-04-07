package my.app.registration.configuration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {CoreConfiguration.class})
public class CoreConfigurationTest {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private RestTemplate restTemplate;

    @Test
    public void testBCryptPasswordEncoder() {
        String password = "password";
        String encodedPassword = bCryptPasswordEncoder.encode(password);

        assertThat(bCryptPasswordEncoder.matches(password, encodedPassword)).isTrue();
    }

    @Test
    public void testRestTemplate() {
        assertThat(restTemplate).isNotNull();
    }
}