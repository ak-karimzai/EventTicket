package com.akkarimzai.eventticket.controllers.api

import com.akkarimzai.eventticket.controllers.middlewares.ErrorResponse
import com.akkarimzai.eventticket.models.user.AuthResponse
import com.akkarimzai.eventticket.models.user.LoginCommand
import com.akkarimzai.eventticket.models.user.RegisterCommand
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.PostMapping

@Tag(name = "Auth API", description = "API for managing user authentication")
interface AuthControllerApi {
    @PostMapping("/register")
    @Operation(
        summary = "Register a new user",
        description = "Creates a new user account",
        responses = [
            ApiResponse(
                responseCode = "201",
                description = "User created successfully",
                content = [Content(schema = Schema(implementation = AuthResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Invalid request",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    fun register(
        @RequestBody
        @Parameter(
            name = "command",
            description = "User registration command",
            required = true
        )
        command: RegisterCommand
    ): AuthResponse

    @PostMapping("/login")
    @Operation(
        summary = "Login to an existing user account",
        description = "Authenticates a user and returns an authentication token",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "User logged in successfully",
                content = [Content(schema = Schema(implementation = AuthResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Invalid request",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            ),
            ApiResponse(
                responseCode = "401",
                description = "Invalid credentials",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    fun login(
        @RequestBody
        @Parameter(
            name = "command",
            description = "User login command",
            required = true
        )
        command: LoginCommand
    ): AuthResponse
}