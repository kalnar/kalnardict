package eu.kalnarapps.kalnardict.koin

import eu.kalnarapps.kalnardict.androidui.dictionaryquery.view.translation.TranslationViewLoaderProvider
import eu.kalnarapps.kalnardict.androidui.dictionaryquery.view.translation.provider.KalnarTranslationViewLoaderProvider
import org.koin.dsl.module


val viewModule = module {

    single { KalnarTranslationViewLoaderProvider() as TranslationViewLoaderProvider }

}