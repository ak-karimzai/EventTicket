package com.akkarimzai.eventticket.models.orderItem

import com.akkarimzai.eventticket.models.common.AbstractValidatableCQ
import org.valiktor.functions.isGreaterThan
import org.valiktor.validate

data class UpdateOrderItemCommand(
    val orderItemId: Long,
    val amount: Int
): AbstractValidatableCQ() {
    override fun dataValidator() {
        validate(this) {
            validate(UpdateOrderItemCommand::orderItemId)
                .isGreaterThan(0)

            validate(UpdateOrderItemCommand::amount)
                .isGreaterThan(0)
        }
    }
}