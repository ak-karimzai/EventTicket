package com.akkarimzai.eventticket.services

import com.akkarimzai.eventticket.entities.User

interface UserService {
    fun currentUser(): User
    fun loadByUsername(username: String): User
    fun create(user: User): User
    fun update(user: User): User
}
