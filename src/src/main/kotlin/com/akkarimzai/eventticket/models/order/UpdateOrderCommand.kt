package com.akkarimzai.eventticket.models.order

import com.akkarimzai.eventticket.models.common.AbstractValidatableCQ
import com.akkarimzai.eventticket.models.orderItem.CreateOrderItemCommand
import com.akkarimzai.eventticket.models.orderItem.UpdateOrderItemCommand
import org.apache.coyote.BadRequestException
import org.valiktor.functions.hasSize
import org.valiktor.functions.validateForEach
import org.valiktor.validate

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.Size
import org.valiktor.functions.isGreaterThan

@Schema(description = "Command to update an order")
data class UpdateOrderCommand(
    @Schema(description = "List of order items to update", example = "[{...}]", required = false)
    @Size(min = 1, max = 20)
    val items: List<UpdateOrderItem>?
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
    data class UpdateOrderItem(
        @Schema(description = "Order item ID", example = "1", required = true)
        val orderItemId: Long,
        @Schema(description = "New amount", example = "2", required = true)
        @get:Min(1)
        val amount: Int
    ): AbstractValidatableCQ() {
        override fun dataValidator() {
            validate(this) {
                validate(UpdateOrderItem::orderItemId)
                    .isGreaterThan(0)

                validate(UpdateOrderItem::amount)
                    .isGreaterThan(0)
            }
        }
    }
}