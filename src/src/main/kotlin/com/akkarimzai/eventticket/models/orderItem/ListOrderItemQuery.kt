package com.akkarimzai.eventticket.models.orderItem

import com.akkarimzai.eventticket.models.common.AbstractValidatableCQ
import org.valiktor.functions.isGreaterThanOrEqualTo
import org.valiktor.functions.isLessThanOrEqualTo
import org.valiktor.validate

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min

@Schema(description = "Query to list order items")
data class ListOrderItemQuery(
    @Schema(description = "Order ID", example = "1", required = true)
    @get:Min(0)
    val orderId: Long,

    @Schema(description = "Page number (0-based)", example = "0", required = true)
    @get:Min(0)
    val page: Int,

    @Schema(description = "Page size (1-20)", example = "10", required = true)
    @get:Min(1)
    @get:Max(20)
    val size: Int
) : AbstractValidatableCQ() {
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