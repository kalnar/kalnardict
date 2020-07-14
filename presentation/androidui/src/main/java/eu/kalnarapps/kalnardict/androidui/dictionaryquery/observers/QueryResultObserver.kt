package eu.kalnarapps.kalnardict.androidui.dictionaryquery.observers

import androidx.lifecycle.Observer
import eu.kalnarapps.kalnardict.androidui.dictionaryquery.model.QueryResult

class QueryResultObserver(
    private val onObserve: (QueryResult) -> Unit
) : Observer<QueryResult> {
    private var lastDictionaryId: Int? = null
    private var listSize: Int = 0
    override fun onChanged(t: QueryResult) {
        if (lastDictionaryId != t.dictionary.id || t.wordList.size != listSize) {
            lastDictionaryId = t.dictionary.id
            listSize = t.wordList.size
            onObserve(t)
        }
    }
}