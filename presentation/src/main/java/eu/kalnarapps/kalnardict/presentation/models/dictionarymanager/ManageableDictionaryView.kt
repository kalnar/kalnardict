package eu.kalnarapps.kalnardict.presentation.models.dictionarymanager

import eu.kalnarapps.kalnardict.presentation.models.translations.RenderingStrategy

data class ManageableDictionaryView(
    val dictionaryName: String,
    val sourceLanguage: String,
    val destinationLanguage: String,
    val currentRenderingStrategy: RenderingStrategy,
    val availableRenderingStrategy: List<RenderingStrategy>,
    val updateInfo: DictionaryUpdateUi = DictionaryUpdateUi.None,
    val dictionaryId: Int
) {
    var onDeleteAction: ((Int) -> Unit)? = null
}


sealed class DictionaryUpdateUi {
    object None : DictionaryUpdateUi()
    data class Info(
        val dictionaryId: Int,
        val renderingStrategy: RenderingStrategy
    ) : DictionaryUpdateUi()
}
