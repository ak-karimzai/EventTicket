package com.akkarimzai.eventticket.models.order

import com.akkarimzai.eventticket.models.common.AbstractValidatableCQ
import com.akkarimzai.eventticket.models.orderItem.CreateOrderItemCommand
import org.valiktor.functions.hasSize
import org.valiktor.functions.validateForEach
import org.valiktor.validate

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Size

@Schema(description = "Command to create an order")
data class CreateOrderCommand(
    @Schema(description = "List of order items", example = "[{...}]", required = true)
    @Size(min = 1, max = 20)
    val items: List<CreateOrderItemCommand>
) : AbstractValidatableCQ() {
    override fun dataValidator() {
        validate(this) {
            validate(CreateOrderCommand::items)
                .hasSize(min = 1, max = 20)
                .validateForEach { it.validate()}
        }
    }
}