package ru.safonoviv.lms.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import ru.safonoviv.lms.util.ReadJson;

@OpenAPIDefinition
@Configuration
public class SpringDocConfig {
	
	@Autowired
	private ReadJson readJson;
	
	@Bean
	public OpenAPI baseOpenApi() {		
		ApiResponse successResponse = new ApiResponse()
				.content(new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
						new io.swagger.v3.oas.models.media.MediaType().addExamples("default",
								new Example().value(readJson.read().get("successResponse").toString()))))
				.description("Successful");

		ApiResponse forbiddenResponse = new ApiResponse()
				.content(new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
						new io.swagger.v3.oas.models.media.MediaType().addExamples("default",
								new Example().value(readJson.read().get("forbiddenResponse").toString()))))
				.description("forbidden!");

		ApiResponse badRequest = new ApiResponse()
				.content(new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
						new io.swagger.v3.oas.models.media.MediaType().addExamples("default",
								new Example().value(readJson.read().get("badRequest").toString()))))
				.description("Bad request!");

		ApiResponse unauthorized = new ApiResponse()
				.content(new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
						new io.swagger.v3.oas.models.media.MediaType().addExamples("default",
								new Example().value(readJson.read().get("unauthorized").toString()))))
				.description("Unauthorized!");
		
		
		
		Components components = new Components().addSecuritySchemes("Bearer Authentication", createAPIKeyScheme());
		components.addResponses("successResponse", successResponse);
		components.addResponses("forbiddenResponse", forbiddenResponse);
		components.addResponses("badRequest", badRequest);
		components.addResponses("unauthorized", unauthorized);
		
		
		
		return new OpenAPI()
				.addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
				.components(components)
				.info(new Info().title("Spring doc").version("1.0.0").description("Spring doc"));
	}
	
	
	private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme().type(SecurityScheme.Type.HTTP)
            .bearerFormat("JWT")
            .scheme("bearer");
    }

}
