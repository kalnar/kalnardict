package eu.kalnarapps.kalnardict.presentation.interactors.query

import eu.kalnarapps.kalnardict.presentation.models.dictionaryquery.QueryUiModel
import eu.kalnarapps.kalnardict.presentation.models.dictionaryquery.WordView

interface SearchQueryUseCaseFromUi {
    suspend operator fun invoke(queryUiModel: QueryUiModel): List<WordView>
}