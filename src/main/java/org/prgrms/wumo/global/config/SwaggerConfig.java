package org.prgrms.wumo.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI openApi(@Value("${springdoc.version}") String springdocVersion) {
		Info info = new Info()
			.title("WuMo")
			.version(springdocVersion)
			.description("우리들의 모임");

		return new OpenAPI()
			.components(new Components())
			.info(info);
	}
}
