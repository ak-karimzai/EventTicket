package com.akkarimzai.eventticket.models.user

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import org.hibernate.validator.constraints.Length

data class RegisterCommand(
    @NotBlank @Length(min = 3, max = 60)
    val name: String,

    @Email
    val email: String,

    @Length(min = 5, max = 40)
    val phoneNumber: String? = null,

    @NotBlank @Length(min = 3, max = 60)
    val username: String,

    @NotBlank @Length(min = 6, max = 60)
    val password: String,
)