package com.ldlda.chesscom_stats.util.serialize.tostring

import kotlinx.serialization.KSerializer
import java.time.Instant

/**
 * Backwards-compatible alias for the ISO-8601 string-based Instant serializer.
 * Keeps existing @Serializable(with = InstantSerializer::class) annotations working.
 */
object InstantSerializer : KSerializer<Instant> by InstantParseSerializer
