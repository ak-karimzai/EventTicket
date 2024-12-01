package com.akkarimzai.eventticket

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
@OpenAPIDefinition(
    info = Info(
        title = "Event Ticket API",
        version = "1.0",
        description = "API for Event Ticket"
    )
)
class EventTicketApplication

fun main(args: Array<String>) {
    runApplication<EventTicketApplication>(*args)
}