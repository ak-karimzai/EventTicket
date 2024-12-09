package com.akkarimzai.eventticket.exceptions

class ConflictException(key: String, value: Any) : Exception("$key: $value already exist") {

}
