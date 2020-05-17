package eu.kalnarapps.kalnardict.androidui.dictionarymanager.dictionary

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import eu.kalnarapps.kalnardict.androidui.R
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.ManageableDictionaryView

class ManageableDictionaryViewHolder(
    inflater: LayoutInflater, parent: ViewGroup
) : RecyclerView.ViewHolder(
    inflater.inflate(
        R.layout.dictionary_manager_manageable_dictionary_item_view,
        parent,
        false
    )
) {
    private val titleView: TextView = itemView.findViewById(R.id.dictionary_name)
    private val languageFromView: TextView = itemView.findViewById(R.id.language_form)
    private val languageToView: TextView = itemView.findViewById(R.id.language_to)

    fun bind(manageableDictionaryView: ManageableDictionaryView) {
        titleView.text = manageableDictionaryView.dictionaryName
        languageFromView.text = manageableDictionaryView.sourceLanguage
        languageToView.text = manageableDictionaryView.destinationLanguage
    }
}