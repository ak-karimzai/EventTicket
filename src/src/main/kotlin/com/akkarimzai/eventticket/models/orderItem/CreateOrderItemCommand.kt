package com.akkarimzai.eventticket.models.orderItem

import com.akkarimzai.eventticket.models.common.AbstractValidatableCQ
import org.valiktor.functions.isGreaterThan
import org.valiktor.validate

data class CreateOrderItemCommand(
    val ticketId: Long,
    val amount: Int
): AbstractValidatableCQ() {
    override fun dataValidator() {
        validate(this) {
            validate(CreateOrderItemCommand::ticketId)
                .isGreaterThan(0)

            validate(CreateOrderItemCommand::amount)
                .isGreaterThan(0)
        }
    }
}