package com.akkarimzai.eventticket.models.user

import com.akkarimzai.eventticket.models.common.AbstractValidatableCQ
import org.valiktor.functions.hasSize
import org.valiktor.validate

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Size

@Schema(description = "Command to login")
data class LoginCommand(
    @Schema(description = "Username", example = "johnDoe", required = true)
    @get:Size(min = 3, max = 60)
    val username: String,

    @Schema(description = "Password", example = "password123", required = true)
    @get:Size(min = 6, max = 60)
    val password: String
) : AbstractValidatableCQ() {
    override fun dataValidator() {
        validate(this) {
            validate(LoginCommand::username)
                .hasSize(min = 3, max = 60)

            validate(LoginCommand::password)
                .hasSize(min = 6, max = 60)
        }
    }
}