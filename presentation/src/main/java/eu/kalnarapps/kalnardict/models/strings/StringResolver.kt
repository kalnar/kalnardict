package eu.kalnarapps.kalnardict.models.strings

interface StringResolver<T: StringResources.StringResource> {
    fun lookUpString(resource: T): String
}