package com.ldlda.chesscom_stats.utils.serialize.tostring

import kotlinx.serialization.SerializationException
import java.net.URI
import java.net.URISyntaxException

/// uri is a safer url. it also doesn't do anything to verify the url (namely doing some dns resolves)
object URISerializer : ToStringSerializer<URI>("URI") {
    override fun fromString(string: String): URI {
        return try {
            URI(string)
        } catch (e: URISyntaxException) {
            throw SerializationException("Invalid URI string: '$string'", e)
        }
    }
}