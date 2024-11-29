package com.akkarimzai.eventticket.models.event

import com.akkarimzai.eventticket.exceptions.BadRequestException
import com.akkarimzai.eventticket.models.common.AbstractValidatableCQ
import org.valiktor.functions.hasSize
import org.valiktor.functions.isNotBlank
import org.valiktor.validate
import java.time.LocalDateTime

class UpdateEventCommand(
    val title: String?,

    val artist: String?,

    val date: LocalDateTime?
): AbstractValidatableCQ() {
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
                // ToDo logic to validate date
                count++
            }

            if (count == 0) {
                throw BadRequestException("Nothing to update!")
            }
        }
    }
}