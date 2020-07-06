package eu.kalnarapps.kalnardict.koin

import eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.mapper.DomainToUiMapper
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.mapper.LanguageMapper
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.model.SelectableLanguage
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictLanguage
import org.koin.core.module.Module
import org.koin.dsl.module


val mapperModule: Module = module {
    factory { LanguageMapper() as DomainToUiMapper<DictLanguage, SelectableLanguage.LanguageUi> }
}