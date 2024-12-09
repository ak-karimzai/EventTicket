package com.akkarimzai.eventticket.models.event

import com.akkarimzai.eventticket.models.common.AbstractValidatableCQ
import com.akkarimzai.eventticket.models.ticket.CreateTicketCommand
import org.valiktor.validate
import org.valiktor.functions.hasSize
import org.valiktor.functions.isNotBlank
import org.valiktor.functions.isNotNull
import org.valiktor.functions.validateForEach
import java.time.LocalDateTime

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

@Schema(description = "Command to create a new event")
data class CreateEventCommand(
    @Schema(description = "Title of the event", example = "Example Event", required = true)
    @get:NotBlank
    @get:Size(min = 3, max = 256)
    val title: String,

    @Schema(description = "Artist performing at the event", example = "Example Artist", required = false)
    @get:NotBlank
    @get:Size(min = 3, max = 256)
    val artist: String?,

    @Schema(description = "Date and time of the event", example = "2022-01-01T12:00:00", required = true)
    val date: LocalDateTime,

    @Schema(description = "List of tickets for the event", example = "[{...}]", required = true)
    @get:NotNull
    val tickets: List<CreateTicketCommand>
) : AbstractValidatableCQ() {
    override fun dataValidator() {
        validate(this) {
            validate(CreateEventCommand::title)
                .isNotBlank()
                .hasSize(min = 3, max = 256)

            artist?.let {
                validate(CreateEventCommand::artist)
                    .isNotBlank()
                    .hasSize(min = 3, max = 256)
            }

            validate(CreateEventCommand::tickets)
                .isNotNull()
                .validateForEach { it.validate() }
        }
    }
}