package com.akkarimzai.eventticket.models.orderItem

import com.akkarimzai.eventticket.models.common.AbstractValidatableCQ
import org.valiktor.functions.isGreaterThan
import org.valiktor.validate

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Min

@Schema(description = "Command to delete an order item")
data class DeleteOrderItemCommand(
    @Schema(description = "Order item ID", example = "1", required = true)
    @get:Min(1)
    val orderItemId: Long
) : AbstractValidatableCQ() {
    override fun dataValidator() {
        validate(this) {
            validate(DeleteOrderItemCommand::orderItemId)
                .isGreaterThan(0)
        }
    }
}