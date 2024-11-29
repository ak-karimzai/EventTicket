package com.akkarimzai.eventticket.controllers.middlewares

class ErrorResponse<T>(
    val status: Int, val message: T
)