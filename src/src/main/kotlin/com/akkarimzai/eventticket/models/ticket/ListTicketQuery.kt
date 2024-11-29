package com.akkarimzai.eventticket.models.ticket

import com.akkarimzai.eventticket.models.common.AbstractValidatableCQ
import org.valiktor.functions.hasSize
import org.valiktor.functions.isGreaterThan
import org.valiktor.validate
import org.valiktor.functions.isGreaterThanOrEqualTo
import org.valiktor.functions.isLessThanOrEqualTo

data class ListTicketQuery(
    val categoryId: Int? = null,

    val eventId: Int? = null,

    val title: String?,

    val page: Int,

    val size: Int
): AbstractValidatableCQ() {
    override fun dataValidator() {
        validate(this) {
            categoryId?.let {
                validate(ListTicketQuery::categoryId)
                    .isGreaterThan(0)
            }

            eventId?.let {
                validate(ListTicketQuery::eventId)
                    .isGreaterThan(0)
            }

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