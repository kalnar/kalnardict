package eu.kalnarapps.kalnardict.presentation.mappers

import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictionaryDisplayType
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictionaryWithDisplayTypeInfo
import eu.kalnarapps.kalnardict.presentation.models.dictionarymanager.DictionaryUpdateUi
import eu.kalnarapps.kalnardict.presentation.models.dictionarymanager.ManageableDictionaryView
import eu.kalnarapps.kalnardict.presentation.models.translations.RenderingStrategy

class ManageableDictionaryMapper(
    private val displayTypeMapper: DomainToUiMapper<DictionaryDisplayType, RenderingStrategy>
) : DomainToUiMapper<DictionaryWithDisplayTypeInfo, ManageableDictionaryView> {
    override fun toUiModel(domainModel: DictionaryWithDisplayTypeInfo): ManageableDictionaryView {
        return ManageableDictionaryView(
            dictionaryId = domainModel.dictionary.id,
            sourceLanguage = domainModel.dictionary.languageFrom.name,
            destinationLanguage = domainModel.dictionary.languageTo.name,
            dictionaryName = domainModel.dictionary.description,
            currentRenderingStrategy = displayTypeMapper.toUiModel(
                domainModel.displayTypeInfo.displayType
            ),
            availableRenderingStrategy = domainModel.displayTypeInfo.supportedDisplayTypes.map {
                displayTypeMapper.toUiModel(it)
            },
            updateInfo = DictionaryUpdateUi.None
        )
    }
}
