package com.akkarimzai.eventticket.services.impl

import com.akkarimzai.eventticket.entities.User
import com.akkarimzai.eventticket.exceptions.UnauthorizedException
import com.akkarimzai.eventticket.models.user.AuthResponse
import com.akkarimzai.eventticket.models.user.LoginCommand
import com.akkarimzai.eventticket.models.user.RegisterCommand
import com.akkarimzai.eventticket.profiles.toUser
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
        val username = authentication?.name ?: throw UnauthorizedException("User not authenticated")

        logger.info { "Current user: $username" }

        return userService.loadByUsername(username)
    }

    fun register(command: RegisterCommand): AuthResponse {
        logger.info { "Request register for user: ${command.username}" }

        val user = command.toUser(passwordEncoder)
        val createdUser = userService.create(user)
        logger.info { "new user created with id: ${createdUser.id}" }

        return generateToken(user)
    }

    fun login(command: LoginCommand): AuthResponse {
        logger.info { "Request login for user: ${command.username}" }

        val user = userService.loadByUsername(command.username)
        passwordEncoder.matches(command.password, user.password).let { matches ->
            if (!matches) {
                throw UnauthorizedException("invalid credentials!")
            }
        }
        return generateToken(user)
    }

    private fun generateToken(user: User): AuthResponse =
        AuthResponse(
            token = jwtService.generateToken(user)
        )
}