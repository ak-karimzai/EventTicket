package com.akkarimzai.eventticket.models.event

import java.time.LocalDateTime

data class EventDto(val id: Long,
    val title: String,
    val artist: String?,
    val date: LocalDateTime)