package com.akkarimzai.eventticket.services

import com.akkarimzai.eventticket.entities.User
import com.akkarimzai.eventticket.models.user.AuthResponse
import com.akkarimzai.eventticket.models.user.LoginCommand
import com.akkarimzai.eventticket.models.user.RegisterCommand

interface AuthService {
    fun currentUser(): User
    fun register(command: RegisterCommand): AuthResponse
    fun login(command: LoginCommand): AuthResponse
}