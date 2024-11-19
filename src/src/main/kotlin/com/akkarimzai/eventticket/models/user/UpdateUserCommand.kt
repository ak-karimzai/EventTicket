package com.akkarimzai.eventticket.models.user

import com.akkarimzai.eventticket.annotations.AtLeastOneNotNull
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

@AtLeastOneNotNull
class UpdateUserCommand(
    @NotBlank @Size(min = 3, max = 60)
    val name: String?,

    @Email
    val email: String?,

    @Size(min = 5, max = 40)
    val phoneNumber: String?,

    @NotBlank @Size(min = 3, max = 60)
    val username: String?,

    @NotBlank @Size(min = 6, max = 60)
    val password: String?,
)