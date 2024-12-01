package com.akkarimzai.eventticket.models.user

import com.akkarimzai.eventticket.models.common.AbstractValidatableCQ
import org.valiktor.functions.hasSize
import org.valiktor.functions.isEmail
import org.valiktor.functions.isNotBlank
import org.valiktor.validate

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

@Schema(description = "Command to register")
data class RegisterCommand(
    @Schema(description = "Full name", example = "John Doe", required = true)
    @get:NotBlank
    @get:Size(min = 3, max = 60)
    val name: String,

    @Schema(description = "Email address", example = "john.doe@example.com", required = true)
    @get:NotBlank
    @get:Email
    val email: String,

    @Schema(description = "Phone number", example = "+1234567890", required = false)
    @NotBlank
    @Size(min = 5, max = 40)
    val phoneNumber: String? = null,

    @Schema(description = "Username", example = "johnDoe", required = true)
    @get:NotBlank
    @get:Size(min = 3, max = 60)
    val username: String,

    @Schema(description = "Password", example = "password123", required = true)
    @get:NotBlank
    @get:Size(min = 6, max = 60)
    val password: String
) : AbstractValidatableCQ() {
    override fun dataValidator() {
        validate(this) {
            validate(RegisterCommand::name)
                .isNotBlank()
                .hasSize(min = 3, max = 60)

            validate(RegisterCommand::email)
                .isNotBlank()
                .isEmail()

            phoneNumber?.let {
                validate(RegisterCommand::phoneNumber)
                    .isNotBlank()
                    .hasSize(min = 5, max = 40)
            }

            validate(RegisterCommand::username)
                .isNotBlank()
                .hasSize(min = 3, max = 60)

            validate(RegisterCommand::password)
                .isNotBlank()
                .hasSize(min = 6, max = 60)
        }
    }
}