package eu.kalnarapps.kalnardict.androidui.dictionaryquery.view.translation.views

import android.content.Context
import android.os.Build
import android.text.Html
import android.util.AttributeSet
import android.widget.ScrollView
import androidx.appcompat.widget.AppCompatTextView
import eu.kalnarapps.kalnardict.androidui.dictionaryquery.view.translation.TranslationView

class SimpleTextTranslationView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ScrollView(context, attrs, defStyleAttr), TranslationView {

    private val textView = AppCompatTextView(context)

    init {
        addView(textView)
    }

    override fun loadContent(data: String) {
        textView.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(data, Html.FROM_HTML_MODE_COMPACT)
        } else {
            Html.fromHtml(data)
        }
    }
}