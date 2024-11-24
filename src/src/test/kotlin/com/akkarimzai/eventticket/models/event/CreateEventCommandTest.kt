//package com.akkarimzai.eventticket.models.event
//
//import io.kotest.core.spec.style.FunSpec
//import jakarta.validation.Validation
//import jakarta.validation.Validator
//import jakarta.validation.ValidatorFactory
//import java.time.LocalDateTime
//import kotlin.test.assertTrue
//
//class CreateEventCommandTest : FunSpec({
//    val factory: ValidatorFactory = Validation.buildDefaultValidatorFactory()
//    val validator: Validator = factory.validator
//
//    test("CreateEventCommand validation should pass for valid inputs") {
//        // Arrange
//        val validCommand = CreateEventCommand(
//            title = "Concert",
//            artist = "John Doe",
//            date = LocalDateTime.now(),
//            tickets = listOf(
//                EventTicketDto(
//                    title = "VIP",
//                    description = "VIP ticket",
//                    price = 100.0
//                )
//            )
//        )
//
//        // Act
//        val violations = validator.validate(validCommand)
//
//        // Assert
//        assertTrue { violations.isEmpty() }
//    }
//
//    test("CreateEventCommand should fail if title is too short") {
//        // Arrange
//        val invalidCommand = CreateEventCommand(
//            title = "A",
//            artist = "John Doe",
//            date = LocalDateTime.now(),
//            tickets = listOf(
//                EventTicketDto(
//                    title = "VIP",
//                    description = "VIP ticket",
//                    price = 100.0
//                )
//            )
//        )
//
//        // Act
//        val violations = validator.validate(invalidCommand)
//
//        // Assert
//        assertTrue {
//            violations.any {
//                it.propertyPath.toString() == "title" && it.message.contains("size must be between 3 and 256")
//            }
//        }
//    }
//
//    test("CreateEventCommand should not fail if artist is not null and length is less than 3") {
//        // Arrange
//        val invalidCommand = CreateEventCommand(
//            title = "Any",
//            artist = "Jo",
//            date = LocalDateTime.now(),
//            tickets = listOf(
//                EventTicketDto(
//                    title = "VIP",
//                    description = "VIP ticket",
//                    price = 100.0
//                )
//            )
//        )
//
//        // Act
//        val violations = validator.validate(invalidCommand)
//
//        // Assert
//        assertTrue {
//            violations.any {
//                it.propertyPath.toString() == "title" && it.message.contains("size must be between 3 and 256")
//            }
//        }
//    }
//
//    test("CreateEventCommand should not fail if artist is null") {
//        // Arrange
//        val invalidCommand = CreateEventCommand(
//            title = "Concert",
//            artist = null,
//            date = LocalDateTime.now(),
//            tickets = listOf(
//                EventTicketDto(
//                    title = "VIP",
//                    description = "VIP ticket",
//                    price = 100.0
//                )
//            )
//        )
//
//        // Act
//        val violations = validator.validate(invalidCommand)
//
//        // Assert
//        assertTrue { violations.isEmpty() }
//    }
//
//    test("CreateEventCommand should fail if price is negative") {
//        // Arrange
//        val invalidTicket = EventTicketDto(
//            title = "VIP",
//            description = "VIP ticket",
//            price = -50.0
//        )
//
//        // Act
//        val violations = validator.validate(invalidTicket)
//
//        // Assert
//        assertTrue {
//            violations.any {
//                it.propertyPath.toString() == "price" && it.message.contains("must be greater than or equal to 0.0")
//            }
//        }
//    }
//
//    test("CreateEventCommand should pass with valid price") {
//        // Arrange
//        val validTicket = EventTicketDto(
//            title = "VIP",
//            description = "VIP ticket",
//            price = 50.0
//        )
//
//        // Act
//        val violations = validator.validate(validTicket)
//
//        // Assert
//        assertTrue { violations.isEmpty() }
//    }
//
//})
