package eu.kalnarapps.kalnardict.presentation.interactors.dictionary

import eu.kalnarapps.kalnardict.data.DisplayTypeRepository
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictionaryDisplayType
import eu.kalnarapps.kalnardict.presentation.mappers.UiToDomainMapper
import eu.kalnarapps.kalnardict.presentation.models.dictionarymanager.DictionaryUpdateUi
import eu.kalnarapps.kalnardict.presentation.models.translations.RenderingStrategy

class UpdateDictionaryFromUi(
    private val displayTypeRepository: DisplayTypeRepository,
    private val renderingStrategyMapper: UiToDomainMapper<RenderingStrategy, DictionaryDisplayType>
) : UpdateDictionaryUseCaseFromUi {
    override suspend operator fun invoke(
        dictionaryUpdateUi: DictionaryUpdateUi.Info
    ) {
        displayTypeRepository.setDisplayTypeFor(
            dictionaryId = dictionaryUpdateUi.dictionaryId,
            displayType = renderingStrategyMapper.toDomainModel(
                dictionaryUpdateUi.renderingStrategy
            )
        )
    }
}