package com.akkarimzai.eventticket.controllers

import org.junit.jupiter.api.BeforeAll
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
abstract class AbstractIntegrationTest {
    companion object {
        val psqlContainer = PostgreSQLContainer("postgres:14-alpine3.20").apply {
            withDatabaseName("task10")
            withUsername("root")
            withPassword("root")
        }

        @BeforeAll
        @JvmStatic
        fun startContainer(): Unit {
            psqlContainer.start()
        }

        @DynamicPropertySource
        @JvmStatic
        fun configureProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", psqlContainer::getJdbcUrl)
            registry.add("spring.datasource.username", psqlContainer::getUsername)
            registry.add("spring.datasource.password", psqlContainer::getPassword)
        }
    }
}