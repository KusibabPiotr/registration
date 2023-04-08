package my.app.registration.jwt.settings;

import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtAlgorithm {

    private final Algorithm algorithm;

    public JwtAlgorithm(@Value("${jwt.secret}")String secret) {
        this.algorithm = Algorithm.HMAC512(secret);
    }
    public Algorithm getAlgorithm() {
        return algorithm;
    }
}
