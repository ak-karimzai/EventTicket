package com.akkarimzai.eventticket.services

import com.akkarimzai.eventticket.entities.User
import org.springframework.security.core.userdetails.UserDetailsService

interface UserService {
    fun loadByUsername(username: String): User
    fun userDetailsService(): UserDetailsService
    fun create(user: User): User
    fun update(user: User): User
}
