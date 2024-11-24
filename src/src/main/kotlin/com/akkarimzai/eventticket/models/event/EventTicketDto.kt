package com.akkarimzai.eventticket.models.event

import com.akkarimzai.eventticket.models.common.AbstractValidatableCQ
import org.valiktor.functions.hasSize
import org.valiktor.functions.isGreaterThanOrEqualTo
import org.valiktor.functions.isNotBlank
import org.valiktor.functions.isNotNull
import org.valiktor.validate

data class EventTicketDto(
    val title: String,
    val description: String?,
    val price: Double
) : AbstractValidatableCQ() {
    override fun dataValidator() {
        validate(this) {
            validate(EventTicketDto::title)
                .isNotBlank()
                .hasSize(min = 3, max = 256)

            description?.let {
                validate(EventTicketDto::description)
                    .hasSize(min = 3, max = 256)
            }

            validate(EventTicketDto::price)
                .isNotNull()
                .isGreaterThanOrEqualTo(0.0)
        }
    }
}
