package eu.kalnarapps.kalnardict.androidui.dictionaryquery.model

data class DictionaryQueryState(
    val typedQueryString: String,
    val queryResults: List<WordView>,
    val currentDictionaryItemView: DictionarySelectorItem,
    val dictionarySelectorItems: List<DictionarySelectorItem>
)

