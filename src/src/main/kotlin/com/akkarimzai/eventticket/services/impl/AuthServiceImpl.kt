package com.akkarimzai.eventticket.services.impl

import com.akkarimzai.eventticket.annotations.Validate
import com.akkarimzai.eventticket.entities.User
import com.akkarimzai.eventticket.exceptions.ConflictException
import com.akkarimzai.eventticket.exceptions.UnauthorizedException
import com.akkarimzai.eventticket.models.user.AuthResponse
import com.akkarimzai.eventticket.models.user.LoginCommand
import com.akkarimzai.eventticket.models.user.RegisterCommand
import com.akkarimzai.eventticket.models.user.UpdateUserCommand
import com.akkarimzai.eventticket.models.user.UserDto
import com.akkarimzai.eventticket.profiles.toUser
import com.akkarimzai.eventticket.profiles.toUserDto
import com.akkarimzai.eventticket.services.AuthService
import com.akkarimzai.eventticket.services.UserService
import mu.KotlinLogging
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
@Validate
class AuthServiceImpl(
    private val userService: UserService,
    private val jwtService: JwtService,
    private val passwordEncoder: PasswordEncoder,
): AuthService {
    private val logger = KotlinLogging.logger {}

    override fun currentUser(): User {
        val authentication = SecurityContextHolder.getContext().authentication
        val username = authentication?.name

        if (username == null) {
            logger.debug { "User context not exist" }
            throw UnauthorizedException("User not authorized!")
        }

        logger.info { "Current user: $username" }

        return userService.loadByUsername(username)
    }

    override fun load(): UserDto {
        return currentUser().toUserDto()
    }

    override fun register(command: RegisterCommand): AuthResponse {
        logger.info { "Request register for user: ${command.username}" }

        val user = command.toUser(passwordEncoder)
        val createdUser = userService.create(user)
        logger.info { "new user created with id: ${createdUser.id}" }

        return generateToken(user)
    }

    override fun login(command: LoginCommand): AuthResponse {
        logger.info { "Request login for user: ${command.username}" }

        val user = userService.loadByUsername(command.username)
        passwordEncoder.matches(command.password, user.password).let { matches ->
            if (!matches) {
                throw UnauthorizedException("invalid credentials!")
            }
        }
        return generateToken(user)
    }

    override fun update(command: UpdateUserCommand) {
        logger.info { "Request update user: $command" }

        command.username?.let { username -> userService.isUsernameExists(username) }.let {
                exist -> if (exist == true) throw ConflictException("User-username", command.username!!) }

        command.email?.let { email -> userService.isEmailExists(email) }.let {
                exist -> if (exist == true) throw ConflictException("User-email", command.email!!) }

        val user = currentUser()
        val userToUpdate = command.toUser(user, passwordEncoder)

        userService.update(userToUpdate)
    }

    private fun generateToken(user: User): AuthResponse =
        AuthResponse(
            token = jwtService.generateToken(user)
        )
}