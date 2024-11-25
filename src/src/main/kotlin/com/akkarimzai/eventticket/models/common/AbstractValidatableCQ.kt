package com.akkarimzai.eventticket.models.common

import org.valiktor.ConstraintViolationException
import org.valiktor.i18n.mapToMessage

abstract class AbstractValidatableCQ {
    fun validate(): List<String> {
        var result: List<String> = listOf()
        try {
            dataValidator()
        } catch (e: ConstraintViolationException) {
            result = e.constraintViolations
                .mapToMessage(baseName = "messages")
                .map { "${it.property}: ${it.message}" }
        }
        return result
    }

    protected abstract fun dataValidator()
}