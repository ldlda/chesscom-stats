package com.ldlda.chesscom_stats.utils.serialize.tostring

import kotlinx.serialization.SerializationException
import java.time.Instant
import java.time.format.DateTimeParseException

object InstantParseSerializer : ToStringSerializer<Instant>("Instant") {
    override fun fromString(string: String): Instant {
        return try {
            Instant.parse(string)
        } catch (e: DateTimeParseException) {
            throw SerializationException("Invalid datetime string: '$string'", e)
        }
    }
}