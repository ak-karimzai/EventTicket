package com.akkarimzai.eventticket.models.order

import com.akkarimzai.eventticket.models.common.AbstractValidatableCQ
import org.valiktor.functions.isGreaterThanOrEqualTo
import org.valiktor.validate

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Min

@Schema(description = "Command item data transfer object")
data class CommandItemDto(
    @Schema(description = "Ticket ID", example = "1", required = true)
    @get:Min(1)
    val ticketId: Long,

    @Schema(description = "Number of tickets", example = "2", required = true)
    @get:Min(0)
    val count: Int
) : AbstractValidatableCQ() {
    override fun dataValidator() {
        validate(this) {
            validate(CommandItemDto::ticketId)
                .isGreaterThanOrEqualTo(1)

            validate(CommandItemDto::count)
                .isGreaterThanOrEqualTo(0)
        }
    }
}