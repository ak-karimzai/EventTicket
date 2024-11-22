package com.akkarimzai.eventticket

import com.akkarimzai.eventticket.models.category.CreateCategoryCommand
import com.akkarimzai.eventticket.services.impl.CategoryService
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.stereotype.Component

@SpringBootApplication
class EventTicketApplication

fun main(args: Array<String>) {
    runApplication<EventTicketApplication>(*args)
}

@Component
class StartupRunner(private val categoryService: CategoryService) : CommandLineRunner {

    override fun run(vararg args: String?) {
        val createCategoryCommand = CreateCategoryCommand(
            title = "Sample Category",
            listOf()
        )

        val createdCategoryId = categoryService.create(createCategoryCommand)
        println("Category created during startup with ID: $createdCategoryId")
    }
}