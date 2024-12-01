package com.akkarimzai.eventticket.controllers.impl

import com.akkarimzai.eventticket.annotations.LogExecutionTime
import com.akkarimzai.eventticket.controllers.api.AuthControllerApi
import com.akkarimzai.eventticket.models.user.AuthResponse
import com.akkarimzai.eventticket.models.user.LoginCommand
import com.akkarimzai.eventticket.models.user.RegisterCommand
import com.akkarimzai.eventticket.services.AuthService
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value = ["/api/v1/auth"])
@LogExecutionTime
class AuthController(private val authController: AuthService): AuthControllerApi {
    override fun register(
        command: RegisterCommand
    ): AuthResponse = authController.register(command)

    override fun login(
        command: LoginCommand
    ): AuthResponse = authController.login(command)
}