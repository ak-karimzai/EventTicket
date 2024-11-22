package com.akkarimzai.eventticket.services.impl

import com.akkarimzai.eventticket.entities.User
import com.akkarimzai.eventticket.exceptions.BadRequestException
import com.akkarimzai.eventticket.services.UserService
import mu.KotlinLogging
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val userService: UserService,
    private val jwtService: JwtService,
    private val passwordEncoder: PasswordEncoder,
) {
    private val logger = KotlinLogging.logger {}

    fun currentUser(): User {
        val authentication = SecurityContextHolder.getContext().authentication
        val username = authentication?.name ?: throw BadRequestException("User not authenticated")

        logger.info { "Current user: $username" }

        return userService.loadByUsername(username)
    }

    fun registerUser(user: User) {
    }
}