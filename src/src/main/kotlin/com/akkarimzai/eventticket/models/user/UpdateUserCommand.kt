package com.akkarimzai.eventticket.models.user

import com.akkarimzai.eventticket.exceptions.BadRequestException
import com.akkarimzai.eventticket.models.common.AbstractValidatableCQ
import org.valiktor.validate
import org.valiktor.functions.hasSize
import org.valiktor.functions.isEmail
import org.valiktor.functions.isNotBlank

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

@Schema(description = "Command to update a user")
class UpdateUserCommand(
    @Schema(description = "New full name", example = "John Doe", required = false)
    @NotBlank
    @Size(min = 3, max = 60)
    val name: String?,

    @Schema(description = "New email address", example = "john.doe@example.com", required = false)
    @NotBlank
    @Email
    val email: String?,

    @Schema(description = "New phone number", example = "+1234567890", required = false)
    @NotBlank
    @Size(min = 5, max = 40)
    val phoneNumber: String?,

    @Schema(description = "New username", example = "johnDoe", required = false)
    @NotBlank
    @Size(min = 3, max = 60)
    val username: String?,

    @Schema(description = "New password", example = "password123", required = false)
    @NotBlank
    @Size(min = 6, max = 60)
    val password: String?
) : AbstractValidatableCQ() {
    override fun dataValidator() {
        validate(this) {
            var count = 0
            name?.let {
                validate(UpdateUserCommand::name)
                    .isNotBlank()
                    .hasSize(min = 3, max = 60)
                count++
            }

            email?.let {
                validate(UpdateUserCommand::email)
                    .isNotBlank()
                    .isEmail()
                count++
            }

            phoneNumber?.let {
                validate(UpdateUserCommand::phoneNumber)
                    .isNotBlank()
                    .hasSize(min = 5, max = 40)
                count++
            }

            username?.let {
                validate(UpdateUserCommand::username)
                    .isNotBlank()
                    .hasSize(min = 3, max = 60)
                count++
            }

            password?.let {
                validate(UpdateUserCommand::password)
                    .isNotBlank()
                    .hasSize(min = 6, max = 60)
                count++
            }

            if (count == 0) {
                throw BadRequestException("Nothing to update!")
            }
        }
    }
}