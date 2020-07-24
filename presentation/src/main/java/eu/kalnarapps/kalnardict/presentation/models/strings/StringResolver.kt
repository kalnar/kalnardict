package eu.kalnarapps.kalnardict.presentation.models.strings

interface StringResolver<T: StringResources.StringResource> {
    fun lookUpString(resource: T): String
}