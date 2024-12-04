package com.akkarimzai.eventticket.models.common

import com.akkarimzai.eventticket.exceptions.ValidationException
import org.valiktor.ConstraintViolationException
import org.valiktor.i18n.mapToMessage

abstract class AbstractValidatableCQ {
    fun validate() {
        var result: List<String> = listOf()
        try {
            dataValidator()
        } catch (e: ConstraintViolationException) {
            result = e.constraintViolations
                .mapToMessage(baseName = "messages")
                .map { "${it.property}: ${it.message}" }
        }

        if (result.isNotEmpty()) {
            throw ValidationException(result)
        }
    }

    protected abstract fun dataValidator()
}