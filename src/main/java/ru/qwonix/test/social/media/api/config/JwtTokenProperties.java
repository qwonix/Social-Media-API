package ru.qwonix.test.social.media.api.config;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.WeakKeyException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.convert.DurationUnit;
import org.springframework.validation.annotation.Validated;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Validated
@Getter
@Slf4j
@ConfigurationProperties("jwt.access")
public class JwtTokenProperties {

    /**
     * Token expiration time.
     *
     * @see <a href="https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.external-config.typesafe-configuration-properties.conversion.durations">Spring Converting Durations Guide</a>
     */
    @DurationUnit(ChronoUnit.HOURS)
    private Duration ttl = Duration.ofHours(1);

    /**
     * Secret key for generating and verifying JWT signatures using the HMAC-SHA algorithm.
     *
     * Throws WeakKeyException if the key byte array length is less than 256 bits (32 bytes) as mandated by the
     * <a href="https://tools.ietf.org/html/rfc7518#section-3.2">JWT JWA Specification
     * (RFC 7518, Section 3.2)</a>
     */
    private SecretKey secret = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public void setSecret(String secret) {
        this.secret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        log.info("The secret key has been accepted and successfully used");
    }

    public void setTtl(Duration ttl) {
        this.ttl = ttl;
        log.info("Token expiration time is set at {}", ttl.toString());
    }
}
