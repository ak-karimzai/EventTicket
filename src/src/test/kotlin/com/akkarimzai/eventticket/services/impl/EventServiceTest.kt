package com.akkarimzai.eventticket.services.impl

import com.akkarimzai.eventticket.entities.Category
import com.akkarimzai.eventticket.entities.Event
import com.akkarimzai.eventticket.entities.Role
import com.akkarimzai.eventticket.entities.Ticket
import com.akkarimzai.eventticket.entities.User
import com.akkarimzai.eventticket.exceptions.BadRequestException
import com.akkarimzai.eventticket.exceptions.NotFoundException
import com.akkarimzai.eventticket.models.event.CreateEventCommand
import com.akkarimzai.eventticket.models.event.ListEventQuery
import com.akkarimzai.eventticket.models.event.UpdateEventCommand
import com.akkarimzai.eventticket.profiles.toDto
import com.akkarimzai.eventticket.repositories.CategoryRepository
import com.akkarimzai.eventticket.repositories.EventRepository
import com.akkarimzai.eventticket.repositories.TicketRepository
import com.akkarimzai.eventticket.services.AuthService
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.data.domain.PageImpl
import java.time.LocalDateTime
import java.util.*

class EventServiceTest : FunSpec({
    val categoryRepository = mockk<CategoryRepository>()
    val eventRepository = mockk<EventRepository>()
    val ticketRepository = mockk<TicketRepository>()
    val authService = mockk<AuthService>()
    val eventService = EventService(categoryRepository, eventRepository, ticketRepository, authService)

    beforeTest {
        clearMocks(categoryRepository, eventRepository)
    }

    test("create event - valid category and command") {
        // Arrange
        val categoryId = 1L
        val command = CreateEventCommand("Event 1", "Artist 1", LocalDateTime.now(), listOf())
        val category = Category(id = categoryId, title = "Category 1")
        val event = Event(id = 1L, title = "Event 1", artist = "Artist 1", date = LocalDateTime.now(), category = category)
        val user = User(id = 1L, username = "username", password = "password",
            role = Role.ADMIN, name = "name", email = "email")

        every { authService.currentUser() } returns user
        every { categoryRepository.findById(categoryId) } returns Optional.of(category)
        every { eventRepository.save(any()) } returns event
        every { ticketRepository.saveAll(any<List<Ticket>>()) } returns listOf()

        // Act
        val result = eventService.create(categoryId, command)

        // Assert
        result shouldBe 1L
        verify { categoryRepository.findById(categoryId) }
        verify { eventRepository.save(any()) }
    }

    test("create event - invalid category id") {
        // Arrange
        val categoryId = 0L
        val command = CreateEventCommand("Event 1", "Artist 1", LocalDateTime.now(), listOf())

        // Act && Assert
        shouldThrow<BadRequestException> {
            eventService.create(categoryId, command)
        }
    }

    test("create event - category not found") {
        // Arrange
        val categoryId = 1L
        val command = CreateEventCommand("Event 1", "Artist 1", LocalDateTime.now(), listOf())

        every { categoryRepository.findById(categoryId) } returns Optional.empty()

        // Act && Assert
        shouldThrow<NotFoundException> {
            eventService.create(categoryId, command)
        }
    }

    test("update event - valid event") {
        // Arrange
        val categoryId = 1L
        val eventId = 1L
        val command = UpdateEventCommand("Updated Event", "Updated Artist", LocalDateTime.now())
        val event = Event(id = eventId, title = "Event 1", artist = "Artist 1", date = LocalDateTime.now(), category = Category(categoryId, "Category 1"))
        val updatedEvent = Event(id = eventId, title = command.title!!, artist = command.artist!!, date = LocalDateTime.now(), category = Category(categoryId, "Category 1"))

        every { eventRepository.findById(eventId) } returns Optional.of(event)
        every { eventRepository.save(any()) } returns updatedEvent

        // Act
        eventService.update(categoryId, eventId, command)

        // Assert
        verify { eventRepository.save(any()) }
    }

    test("update event - event not found") {
        // Arrange
        val categoryId = 1L
        val eventId = 999L
        val command = UpdateEventCommand("Updated Event", "Updated Artist", LocalDateTime.now())

        every { eventRepository.findById(eventId) } returns Optional.empty()

        // Act && Assert
        shouldThrow<NotFoundException> {
            eventService.update(categoryId, eventId, command)
        }
    }

    test("load event - valid event") {
        // Arrange
        val categoryId = 1L
        val eventId = 1L
        val event = Event(id = eventId, title = "Event 1", artist = "Artist 1", date = LocalDateTime.now(), category = Category(categoryId, "Category 1"))

        every { eventRepository.findById(eventId) } returns Optional.of(event)

        // Act
        val result = eventService.load(categoryId, eventId)

        // Assert
        result shouldBe event.toDto()
    }

    test("load event - event not found") {
        // Arrange
        val categoryId = 1L
        val eventId = 999L

        every { eventRepository.findById(eventId) } returns Optional.empty()

        // Act && Assert
        shouldThrow<NotFoundException> {
            eventService.load(categoryId, eventId)
        }
    }

    test("list events - valid query") {
        // Arrange
        val query = ListEventQuery(title = "Event", artist = "Artist", from =  LocalDateTime.now(),
            to = LocalDateTime.now().plusDays(1), page = 0, size = 10)
        val eventPage = PageImpl(listOf(Event(1L, "Event 1", "Artist 1", LocalDateTime.now(), mutableListOf(), Category(1L, "Category 1"))))

        every { eventRepository.findAll(any(), any()) } returns eventPage

        // Act
        val result = eventService.list(query)

        // Assert
        result.totalElements shouldBe 1
        verify { eventRepository.findAll(any(), any()) }
    }

    test("list events - empty result") {
        // Arrange
        val query = ListEventQuery(categoryId = null, "Event", "Artist", LocalDateTime.now(), LocalDateTime.now().plusDays(1), 0, 10)
        val eventPage = PageImpl(listOf<Event>())

        every { eventRepository.findAll(any(), any()) } returns eventPage

        // Act
        val result = eventService.list(query)

        // Assert
        result.totalElements shouldBe 0
    }
})
