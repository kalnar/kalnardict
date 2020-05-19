package eu.kalnarapps.kalnardict.androidui.dictionarymanager.dictionary

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import eu.kalnarapps.kalnardict.androidui.R

class NewButtonViewHolder(
    inflater: LayoutInflater, parent: ViewGroup
) : RecyclerView.ViewHolder(
    inflater.inflate(
        R.layout.dictionary_manager_new_dictionary_button_view,
        parent,
        false
    )
) {

    private val button: ImageView = itemView.findViewById(R.id.new_dictionary_button)

    fun bind(onNewButtonAction: OnNewButtonAction) {
        button.setOnClickListener {
            onNewButtonAction.invoke()
        }
    }
}