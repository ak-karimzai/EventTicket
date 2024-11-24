//package com.akkarimzai.eventticket.models.category
//
//import io.kotest.core.spec.style.FunSpec
//import io.kotest.matchers.shouldBe
//import jakarta.validation.Validation
//import jakarta.validation.Validator
//import jakarta.validation.ValidatorFactory
//import kotlin.test.assertTrue
//
//class UpdateCategoryCommandTest : FunSpec({
//    lateinit var validator: Validator
//
//    beforeTest {
//        val factory: ValidatorFactory = Validation.buildDefaultValidatorFactory()
//        validator = factory.validator
//    }
//
//    test("should pass validation when title is valid") {
//        // Arrange
//        val command = UpdateCategoryCommand(title = "Valid Title")
//
//        // Act
//        val violations = validator.validate(command)
//
//        // Assert
//        violations.size shouldBe 0
//    }
//
//    test("should fail validation when title is blank") {
//        // Arrange
//        val command = UpdateCategoryCommand(title = " ")
//
//        // Act
//        val violations = validator.validate(command)
//
//        // Assert
//        violations.size shouldBe 2
//        assertTrue {
//            violations.any {
//                it.propertyPath.toString() == "title" && it.message.contains("must not be blank")
//            }
//        }
//    }
//
//    test("should fail validation when title is null") {
//        // Arrange
//        val command = UpdateCategoryCommand(title = null)
//
//        // Act
//        val violations = validator.validate(command)
//
//        // Assert
//        violations.size shouldBe 2
//        assertTrue {
//            violations.any {
//                it.propertyPath.toString() == "title" && it.message.contains("must not be blank")
//            }
//        }
//    }
//
//    test("should fail validation when title is too short") {
//        // Arrange
//        val command = UpdateCategoryCommand(title = "AB")
//
//        // Act
//        val violations = validator.validate(command)
//
//        // Assert
//        violations.size shouldBe 1
//        assertTrue {
//            violations.any {
//                it.propertyPath.toString() == "title" && it.message.contains("size must be between 3 and 256")
//            }
//        }
//    }
//
//    test("should fail validation when title is too long") {
//        // Arrange
//        val longTitle = "A".repeat(257)
//        val command = UpdateCategoryCommand(title = longTitle)
//
//        // Act
//        val violations = validator.validate(command)
//
//        // Assert
//        violations.size shouldBe 1
//        assertTrue {
//            violations.any {
//                it.propertyPath.toString() == "title" && it.message.contains("size must be between 3 and 256")
//            }
//        }
//    }
//})
