package com.akkarimzai.eventticket.models.event

import com.akkarimzai.eventticket.models.common.AbstractValidatableCQ
import org.valiktor.validate
import org.valiktor.functions.hasSize
import org.valiktor.functions.isNotBlank
import org.valiktor.functions.isNotNull
import org.valiktor.functions.validateForEach
import java.time.LocalDateTime

data class CreateEventCommand(
    val title: String,
    val artist: String?,
    val date: LocalDateTime,
    val tickets: List<EventTicketDto>
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