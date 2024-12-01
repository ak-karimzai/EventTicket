package com.akkarimzai.eventticket.models.event

import com.akkarimzai.eventticket.models.common.AbstractValidatableCQ
import org.valiktor.functions.hasSize
import org.valiktor.functions.isGreaterThan
import org.valiktor.functions.isGreaterThanOrEqualTo
import org.valiktor.functions.isLessThanOrEqualTo
import org.valiktor.validate
import java.time.LocalDateTime

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.Size

@Schema(description = "Query to list events")
data class ListEventQuery(
    @Schema(description = "Category ID filter", example = "1", required = false)
    @Min(1)
    val categoryId: Long? = null,

    @Schema(description = "Title filter", example = "Example Event", required = false)
    @Size(min = 1, max = 256)
    val title: String?,

    @Schema(description = "Artist filter", example = "Example Artist", required = false)
    @Size(min = 1, max = 256)
    val artist: String?,

    @Schema(description = "Start date filter", example = "2022-01-01T12:00:00", required = false)
    val from: LocalDateTime?,

    @Schema(description = "End date filter", example = "2022-01-31T12:00:00", required = false)
    val to: LocalDateTime?,

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
            categoryId?.let {
                validate(ListEventQuery::categoryId)
                    .isGreaterThan(0)
            }

            title?.let {
                validate(ListEventQuery::title)
                    .hasSize(min = 1, max = 256)
            }

            artist?.let {
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