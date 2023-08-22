package ru.qwonix.test.social.media.api.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@Data
@ConfigurationProperties("jwt.access")
public class JwtTokenProperties {

    /**
     * Token expiration time
     */
    private Duration ttl = Duration.ofHours(1);

    /**
     * Token expiration time
     */
    private String secret;

}
