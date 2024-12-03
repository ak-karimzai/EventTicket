package com.akkarimzai.eventticket.annotations

import mu.KotlinLogging
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.springframework.stereotype.Component
import kotlin.time.measureTime


@Aspect
@Component
class LogExecutionTimeAspect {
    private val logger = KotlinLogging.logger {}

    @Pointcut("@annotation(com.akkarimzai.eventticket.annotations.LogExecutionTime)")
    fun logExecutionTimeMethod() {}

    @Pointcut("@within(com.akkarimzai.eventticket.annotations.LogExecutionTime)")
    fun logExecutionTimeClass() {}

    @Around("logExecutionTimeMethod() || logExecutionTimeClass()")
    @Throws(Throwable::class)
    fun logExecutionTime(joinPoint: ProceedingJoinPoint) : Any? {
        val result: Any?
        val duration = measureTime {
            result = joinPoint.proceed()
        }
        logger.info {
            "Executed method: {${joinPoint.signature.name}} in class: {${joinPoint.target.javaClass.simpleName}} in ${duration}ms."
        }
        return result
    }
}