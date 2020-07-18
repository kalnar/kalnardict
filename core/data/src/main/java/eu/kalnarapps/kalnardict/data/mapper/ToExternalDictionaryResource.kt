package eu.kalnarapps.kalnardict.data.mapper

import eu.kalnarapps.kalnardict.data.ExternalDictionaryResource
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ExternalDatabase

fun ExternalDatabase.toExternalDictionaryResource(): ExternalDictionaryResource {
    return object : ExternalDictionaryResource {
        override fun sdCardPath(): String = uri
    }
}