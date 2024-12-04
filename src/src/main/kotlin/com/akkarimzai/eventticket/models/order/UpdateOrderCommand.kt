package com.akkarimzai.eventticket.models.order

import com.akkarimzai.eventticket.models.common.AbstractValidatableCQ
import com.akkarimzai.eventticket.models.orderItem.CreateOrderItemCommand
import com.akkarimzai.eventticket.models.orderItem.UpdateOrderItemCommand
import org.apache.coyote.BadRequestException
import org.valiktor.functions.hasSize
import org.valiktor.functions.validateForEach
import org.valiktor.validate

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Size

@Schema(description = "Command to update an order")
data class UpdateOrderCommand(
    @Schema(description = "List of order items to update", example = "[{...}]", required = false)
    @Size(min = 1, max = 20)
    val items: List<UpdateOrderItemCommand>?
) : AbstractValidatableCQ() {
    override fun dataValidator() {
        validate(this) {
            var count = 0
            items?.let {
                validate(UpdateOrderCommand::items)
                    .hasSize(min = 1, max = 20)
                    .validateForEach { it.validate() }
                count++
            }
            if (count == 0) {
                throw BadRequestException("Nothing to update!")
            }
        }
    }
}