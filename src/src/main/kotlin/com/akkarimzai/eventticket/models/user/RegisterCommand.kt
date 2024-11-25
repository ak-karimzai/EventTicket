package com.akkarimzai.eventticket.models.user

import com.akkarimzai.eventticket.models.common.AbstractValidatableCQ
import org.valiktor.functions.hasSize
import org.valiktor.functions.isEmail
import org.valiktor.functions.isNotBlank
import org.valiktor.validate
data class RegisterCommand(
    val name: String,

    val email: String,

    val phoneNumber: String? = null,

    val username: String,

    val password: String,
): AbstractValidatableCQ() {
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