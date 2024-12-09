package com.akkarimzai.eventticket.models.ticket

import com.akkarimzai.eventticket.models.common.AbstractValidatableCQ
import org.valiktor.functions.hasSize
import org.valiktor.functions.isGreaterThan
import org.valiktor.validate
import org.valiktor.functions.isGreaterThanOrEqualTo
import org.valiktor.functions.isLessThanOrEqualTo

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.Size

@Schema(description = "Query to list tickets")
data class ListTicketQuery(
    @Schema(description = "Category ID", example = "1", required = false)
    @get:Min(1)
    val categoryId: Int? = null,

    @Schema(description = "Event ID", example = "1", required = false)
    @get:Min(1)
    val eventId: Int? = null,

    @Schema(description = "Ticket title", example = "Example Event", required = false)
    @Size(min = 1, max = 60)
    val title: String?,

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