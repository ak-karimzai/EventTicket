package com.akkarimzai.eventticket.models.order

import com.akkarimzai.eventticket.models.common.AbstractValidatableCQ
import com.akkarimzai.eventticket.models.orderItem.CreateOrderItemCommand
import org.valiktor.functions.hasSize
import org.valiktor.functions.validateForEach
import org.valiktor.validate

data class CreateOrderCommand(
    val items: List<CreateOrderItemCommand>
): AbstractValidatableCQ() {
    override fun dataValidator() {
        validate(this) {
            validate(CreateOrderCommand::items)
                .hasSize(min = 1, max = 20)
                .validateForEach { it.validate() }
        }
    }
}