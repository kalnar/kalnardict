package eu.kalnarapps.kalnardict.presentation.models.strings

object StringResources {
    interface StringResource
    enum class DictionaryQueryString :
        StringResource {
        QUERY_MODE_MATCH_ANYWHERE,
        QUERY_MODE_MATCH_BEGINNING,
        QUERY_MODE_MATCH_END,
        QUERY_MODE_MATCH_EXACT,
        QUERY_MODE_MATCH_FUZZY;
    }
}