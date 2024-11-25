package com.akkarimzai.eventticket.services.impl

import com.akkarimzai.eventticket.entities.Category
import com.akkarimzai.eventticket.entities.Event
import com.akkarimzai.eventticket.entities.Ticket
import com.akkarimzai.eventticket.exceptions.BadRequestException
import com.akkarimzai.eventticket.exceptions.NotFoundException
import com.akkarimzai.eventticket.models.ticket.CreateTicketCommand
import com.akkarimzai.eventticket.models.ticket.ListTicketQuery
import com.akkarimzai.eventticket.models.ticket.UpdateTicketCommand
import com.akkarimzai.eventticket.profiles.toDto
import com.akkarimzai.eventticket.repositories.EventRepository
import com.akkarimzai.eventticket.repositories.TicketRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.*
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import java.time.LocalDateTime
import java.util.*

class TicketServiceTest : FunSpec({
    val ticketRepository = mockk<TicketRepository>()
    val eventRepository = mockk<EventRepository>()
    val ticketService = TicketService(ticketRepository, eventRepository)

    val eventId = 1L
    val ticketId = 100L
    val categoryId = 1L
    val category = Category(id = categoryId, title = "Category 1")
    val event = Event(id = 1L, title = "Event 1", artist = "Artist 1", date = LocalDateTime.now(), category = category)
    val ticket = Ticket(id = ticketId, event = event, title = "Test Ticket", price = 43.2)
    val ticketDto = ticket.toDto()

    test("should load ticket by id") {
        // Arrange
        every { ticketRepository.findById(ticketId) } returns Optional.of(ticket)

        // Act
        val result = ticketService.load(eventId, ticketId)

        // Assert
        result shouldBe ticketDto
        verify { ticketRepository.findById(ticketId) }
    }

    test("should throw NotFoundException when ticket not found") {
        // Arrange
        every { ticketRepository.findById(ticketId) } returns Optional.empty()

        // Act & Assert
        shouldThrow<NotFoundException> {
            ticketService.load(eventId, ticketId)
        }
    }

    test("should create a new ticket") {
        // Arrange
        val createTicketCommand = CreateTicketCommand(title = "New Ticket", description = null, price = 43.2)
        every { eventRepository.findById(eventId) } returns Optional.of(event)
        every { ticketRepository.save(any()) } returns ticket

        // Act
        val result = ticketService.create(eventId, createTicketCommand)

        // Assert
        result shouldBe ticketId
        verify { eventRepository.findById(eventId) }
        verify { ticketRepository.save(any()) }
    }

    test("should update an existing ticket") {
        // Arrange
        val updateTicketCommand = UpdateTicketCommand(title = "Updated Ticket", description = null, price = null)
        every { ticketRepository.findById(ticketId) } returns Optional.of(ticket)
        every { ticketRepository.save(any()) } returns ticket

        // Act
        ticketService.update(eventId, ticketId, updateTicketCommand)

        // Assert
        verify { ticketRepository.save(any()) }
    }

    test("should list tickets with query") {
        // Arrange
        val tickets = listOf(ticket)
        val page = PageImpl(tickets)
        val listTicketQuery = ListTicketQuery(page = 0, size = 10, title = "Test")
        every { ticketRepository.findAll(any(), any<PageRequest>()) } returns page

        // Act
        val result = ticketService.list(eventId, listTicketQuery)

        // Assert
        result.content.size shouldBe 1
        verify { ticketRepository.findAll(any(), any<PageRequest>()) }
    }

    test("should throw BadRequestException when event id is invalid") {
        // Arrange && Act && Assert
        shouldThrow<BadRequestException> {
            ticketService.load(0, ticketId)
        }
    }

    test("should throw NotFoundException when ticket's event id doesn't match") {
        // Arrange
        val invalidTicket = Ticket(id = ticketId, title = "Wrong Event",
            description = null, price = 45.2, event = event)
        every { ticketRepository.findById(ticketId) } returns Optional.of(invalidTicket)

        // Act && Assert
        shouldThrow<NotFoundException> {
            ticketService.load(eventId, ticketId)
        }
    }
})
