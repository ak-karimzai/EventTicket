package com.akkarimzai.eventticket.models.event

import com.akkarimzai.eventticket.models.common.AbstractValidatableCQ
import org.valiktor.functions.hasSize
import org.valiktor.functions.isGreaterThanOrEqualTo
import org.valiktor.functions.isNotBlank
import org.valiktor.functions.isNotNull
import org.valiktor.validate

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

@Schema(description = "Event ticket data transfer object")
data class EventTicketDto(
    @Schema(description = "Title of the ticket", example = "Example Ticket", required = true)
    @get:NotBlank
    @get:Size(min = 3, max = 256)
    val title: String,

    @Schema(description = "Description of the ticket", example = "Example Description", required = false)
    @Size(min = 3, max = 256)
    val description: String?,

    @Schema(description = "Price of the ticket", example = "10.99", required = true)
    @get:NotNull
    @get:DecimalMin(value = "0.0")
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