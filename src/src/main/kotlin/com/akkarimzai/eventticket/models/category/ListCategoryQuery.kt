package com.akkarimzai.eventticket.models.category

import com.akkarimzai.eventticket.models.common.AbstractValidatableCQ
import org.valiktor.functions.hasSize
import org.valiktor.functions.isGreaterThanOrEqualTo
import org.valiktor.functions.isLessThanOrEqualTo
import org.valiktor.validate

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.Size

@Schema(description = "Query to list categories")
data class ListCategoryQuery(
    @Schema(description = "Optional title filter", example = "Example Category")
    @get:Size(min = 1, max = 126)
    val title: String?,

    @Schema(description = "Page number (0-based)", example = "0", required = true)
    @get:Min(0)
    val page: Int,

    @Schema(description = "Page size (1-20)", example = "10", required = true)
    @get:Min(1)
    @get:Max(20)
    val size: Int
): AbstractValidatableCQ() {
    override fun dataValidator() {
        validate(this) {
            title?.let {
                validate(ListCategoryQuery::title)
                    .hasSize(min = 1, max = 126)
            }

            validate(ListCategoryQuery::page)
                .isGreaterThanOrEqualTo(0)

            validate(ListCategoryQuery::size)
                .isGreaterThanOrEqualTo(1)
                .isLessThanOrEqualTo(20)
        }
    }
}