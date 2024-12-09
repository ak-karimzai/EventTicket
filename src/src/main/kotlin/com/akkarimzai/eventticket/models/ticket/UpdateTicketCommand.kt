package com.akkarimzai.eventticket.models.ticket

import com.akkarimzai.eventticket.models.common.AbstractValidatableCQ
import org.apache.coyote.BadRequestException
import org.valiktor.validate
import org.valiktor.functions.hasSize
import org.valiktor.functions.isGreaterThanOrEqualTo

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.Size

@Schema(description = "Command to update a ticket")
class UpdateTicketCommand(
    @Schema(description = "New ticket title", example = "Updated Event", required = false)
    @Size(min = 3, max = 256)
    val title: String?,

    @Schema(description = "New ticket description", example = "This is an updated event", required = false)
    @Size(min = 3, max = 256)
    val description: String?,

    @Schema(description = "New ticket price", example = "11.99", required = false)
    @get:DecimalMin(value = "0.0")
    val price: Double?
) : AbstractValidatableCQ() {
    override fun dataValidator() {
        validate(this) {
            var count = 0
            title?.let {
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