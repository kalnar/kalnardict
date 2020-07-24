package eu.kalnarapps.kalnardict.androidui.dictionaryquery.view.translation

import android.view.ViewStub
import eu.kalnarapps.kalnardict.presentation.models.translations.RenderingStrategy

interface TranslationViewLoaderProvider {
    fun provide(renderingStrategy: RenderingStrategy): TranslationViewLoader
}

interface TranslationViewLoader {

    fun loadTranslationInto(viewStub: ViewStub): TranslationView
}
