package eu.kalnarapps.kalnardict.presentation.models.dictionaryquery

data class QueryUiModel(
    val queryString: String,
    val dictionaryUiModel: DictionaryUiModel,
    val queryMode: QueryModelUiModel
)
