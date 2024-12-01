package com.akkarimzai.eventticket.models.order

import com.akkarimzai.eventticket.models.common.AbstractValidatableCQ
import org.valiktor.functions.isGreaterThanOrEqualTo
import org.valiktor.functions.isLessThanOrEqualTo
import org.valiktor.validate

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min

@Schema(description = "Query to list orders")
data class ListOrderQuery(
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
            validate(ListOrderQuery::page)
                .isGreaterThanOrEqualTo(0)

            validate(ListOrderQuery::size)
                .isGreaterThanOrEqualTo(1)
                .isLessThanOrEqualTo(20)
        }
    }
}