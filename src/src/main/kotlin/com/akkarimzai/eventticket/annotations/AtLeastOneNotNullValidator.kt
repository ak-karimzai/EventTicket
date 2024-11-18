package com.akkarimzai.eventticket.annotations

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import kotlin.reflect.full.memberProperties

class AtLeastOneNotNullValidator : ConstraintValidator<AtLeastOneNotNull, Any> {
    override fun isValid(value: Any?, context: ConstraintValidatorContext?): Boolean {
        if (value == null) return false

        val properties = value::class.memberProperties

        return properties.any { property ->
            property.getter.call(value) != null
        }
    }
}