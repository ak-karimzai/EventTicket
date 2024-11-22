package com.akkarimzai.eventticket.services.impl

import com.akkarimzai.eventticket.entities.User
import com.akkarimzai.eventticket.exceptions.ConflictException
import com.akkarimzai.eventticket.exceptions.NotFoundException
import com.akkarimzai.eventticket.repositories.UserRepository
import com.akkarimzai.eventticket.services.UserService
import mu.KotlinLogging
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(private val repository: UserRepository): UserService {
    private val logger = KotlinLogging.logger {}

    override fun userDetailsService(): UserDetailsService =
        UserDetailsService { username -> loadByUsername(username) }

    override fun loadByUsername(username: String): User {
        logger.info { "Loading user with username: $username" }

        return repository.findByUsername(username)
            ?: throw NotFoundException("user", username).also {
                logger.debug { "User-username: $username not found" }
            }
    }

    override fun create(user: User): User {
        logger.info { "saving user info: $user" }

        isUsernameExists(user.username).let { exist ->
            if (exist) {
                throw ConflictException("User-username", user.username)
            }
        }

        isEmailExists(user.email).let { exist ->
            if (exist) {
                throw ConflictException("User-email", user.email)
            }
        }

        return repository.save(user).also {
            logger.debug { "User saved with id: ${user.id}" }
        }
    }

    override fun update(user: User): User {
        return this.create(user)
    }

    private fun isUsernameExists(username: String): Boolean {
        logger.info { "Checking username existence: $username" }

        return repository.existsByUsername(username).also { exist ->
            val status = "exists"; if (exist) else "not found"

            logger.info { "User-username: $username $status" }
        }
    }

    private fun isEmailExists(email: String): Boolean {
        logger.info { "Checking email existence: $email" }

        return repository.existsByEmail(email).also { exist ->
            val status = "exists"; if (exist) else "not found"

            logger.info { "User-email: $email $status" }
        }
    }
}