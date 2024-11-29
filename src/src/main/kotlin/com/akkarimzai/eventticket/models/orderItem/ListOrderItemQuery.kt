package com.akkarimzai.eventticket.models.orderItem

import com.akkarimzai.eventticket.models.common.AbstractValidatableCQ
import org.valiktor.functions.isGreaterThanOrEqualTo
import org.valiktor.functions.isLessThanOrEqualTo
import org.valiktor.validate

data class ListOrderItemQuery(
    val orderId: Long,
    val page: Int,
    val size: Int
): AbstractValidatableCQ() {
    override fun dataValidator() {
        validate(this) {
            validate(ListOrderItemQuery::orderId)
                .isGreaterThanOrEqualTo(0)

            validate(ListOrderItemQuery::page)
                .isGreaterThanOrEqualTo(0)

            validate(ListOrderItemQuery::size)
                .isGreaterThanOrEqualTo(1)
                .isLessThanOrEqualTo(20)
        }
    }
}