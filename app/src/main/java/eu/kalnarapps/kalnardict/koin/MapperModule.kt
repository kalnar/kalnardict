package eu.kalnarapps.kalnardict.koin

import eu.kalnarapps.kalnardict.androidui.common.mapper.UiToDomainMapper
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.mapper.LanguageMapper
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.model.SelectableLanguage
import eu.kalnarapps.kalnardict.data.entities.Language
import eu.kalnarapps.kalnardict.data.entities.Word
import eu.kalnarapps.kalnardict.data.mapper.DataToDomainMapper
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
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictLanguage
import eu.kalnarapps.kalnardict.domain.entities.dictionary.QueryMode
import eu.kalnarapps.kalnardict.presentation.mappers.DomainToUiMapper
import eu.kalnarapps.kalnardict.presentation.mappers.QueryModeMapper
import eu.kalnarapps.kalnardict.presentation.models.dictionaryquery.QueryModelUiModel
import org.koin.core.module.Module
import org.koin.dsl.module


val mapperModule: Module = module {
    factory { WordInfoMapper() as RoomEntityToLocalDataMapper<Word.WordInfo, WordDataEntry> }
    single { LanguageMapper() }
    single(Qualifiers.languageDomainUiMapper) {
        get<LanguageMapper>() as eu.kalnarapps.kalnardict.androidui.common.mapper.DomainToUiMapper<DictLanguage, SelectableLanguage.LanguageUi>
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
}

