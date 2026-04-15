package eu.kalnarapps.kalnardict.androidui.dictionaryquery.view.translation.views

import android.content.Context
import android.os.Build
import android.text.Html
import android.util.AttributeSet
import android.util.Base64
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.widget.FrameLayout
import android.widget.ScrollView
import androidx.appcompat.widget.AppCompatTextView
import eu.kalnarapps.kalnardict.androidui.dictionaryquery.view.translation.TranslationView
import  org.koin.core.component.KoinComponent

class TextViewHtmlTranslationView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ScrollView(context, attrs, defStyleAttr), TranslationView {

    private val textView = AppCompatTextView(context)

    init {
        addView(textView)
    }

    override fun loadContent(data: String) {

        Log.d("kd.html", "before: ${System.currentTimeMillis()}")
        textView.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(data, Html.FROM_HTML_MODE_COMPACT)
        } else {
            Html.fromHtml(data)
        }
        Log.d("kd.html", "after: ${System.currentTimeMillis()}")
    }
}


class WebViewHtmlTranslationView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), TranslationView, KoinComponent {

    private val webView: WebView = WebView(context)

    init {
        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        webView.isVerticalScrollBarEnabled = false
        addView(webView)
    }

    override fun loadContent(data: String) {
        webView.loadData(
            Base64.encodeToString(data.toByteArray(), Base64.NO_PADDING),
            "text/html; charset=utf-8",
            "base64"
        )
    }
}
