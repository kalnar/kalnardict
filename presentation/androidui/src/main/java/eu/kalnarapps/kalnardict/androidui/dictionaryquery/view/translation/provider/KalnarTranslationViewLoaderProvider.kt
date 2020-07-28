package eu.kalnarapps.kalnardict.androidui.dictionaryquery.view.translation.provider

import android.view.ViewStub
import eu.kalnarapps.kalnardict.androidui.R
import eu.kalnarapps.kalnardict.androidui.dictionaryquery.view.translation.TranslationView
import eu.kalnarapps.kalnardict.androidui.dictionaryquery.view.translation.TranslationViewLoader
import eu.kalnarapps.kalnardict.androidui.dictionaryquery.view.translation.TranslationViewLoaderProvider
import eu.kalnarapps.kalnardict.androidui.dictionaryquery.view.translation.views.SimpleTextTranslationView
import eu.kalnarapps.kalnardict.androidui.dictionaryquery.view.translation.views.WebViewHtmlTranslationView
import eu.kalnarapps.kalnardict.presentation.models.translations.RenderingStrategy
import org.koin.core.KoinComponent

class KalnarTranslationViewLoaderProvider : TranslationViewLoaderProvider, KoinComponent {
    override fun provide(renderingStrategy: RenderingStrategy): TranslationViewLoader {
        return when (renderingStrategy.id) {
            "html" -> TranslationWebViewHtmlLoader()
            else -> TranslationTextLoader()
        }
    }
}

class TranslationTextLoader : TranslationViewLoader {
    override fun loadTranslationInto(viewStub: ViewStub): TranslationView {
        viewStub.layoutResource = R.layout.translation_view_simple_text
        val view = viewStub.inflate()
        if (view is SimpleTextTranslationView) {
            return view
        } else {
            throw IllegalViewTypeException("${view.id} is not a SimpleTextTranslationView")
        }
    }

}

class TranslationWebViewHtmlLoader : TranslationViewLoader {
    override fun loadTranslationInto(viewStub: ViewStub): TranslationView {
        viewStub.layoutResource = R.layout.translation_view_web_html
        val view = viewStub.inflate()
        if (view is WebViewHtmlTranslationView) {
            return view
        } else {
            throw IllegalViewTypeException("${view.id} is not a WebViewHtmlTranslationView")
        }
    }

}

class IllegalViewTypeException(msg: String) : Exception(msg)
