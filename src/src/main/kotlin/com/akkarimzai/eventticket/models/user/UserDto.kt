package com.akkarimzai.eventticket.models.user

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "User data transfer object")
data class UserDto(
    @Schema(description = "User ID", example = "1", required = true)
    val id: Long,

    @Schema(description = "Full name", example = "John Doe", required = true)
    val name: String,

    @Schema(description = "Email address", example = "john.doe@example.com", required = true)
    val email: String,

    @Schema(description = "Phone number", example = "+1234567890", required = false)
    val phoneNumber: String?,

    @Schema(description = "Username", example = "johnDoe", required = true)
    val username: String,
)