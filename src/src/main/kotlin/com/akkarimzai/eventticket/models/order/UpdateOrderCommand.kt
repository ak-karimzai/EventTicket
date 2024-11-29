package com.akkarimzai.eventticket.models.order

import com.akkarimzai.eventticket.models.common.AbstractValidatableCQ
import com.akkarimzai.eventticket.models.orderItem.CreateOrderItemCommand
import org.apache.coyote.BadRequestException
import org.valiktor.functions.hasSize
import org.valiktor.functions.validateForEach
import org.valiktor.validate

data class UpdateOrderCommand(
    val items: List<CreateOrderItemCommand>?
): AbstractValidatableCQ() {
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