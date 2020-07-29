package eu.kalnarapps.kalnardict.koin

import eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.mapper.LanguageMapper
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.model.SelectableLanguage
import eu.kalnarapps.kalnardict.data.entities.Language
import eu.kalnarapps.kalnardict.data.entities.Word
import eu.kalnarapps.kalnardict.data.mapper.DataToDomainMapper
import eu.kalnarapps.kalnardict.data.mapper.DictionaryDisplayTypeDomainMapper
import eu.kalnarapps.kalnardict.data.mapper.DictionaryDisplayTypeMapper
import eu.kalnarapps.kalnardict.data.mapper.DomainToDataMapper
import eu.kalnarapps.kalnardict.data.mapper.LanguageDataMapper
import eu.kalnarapps.kalnardict.data.mapper.LanguageLogEntryData
import eu.kalnarapps.kalnardict.data.mapper.LanguageRoomMapper
import eu.kalnarapps.kalnardict.data.mapper.LocalDataToRoomEntityMapper
import eu.kalnarapps.kalnardict.data.mapper.RoomEntityToLocalDataMapper
import eu.kalnarapps.kalnardict.data.mapper.TranslatedWordInsertEntry
import eu.kalnarapps.kalnardict.data.mapper.WordDataEntry
import eu.kalnarapps.kalnardict.data.mapper.todata.WordInfoMapper
import eu.kalnarapps.kalnardict.data.mapper.toroom.TranslatedWordMapper
import eu.kalnarapps.kalnardict.data.model.contracts.DictionaryDisplayTypeDataEntry
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictLanguage
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictQuery
import eu.kalnarapps.kalnardict.domain.entities.dictionary.Dictionary
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictionaryDisplayType
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictionaryWithDisplayType
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictionaryWithDisplayTypeInfo
import eu.kalnarapps.kalnardict.domain.entities.dictionary.QueryMode
import eu.kalnarapps.kalnardict.domain.entities.words.DictWord
import eu.kalnarapps.kalnardict.presentation.mappers.DictionaryDisplayTypeDomainToUiMapper
import eu.kalnarapps.kalnardict.presentation.mappers.DictionaryMapper
import eu.kalnarapps.kalnardict.presentation.mappers.DomainToUiMapper
import eu.kalnarapps.kalnardict.presentation.mappers.ManageableDictionaryMapper
import eu.kalnarapps.kalnardict.presentation.mappers.QueryModeMapper
import eu.kalnarapps.kalnardict.presentation.mappers.RenderingStrategyMapper
import eu.kalnarapps.kalnardict.presentation.mappers.UiToDomainMapper
import eu.kalnarapps.kalnardict.presentation.mappers.UiToDomainMapperWithExtras
import eu.kalnarapps.kalnardict.presentation.mappers.query.QueryModeUiToDomainMapper
import eu.kalnarapps.kalnardict.presentation.mappers.query.QueryUiToDomainMapper
import eu.kalnarapps.kalnardict.presentation.mappers.query.WordDomainToUiMapper
import eu.kalnarapps.kalnardict.presentation.models.dictionarymanager.ManageableDictionaryView
import eu.kalnarapps.kalnardict.presentation.models.dictionaryquery.DictionaryUiModel
import eu.kalnarapps.kalnardict.presentation.models.dictionaryquery.QueryModelUiModel
import eu.kalnarapps.kalnardict.presentation.models.dictionaryquery.QueryUiModel
import eu.kalnarapps.kalnardict.presentation.models.dictionaryquery.WordView
import eu.kalnarapps.kalnardict.presentation.models.translations.RenderingStrategy
import org.koin.core.module.Module
import org.koin.dsl.module


val mapperModule: Module = module {
    factory { WordInfoMapper() as RoomEntityToLocalDataMapper<Word.WordInfo, WordDataEntry> }
    single { LanguageMapper() }
    single(Qualifiers.languageDomainUiMapper) {
        get<LanguageMapper>()
                as DomainToUiMapper<DictLanguage, SelectableLanguage.LanguageUi>
    }
    single(Qualifiers.languageUiDomainMapper) {
        get<LanguageMapper>() as UiToDomainMapper<SelectableLanguage.LanguageUi, DictLanguage>
    }
    single(Qualifiers.queryModeDomainUiMapper) {
        QueryModeMapper(
            stringResolver = get(Qualifiers.StringResolvers.dictionaryQueryString)
        ) as DomainToUiMapper<QueryMode, QueryModelUiModel>
    }

    single { LanguageDataMapper() }
    single(Qualifiers.languageDomainDataMapper) {
        get<LanguageDataMapper>() as DomainToDataMapper<DictLanguage, LanguageLogEntryData>
    }
    single(Qualifiers.languageDataDomainMapper) {
        get<LanguageDataMapper>() as DataToDomainMapper<LanguageLogEntryData, DictLanguage>
    }


    factory(Qualifiers.translatedWordMapper) {
        TranslatedWordMapper() as LocalDataToRoomEntityMapper<TranslatedWordInsertEntry, Word>
    }

    factory(Qualifiers.languageRoomMapper) {
        LanguageRoomMapper() as LocalDataToRoomEntityMapper<LanguageLogEntryData, Language>
    }

    single(Qualifiers.dictionaryDisplayDataDomainMapper) {
        DictionaryDisplayTypeMapper()
                as DataToDomainMapper<DictionaryDisplayTypeDataEntry, DictionaryDisplayType>
    }

    single(Qualifiers.dictionaryDisplayDomainUiMapper) {
        DictionaryDisplayTypeDomainToUiMapper(
            stringResolver = get(Qualifiers.StringResolvers.dictionaryRenderingStrategyStrings)
        ) as DomainToUiMapper<DictionaryDisplayType, RenderingStrategy>
    }

    single(Qualifiers.DomainToData.dictionaryDisplayDomainDataMapper) {
        DictionaryDisplayTypeDomainMapper() as
                DomainToDataMapper<DictionaryDisplayType, DictionaryDisplayTypeDataEntry>
    }

    single(Qualifiers.dictionaryWithDisplayTypeInfoDomainUiMapper) {
        DictionaryMapper(
            displayTypeMapper = get(Qualifiers.dictionaryDisplayDomainUiMapper)
        ) as DomainToUiMapper<DictionaryWithDisplayType, DictionaryUiModel>
    }

    single(Qualifiers.manageableDictionaryDomainUiMapper) {
        ManageableDictionaryMapper(
            displayTypeMapper = get(Qualifiers.dictionaryDisplayDomainUiMapper)
        ) as DomainToUiMapper<DictionaryWithDisplayTypeInfo, ManageableDictionaryView>
    }

    single(Qualifiers.renderingStrategyUiDomainMapper) {
        RenderingStrategyMapper() as
                UiToDomainMapper<RenderingStrategy, DictionaryDisplayType>
    }

    single(Qualifiers.UiToDomain.queryModeUiToDomainMapper) {
        QueryModeUiToDomainMapper() as UiToDomainMapper<QueryModelUiModel, QueryMode>
    }

    single(Qualifiers.DomainToUi.wordDomainToUiMapper) {
        WordDomainToUiMapper() as DomainToUiMapper<DictWord, WordView>
    }

    single(Qualifiers.UiToDomain.queryUiToDomainMapper) {
        QueryUiToDomainMapper(
            queryModeUiToDomainMapper = get(Qualifiers.UiToDomain.queryModeUiToDomainMapper)
        ) as UiToDomainMapperWithExtras<QueryUiModel, DictQuery, Dictionary>
    }
}

