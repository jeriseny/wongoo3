package org.wongoo.wongoo3.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springdoc.core.utils.SpringDocUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.wongoo.wongoo3.domain.user.dto.LoginUser;

@Configuration
public class SwaggerConfig {

    private static final String JWT_SCHEME_NAME = "JWT";

    static {
        SpringDocUtils.getConfig().addRequestWrapperToIgnore(LoginUser.class);
    }

    @Bean
    public OpenAPI openApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Wongoo3 API")
                        .description("Wongoo3 API Docs")
                        .version("v0.1"))
                .addSecurityItem(new SecurityRequirement().addList(JWT_SCHEME_NAME))
                .components(new Components().addSecuritySchemes(JWT_SCHEME_NAME, jwtSecurityScheme()));
    }

    private SecurityScheme jwtSecurityScheme() {
        return new SecurityScheme()
                .name(JWT_SCHEME_NAME)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT");
    }

    @Bean
    public OperationCustomizer hideLoginUserParameter() {
        return ((operation, handlerMethod) -> {
            if (operation.getParameters() == null) {return operation;}

            operation.getParameters().removeIf(parameter ->
                    parameter.getSchema() != null &&
                            "loginUser".equals(parameter.getSchema().getName())
            );

            return operation;
        });
    }

}
