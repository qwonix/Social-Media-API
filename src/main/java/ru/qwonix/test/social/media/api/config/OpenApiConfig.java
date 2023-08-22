package ru.qwonix.test.social.media.api.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;

@OpenAPIDefinition(
        info = @Info(contact = @Contact(
                name = "qwonix",
                email = "roman.qwonix@gmail.com",
                url = "https://github.com/qwonix"
        ),
                title = "OpenAPI documentation",
                version = "1.0"
        )
)
@SecuritySchemes({
        @SecurityScheme(
                name = "Bearer",
                description = "JWT Bearer Authentication. To receive the token you need to register and pass Basic authentication.",
                scheme = "Bearer",
                type = SecuritySchemeType.HTTP,
                bearerFormat = "JWT",
                in = SecuritySchemeIn.HEADER),
        @SecurityScheme(
                name = "Basic",
                description = "Basic Authentication",
                scheme = "Basic",
                type = SecuritySchemeType.HTTP,
                bearerFormat = "Basic",
                in = SecuritySchemeIn.HEADER)
})
public class OpenApiConfig {
}
