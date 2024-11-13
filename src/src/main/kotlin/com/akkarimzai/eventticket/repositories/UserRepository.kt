package com.akkarimzai.eventticket.repositories

import com.akkarimzai.eventticket.entities.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {
    fun findByUsername(username: String): User?
}