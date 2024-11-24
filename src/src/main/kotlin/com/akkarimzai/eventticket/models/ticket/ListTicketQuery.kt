package com.akkarimzai.eventticket.models.ticket

import com.akkarimzai.eventticket.models.common.AbstractValidatableCQ
import org.valiktor.functions.hasSize
import org.valiktor.validate
import org.valiktor.functions.isGreaterThanOrEqualTo
import org.valiktor.functions.isLessThanOrEqualTo

data class ListTicketQuery(
    val title: String?,

    val page: Int,

    val size: Int
): AbstractValidatableCQ() {
    override fun dataValidator() {
        validate(this) {
            title?.let {
                validate(ListTicketQuery::title)
                    .hasSize(min = 1, max = 60)
            }

            validate(ListTicketQuery::page)
                .isGreaterThanOrEqualTo(0)

            validate(ListTicketQuery::size)
                .isGreaterThanOrEqualTo(1)
                .isLessThanOrEqualTo(20)
        }
    }
}