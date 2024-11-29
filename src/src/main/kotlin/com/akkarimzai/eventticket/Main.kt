package com.akkarimzai.eventticket

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class EventTicketApplication

fun main(args: Array<String>) {
    runApplication<EventTicketApplication>(*args)
}