package com.akkarimzai.eventticket.models.event

import com.akkarimzai.eventticket.exceptions.BadRequestException
import com.akkarimzai.eventticket.models.common.AbstractValidatableCQ
import org.valiktor.functions.hasSize
import org.valiktor.functions.isNotBlank
import org.valiktor.validate
import java.time.LocalDateTime

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

@Schema(description = "Command to update an event")
data class UpdateEventCommand(
    @Schema(description = "New title of the event", example = "Example Event", required = false)
    @NotBlank
    @Size(min = 3, max = 256)
    val title: String?,

    @Schema(description = "New artist for the event", example = "Example Artist", required = false)
    @NotBlank
    @Size(min = 3, max = 256)
    val artist: String?,

    @Schema(description = "New date and time of the event", example = "2022-01-01T12:00:00", required = false)
    val date: LocalDateTime?
) : AbstractValidatableCQ() {
    override fun dataValidator() {
        validate(this) {
            var count = 0
            title?.let {
                validate(UpdateEventCommand::title)
                    .isNotBlank()
                    .hasSize(min = 3, max = 256)
                count++
            }

            artist?.let {
                validate(UpdateEventCommand::artist)
                    .isNotBlank()
                    .hasSize(min = 3, max = 256)
                count++
            }

            date?.let {
                count++
            }

            if (count == 0) {
                throw BadRequestException("Nothing to update!")
            }
        }
    }
}