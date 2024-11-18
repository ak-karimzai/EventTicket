package com.akkarimzai.eventticket.entities

import com.akkarimzai.eventticket.entities.common.Entity
import jakarta.persistence.Column
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table
import java.time.LocalDateTime

@jakarta.persistence.Entity
@Table(name = "users")
class User(
    id: Long? = null,

    @Column(nullable = false)
    var name: String,

    @Column(nullable = false)
    var email: String,

    @Column(name = "phone_number")
    var phoneNumber: String? = null,

    @Column(nullable = false)
    var username: String,

    @Column(nullable = false)
    var password: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var role: Role = Role.USER,

    @Column(name = "joined_at", nullable = false)
    var joinedAt: LocalDateTime = LocalDateTime.now()
): Entity(id)
