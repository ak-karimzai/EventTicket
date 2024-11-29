package com.akkarimzai.eventticket.models.orderItem

import com.akkarimzai.eventticket.models.common.AbstractValidatableCQ
import org.valiktor.functions.isGreaterThan
import org.valiktor.validate

data class DeleteOrderItemCommand(
    val orderItemId: Long
): AbstractValidatableCQ() {
    override fun dataValidator() {
        validate(this) {
            validate(DeleteOrderItemCommand::orderItemId)
                .isGreaterThan(0)
        }
    }
}
