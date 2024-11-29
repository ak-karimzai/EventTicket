package com.akkarimzai.eventticket.models.user

import com.akkarimzai.eventticket.models.common.AbstractValidatableCQ
import org.valiktor.functions.hasSize
import org.valiktor.validate

data class LoginCommand(
    val username: String,
    val password: String
): AbstractValidatableCQ() {
    override fun dataValidator() {
        validate(this) {
            validate(LoginCommand::username)
                .hasSize(min = 3, max = 60)

            validate(LoginCommand::password)
                .hasSize(min = 6, max = 60)
        }
    }
}