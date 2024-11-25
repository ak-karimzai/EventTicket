package com.akkarimzai.eventticket.models.order

import com.akkarimzai.eventticket.models.common.AbstractValidatableCQ
import org.valiktor.functions.isGreaterThanOrEqualTo
import org.valiktor.validate

data class CommandItemDto(
    val ticketId: Long,
    val count: Int
): AbstractValidatableCQ() {
    override fun dataValidator() {
        validate(this) {
            validate(CommandItemDto::ticketId)
                .isGreaterThanOrEqualTo(1)

            validate(CommandItemDto::count)
                .isGreaterThanOrEqualTo(0)
        }
    }
}