package com.akkarimzai.eventticket.models.user

data class UserDto(
    val id: Long,
    val name: String,
    val email: String,
    val phoneNumber: String?,
    val username: String,
)