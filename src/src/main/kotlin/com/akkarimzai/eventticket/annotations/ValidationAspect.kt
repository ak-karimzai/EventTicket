package com.akkarimzai.eventticket.annotations

import com.akkarimzai.eventticket.exceptions.ValidationException
import com.akkarimzai.eventticket.models.common.AbstractValidatableCQ
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.aspectj.lang.annotation.Pointcut
import org.springframework.stereotype.Component

@Aspect
@Component
class ValidationAspect {
    @Pointcut("within(@Validate *)")
    fun classLevelValidation() {}

    @Pointcut("@annotation(Validate)")
    fun methodLevelValidation() {}

    @Before("classLevelValidation() || methodLevelValidation()")
    fun validateParameters(joinPoint: JoinPoint) {
        val args = joinPoint.args

        args.filterIsInstance<AbstractValidatableCQ>().forEach { param ->
            val validationErrors = param.validate()
            if (validationErrors.isNotEmpty()) {
                throw ValidationException(validationErrors)
            }
        }
    }
}