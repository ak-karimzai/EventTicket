package com.akkarimzai.eventticket.services

import com.akkarimzai.eventticket.entities.User
import com.akkarimzai.eventticket.models.user.AuthResponse
import com.akkarimzai.eventticket.models.user.LoginCommand
import com.akkarimzai.eventticket.models.user.RegisterCommand
import com.akkarimzai.eventticket.models.user.UpdateUserCommand
import com.akkarimzai.eventticket.models.user.UserDto

interface AuthService {
    fun currentUser(): User
    fun load(): UserDto
    fun register(command: RegisterCommand): AuthResponse
    fun login(command: LoginCommand): AuthResponse
    fun update(command: UpdateUserCommand)
}