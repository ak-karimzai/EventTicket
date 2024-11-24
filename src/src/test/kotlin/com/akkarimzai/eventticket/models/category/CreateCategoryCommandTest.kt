package com.akkarimzai.eventticket.models.category

import com.akkarimzai.eventticket.models.event.CreateEventCommand
import com.akkarimzai.eventticket.models.event.EventTicketDto
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDateTime
import kotlin.test.assertTrue
/*

class CreateCategoryCommandTest : FunSpec({

    lateinit var validator: Validator

    beforeTest {
        val factory: ValidatorFactory = Validation.buildDefaultValidatorFactory()
        validator = factory.validator
    }

    test("should pass validation with valid data") {
        // Arrange
        val command = CreateCategoryCommand(
            title = "Concert Night",
            events = listOf()
        )

        // Act
        val violations: Set<ConstraintViolation<CreateCategoryCommand>> = validator.validate(command)

        // Assert
        violations.isEmpty() shouldBe true
    }

    test("should fail validation when title is blank") {
        // Arrange
        val command = CreateCategoryCommand(
            title = "   ",
            events = listOf()
        )

        // Act
        val violations: Set<ConstraintViolation<CreateCategoryCommand>> = validator.validate(command)

        // Assert
        violations.size shouldBe 1
        assertTrue {
            violations.any {
                it.propertyPath.toString() == "title" && it.message.contains("must not be blank")
            }
        }
    }

    test("should fail while title length is less than three") {
        // Arrange
        val command = CreateCategoryCommand(
            title = "Va",
            events = listOf()
        )

        // Act
        val violations: Set<ConstraintViolation<CreateCategoryCommand>> = validator.validate(command)

        // Assert
        violations.isEmpty() shouldBe true
    }
})
*/
