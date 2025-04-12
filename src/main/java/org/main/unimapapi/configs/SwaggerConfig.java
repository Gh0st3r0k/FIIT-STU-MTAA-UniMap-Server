package org.main.unimapapi.configs;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "UniMap API",
                version = "1.2",
                description = "Backend API for UniMap Mobile app (FIIT STU project)"
        )
)
public class SwaggerConfig {
}
