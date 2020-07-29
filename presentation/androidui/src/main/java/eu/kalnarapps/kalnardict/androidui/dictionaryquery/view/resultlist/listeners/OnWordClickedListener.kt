package eu.kalnarapps.kalnardict.androidui.dictionaryquery.view.resultlist.listeners

import eu.kalnarapps.kalnardict.presentation.models.dictionaryquery.WordView

interface OnWordClickedListener {
    fun onWordClicked(wordView: WordView)
}