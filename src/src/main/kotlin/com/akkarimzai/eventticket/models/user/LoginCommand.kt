package com.akkarimzai.eventticket.models.user

import jakarta.validation.constraints.NotBlank
import org.hibernate.validator.constraints.Length

data class LoginCommand(
    @NotBlank @Length(min = 3, max = 60)
    val username: String,

    @NotBlank @Length(min = 6, max = 60)
    val password: String
)