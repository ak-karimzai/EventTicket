package com.akkarimzai.eventticket.profiles

import com.akkarimzai.eventticket.entities.User
import com.akkarimzai.eventticket.models.user.RegisterCommand
import com.akkarimzai.eventticket.models.user.UpdateUserCommand
import com.akkarimzai.eventticket.models.user.UserDto
import org.springframework.security.crypto.password.PasswordEncoder

fun User.toUserDto(): UserDto {
    return UserDto(
        id = this.id!!,
        name = this.name,
        email = this.email,
        phoneNumber = this.phoneNumber,
        username = this.username,
    )
}

fun RegisterCommand.toUser(passwordEncoder: PasswordEncoder): User {
    return User(
        name = this.name,
        email = this.email,
        phoneNumber = this.phoneNumber,
        username = this.username,
        password = passwordEncoder.encode(this.password)
    )
}

fun UpdateUserCommand.toUser(user: User, passwordEncoder: PasswordEncoder): User {
    return user.also {
        this.name?.let { user.name = it }
        this.email?.let { user.email = it }
        this.phoneNumber?.let { user.phoneNumber = it }
        this.username?.let { user.username = it }
        this.password?.let { user.password = passwordEncoder.encode(it) }
    }
}