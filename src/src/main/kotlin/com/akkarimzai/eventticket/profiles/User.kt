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
    return User(
        id = user.id,
        name = this.name ?: user.name,
        email = this.email ?: user.email,
        phoneNumber = this.phoneNumber ?: user.phoneNumber,
        username = this.username ?: user.username,
        password = if (this.password != null) passwordEncoder.encode(this.password) else user.password
    )
}