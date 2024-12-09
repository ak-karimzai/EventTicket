package com.akkarimzai.eventticket.models.ticket

import com.akkarimzai.eventticket.models.common.AbstractValidatableCQ
import org.valiktor.functions.hasSize
import org.valiktor.functions.isGreaterThanOrEqualTo
import org.valiktor.validate

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.Size

@Schema(description = "Command to create a ticket")
data class CreateTicketCommand(
    @Schema(description = "Ticket title", example = "Example Event", required = true)
    @get:Size(min = 3, max = 256)
    val title: String,

    @Schema(description = "Ticket description", example = "This is an example event", required = false)
    @Size(min = 3, max = 256)
    val description: String?,

    @Schema(description = "Ticket price", example = "10.99", required = true)
    @get:DecimalMin(value = "0.0")
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