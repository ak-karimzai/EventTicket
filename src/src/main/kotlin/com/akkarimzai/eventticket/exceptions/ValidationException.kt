package com.akkarimzai.eventticket.exceptions

class ValidationException(val validationErrors: List<String>) : Exception()
