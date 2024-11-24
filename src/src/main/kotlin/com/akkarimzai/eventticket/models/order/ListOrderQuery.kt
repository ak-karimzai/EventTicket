package com.akkarimzai.eventticket.models.order

import com.akkarimzai.eventticket.models.common.AbstractValidatableCQ
import org.valiktor.functions.isGreaterThanOrEqualTo
import org.valiktor.functions.isLessThanOrEqualTo
import org.valiktor.validate

data class ListOrderQuery(
    val page: Int,
    val size: Int
): AbstractValidatableCQ() {
    override fun dataValidator() {
        validate(this) {
            validate(ListOrderQuery::page)
                .isGreaterThanOrEqualTo(0)

            validate(ListOrderQuery::size)
                .isGreaterThanOrEqualTo(1)
                .isLessThanOrEqualTo(20)
        }
    }
}
