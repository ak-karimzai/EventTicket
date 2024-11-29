package com.akkarimzai.eventticket.models.user

import com.akkarimzai.eventticket.exceptions.BadRequestException
import com.akkarimzai.eventticket.models.common.AbstractValidatableCQ
import org.valiktor.validate
import org.valiktor.functions.hasSize
import org.valiktor.functions.isEmail
import org.valiktor.functions.isNotBlank

class UpdateUserCommand(
    val name: String?,
    val email: String?,
    val phoneNumber: String?,
    val username: String?,
    val password: String?,
): AbstractValidatableCQ() {
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