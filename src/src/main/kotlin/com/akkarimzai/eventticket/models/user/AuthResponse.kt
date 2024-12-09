package com.akkarimzai.eventticket.models.user

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Authentication response")
data class AuthResponse(
    @Schema(description = "Authentication token", example = "abc123", required = true)
    val token: String
)
