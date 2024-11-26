package com.akkarimzai.eventticket.models.event

import com.akkarimzai.eventticket.models.common.AbstractValidatableCQ
import org.valiktor.functions.hasSize
import org.valiktor.functions.isGreaterThan
import org.valiktor.functions.isGreaterThanOrEqualTo
import org.valiktor.functions.isLessThanOrEqualTo
import org.valiktor.validate
import java.time.LocalDateTime

data class ListEventQuery(
    val categoryId: Long?,
    val title: String?,
    val artist: String?,
    val from: LocalDateTime?,
    val to: LocalDateTime?,
    val page: Int,
    val size: Int
) : AbstractValidatableCQ() {
    override fun dataValidator() {
        validate(this) {
            categoryId?.let {
                validate(ListEventQuery::categoryId)
                    .isGreaterThan(0)
            }

            title?.let {
                validate(ListEventQuery::title)
                    .hasSize(min = 1, max = 256)
            }

            artist.let {
                validate(ListEventQuery::artist)
                    .hasSize(min = 1, max = 256)
            }

            validate(ListEventQuery::page)
                .isGreaterThanOrEqualTo(0)

            validate(ListEventQuery::size)
                .isGreaterThanOrEqualTo(1)
                .isLessThanOrEqualTo(20)
        }
    }
}
