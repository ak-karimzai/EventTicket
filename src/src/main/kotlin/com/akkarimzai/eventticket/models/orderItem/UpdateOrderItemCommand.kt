package com.akkarimzai.eventticket.models.orderItem

import com.akkarimzai.eventticket.models.common.AbstractValidatableCQ
import org.valiktor.functions.isGreaterThan
import org.valiktor.validate

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Min

@Schema(description = "Command to update an order item")
data class UpdateOrderItemCommand(
    @Schema(description = "New amount", example = "2", required = true)
    @get:Min(1)
    val amount: Int
) : AbstractValidatableCQ() {
    override fun dataValidator() {
        validate(this) {
            validate(UpdateOrderItemCommand::amount)
                .isGreaterThan(0)
        }
    }
}