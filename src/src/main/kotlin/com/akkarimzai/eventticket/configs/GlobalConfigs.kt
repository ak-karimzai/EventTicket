package com.akkarimzai.eventticket.configs

import com.akkarimzai.eventticket.controllers.middlewares.JwtAuthenticationMiddleware
import com.akkarimzai.eventticket.entities.Role
import com.akkarimzai.eventticket.services.UserService
import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
class GlobalConfigs(
    private val jwtAuthenticationMiddleware: JwtAuthenticationMiddleware,
    private val userService: UserService) {

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun authenticationManager(authenticationConfiguration: AuthenticationConfiguration): AuthenticationManager {
        return authenticationConfiguration.authenticationManager
    }

    @Bean
    fun authenticationProvider(userService: UserService, passwordEncoder: PasswordEncoder): AuthenticationProvider {
        val authProvider = DaoAuthenticationProvider()
        authProvider.setUserDetailsService(userService.userDetailsService())
        authProvider.setPasswordEncoder(passwordEncoder)
        return authProvider
    }

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        return http.csrf { it.disable() }
            .cors { cors ->
                cors.configurationSource {
                    val corsConfig = CorsConfiguration()
                    corsConfig.allowedOriginPatterns = listOf("*")
                    corsConfig.allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "OPTIONS")
                    corsConfig.allowedHeaders = listOf("*")
                    corsConfig.allowCredentials = true
                    corsConfig
                }
            }
            .authorizeHttpRequests { request ->
                request
//                    .requestMatchers("/api/auth/**").permitAll()
//                    .requestMatchers("/swagger-ui/**").permitAll()
//                    .requestMatchers(HttpMethod.GET, "/api/**").permitAll()
//                    .requestMatchers(HttpMethod.POST, "/api/**").hasAnyRole(Role.ADMIN.name, Role.USER.name)
//                    .requestMatchers(HttpMethod.PUT, "/api/**").hasAnyRole(Role.ADMIN.name, Role.USER.name)
//                    .requestMatchers(HttpMethod.DELETE, "/api/**").hasRole(Role.ADMIN.name)
                    .anyRequest().permitAll()
            }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authenticationProvider(authenticationProvider(userService, passwordEncoder()))
            .addFilterBefore(jwtAuthenticationMiddleware, UsernamePasswordAuthenticationFilter::class.java)
            .build()
    }

//    @Bean
//    fun customOpenAPI(): OpenAPI {
//        return OpenAPI()
//            .info(
//                Info()
//                    .title("API Documentation")
//                    .version("v1")
//                    .description("API Docs")
//            )
//            .addSecurityItem(SecurityRequirement().addList("bearerAuth"))
//            .components(
//                Components().addSecuritySchemes(
//                    "bearerAuth",
//                    SecurityScheme()
//                        .name("bearerAuth")
//                        .type(SecurityScheme.Type.HTTP)
//                        .scheme("bearer")
//                        .bearerFormat("JWT")
//                )
//            )
//    }
}
