package eu.kalnarapps.kalnardict.androidui.dictionarymanager.main.dictionary

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import eu.kalnarapps.kalnardict.androidui.R
import eu.kalnarapps.kalnardict.androidui.common.spinner.SimpleListSpinnerAdapter
import eu.kalnarapps.kalnardict.androidui.common.viewextensions.visibleXorGone
import eu.kalnarapps.kalnardict.presentation.models.dictionarymanager.DictionaryUpdateUi
import eu.kalnarapps.kalnardict.presentation.models.dictionarymanager.ManageableDictionaryView
import eu.kalnarapps.kalnardict.presentation.models.translations.RenderingStrategy

class ManageableDictionaryViewHolder(
    inflater: LayoutInflater, parent: ViewGroup,
    private val onDictionaryUpdateListener: OnDictionaryUpdateListener
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
    private val card: MaterialCardView = itemView.findViewById(R.id.manageable_dictionary_view)
    private val updateView: ConstraintLayout = itemView.findViewById(R.id.dictionary_update_view)
    private val updateRenderingStrategySpinner: Spinner =
        itemView.findViewById(R.id.dictionary_update_rendering_value)
    private val submitUpdateCta: AppCompatButton =
        itemView.findViewById(R.id.dictionary_update_submit_cta)
    private val deleteCta: AppCompatButton =
        itemView.findViewById(R.id.dictionary_delete_cta)


    fun bind(manageableDictionaryView: ManageableDictionaryView) {
        titleView.text = manageableDictionaryView.dictionaryName
        languageFromView.text = manageableDictionaryView.sourceLanguage
        languageToView.text = manageableDictionaryView.destinationLanguage
        card.setOnClickListener {
            updateView.visibleXorGone(!updateView.isVisible)
        }
        updateRenderingStrategySpinner.apply {
            adapter = SimpleListSpinnerAdapter(
                context = this.context,
                dictionarySelectorItems = manageableDictionaryView.availableRenderingStrategy
            )
            post {
                with(manageableDictionaryView.availableRenderingStrategy) {
                    find { it == manageableDictionaryView.currentRenderingStrategy }
                        ?.let {
                            setSelection(this.indexOf(it))
                        }
                }
            }
        }
        submitUpdateCta.setOnClickListener {
            onDictionaryUpdateListener.onClick(
                DictionaryUpdateUi.Info(
                    dictionaryId = manageableDictionaryView.dictionaryId,
                    renderingStrategy = updateRenderingStrategySpinner.selectedItem as RenderingStrategy
                )
            )
        }

        deleteCta.setOnClickListener {
            manageableDictionaryView.onDeleteAction?.invoke(manageableDictionaryView.dictionaryId)
        }

    }
}