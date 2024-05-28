package com.capstone.jachulsa.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class SwaggerConfig {
    @Bean
    fun openAPI(): OpenAPI = OpenAPI()
            .components(Components())
            .info(apiInfo())

    private fun apiInfo() = Info()
            .title("Jachulsa Swagger")
            .description("CapstoneDesign 자출사 프로젝트 Swagger UI test2")
            .version("1.0.0")
}