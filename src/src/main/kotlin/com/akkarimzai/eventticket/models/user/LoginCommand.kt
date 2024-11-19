package com.akkarimzai.eventticket.models.user

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class LoginCommand(
    @NotBlank @Size(min = 3, max = 60)
    val username: String,

    @NotBlank @Size(min = 6, max = 60)
    val password: String
)