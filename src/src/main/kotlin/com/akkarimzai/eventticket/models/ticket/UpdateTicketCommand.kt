package com.akkarimzai.eventticket.models.ticket

import com.akkarimzai.eventticket.models.common.AbstractValidatableCQ
import org.apache.coyote.BadRequestException
import org.valiktor.validate
import org.valiktor.functions.hasSize
import org.valiktor.functions.isGreaterThanOrEqualTo

class UpdateTicketCommand(
    val title: String?,
    val description: String?,
    val price: Double?,
): AbstractValidatableCQ() {
    override fun dataValidator() {
        validate(this) {
            var count = 0
            description?.let {
                validate(UpdateTicketCommand::title)
                    .hasSize(min = 3, max = 256)
                count++
            }

            description?.let {
                validate(UpdateTicketCommand::description)
                    .hasSize(min = 3, max = 256)
                count++
            }

            price?.let {
                validate(UpdateTicketCommand::price)
                    .isGreaterThanOrEqualTo(0.0)
                count++
            }

            if (count == 0) {
                throw BadRequestException("Nothing to update!")
            }
        }
    }
}