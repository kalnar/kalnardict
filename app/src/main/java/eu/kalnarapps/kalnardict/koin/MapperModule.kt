package eu.kalnarapps.kalnardict.koin

import eu.kalnarapps.kalnardict.androidui.common.mapper.DomainToUiMapper
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.mapper.LanguageMapper
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.model.SelectableLanguage
import eu.kalnarapps.kalnardict.data.entities.Language
import eu.kalnarapps.kalnardict.data.entities.Word
import eu.kalnarapps.kalnardict.data.mapper.DomainToLocalDataMapper
import eu.kalnarapps.kalnardict.data.mapper.LanguageDataMapper
import eu.kalnarapps.kalnardict.data.mapper.LanguageLogEntryData
import eu.kalnarapps.kalnardict.data.mapper.LanguageRoomMapper
import eu.kalnarapps.kalnardict.data.mapper.LocalDataToRoomEntityMapper
import eu.kalnarapps.kalnardict.data.mapper.RoomEntityToLocalDataMapper
import eu.kalnarapps.kalnardict.data.mapper.TranslatedWordDataEntry
import eu.kalnarapps.kalnardict.data.mapper.WordDataEntry
import eu.kalnarapps.kalnardict.data.mapper.todata.WordInfoMapper
import eu.kalnarapps.kalnardict.data.mapper.toroom.TranslatedWordMapper
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictLanguage
import org.koin.core.module.Module
import org.koin.core.qualifier.StringQualifier
import org.koin.dsl.module


val mapperModule: Module = module {
    factory { WordInfoMapper() as RoomEntityToLocalDataMapper<Word.WordInfo, WordDataEntry> }
    factory { LanguageMapper() as DomainToUiMapper<DictLanguage, SelectableLanguage.LanguageUi> }
    factory { LanguageDataMapper() as DomainToLocalDataMapper<DictLanguage, LanguageLogEntryData> }

    factory(Qualifier.translatedWordMapper) {
        TranslatedWordMapper() as LocalDataToRoomEntityMapper<TranslatedWordDataEntry, Word>
    }

    factory(Qualifier.languageRoomMapper) {
        LanguageRoomMapper() as LocalDataToRoomEntityMapper<LanguageLogEntryData, Language>
    }
}

object Qualifier {
    val translatedWordMapper = StringQualifier("translated_word")
    val languageRoomMapper = StringQualifier("language_room")
}