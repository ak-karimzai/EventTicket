package com.akkarimzai.eventticket.models.ticket

import com.akkarimzai.eventticket.models.common.AbstractValidatableCQ
import org.valiktor.functions.hasSize
import org.valiktor.functions.isGreaterThanOrEqualTo
import org.valiktor.validate

data class CreateTicketCommand(
    val title: String,
    val description: String?,
    val price: Double
) : AbstractValidatableCQ() {
    override fun dataValidator() {
        validate(this) {
            validate(CreateTicketCommand::title)
                .hasSize(min = 3, max = 256)

            description?.let {
                validate(CreateTicketCommand::description)
                    .hasSize(min = 3, max = 256)
            }

            validate(CreateTicketCommand::price)
                .isGreaterThanOrEqualTo(0.0)
        }
    }
}