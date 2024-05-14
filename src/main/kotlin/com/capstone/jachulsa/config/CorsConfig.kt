package com.capstone.jachulsa.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Order(1)
@Configuration
@EnableWebSecurity
open class CorsConfig(

) {
    @Bean
    open fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http.invoke {
            cors {
                configurationSource = corsConfigurationSource()
            }
            csrf { disable() }
            authorizeRequests {
                authorize("/**", permitAll)
            }
        }
        return http.build()
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        configuration.allowedOrigins = listOf("http://localhost:3000","https://jachulsa.shop"," https://dev.jachulsa.shop")
        configuration.allowedMethods = listOf("POST", "GET", "DELETE", "PUT","OPTIONS")
        configuration.allowedHeaders = listOf("*")
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }
}