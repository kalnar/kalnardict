package eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.table.info

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textfield.TextInputEditText
import eu.kalnarapps.kalnardict.androidui.R
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.model.ExternalTableUiInfo
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.model.SelectableLanguage
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.table.info.language.LanguageSelectorSpinnerAdapter

class TableInfoViewHolder(
    inflater: LayoutInflater, parent: ViewGroup,
    private val onTableInfoChangeListener: OnRegisterTablesListener
) : RecyclerView.ViewHolder(
    inflater.inflate(
        R.layout.dictionary_registry_table_info_item_view,
        parent,
        false
    )
) {
    private val titleView: TextView = itemView.findViewById(R.id.dictionary_name)
    private val languageFromView: TextView = itemView.findViewById(R.id.language_from_key)
    private val languageFromSelector: Spinner = itemView.findViewById(R.id.language_from_selector)
    private val languageToView: TextView = itemView.findViewById(R.id.language_to_key)
    private val languageToSelector: Spinner = itemView.findViewById(R.id.language_to_selector)
    private val dictionaryNameEditText: TextInputEditText =
        itemView.findViewById(R.id.dictionary_name_key_edit)
    private val registeringSwitch: SwitchMaterial =
        itemView.findViewById(R.id.table_registering_switch)

    fun bind(
        tableInfoUi: ExternalTableUiInfo,
        languageItemUiModels: List<SelectableLanguage.LanguageUi>
    ) {
        titleView.text = tableInfoUi.dictionaryName
        dictionaryNameEditText.apply {
            hint = context.getString(R.string.table_registration_dialog_dictionary_name_hint)
            setText(tableInfoUi.dictionaryName)
            addTextChangedListener {
                onTableInfoChangeListener.onChanged(
                    getTableUiInfo()
                )
            }
        }
        languageFromView.text = tableInfoUi.originalLanguageFrom
        languageToView.text = tableInfoUi.originalLanguageTo
        titleView.setOnClickListener {
            onTableInfoChangeListener.onChanged(getTableUiInfo())
        }
        languageFromSelector.apply {
            adapter = LanguageSelectorSpinnerAdapter(
                context = languageFromView.context,
                dictionarySelectorItems = languageItemUiModels
            )
            post {
                languageItemUiModels.find { it == tableInfoUi.languageFromUi }?.let {
                    setSelection(languageItemUiModels.indexOf(it))
                }
            }
            adapter.apply {
                onItemSelectedListener = OnLanguageSelectedListener()
            }
        }
        languageToSelector.apply {
            adapter = LanguageSelectorSpinnerAdapter(
                context = languageToView.context,
                dictionarySelectorItems = languageItemUiModels
            ).apply {
                onItemSelectedListener = OnLanguageSelectedListener()
            }
            post {
                languageItemUiModels.find { it == tableInfoUi.languageToUi }?.let {
                    setSelection(languageItemUiModels.indexOf(it))
                }
            }
        }
        registeringSwitch.setOnCheckedChangeListener { _, _ ->
            onTableInfoChangeListener.onChanged(
                getTableUiInfo()
            )
        }
    }

    private fun getTableUiInfo(): ExternalTableUiInfo {
        return ExternalTableUiInfo(
            originalTableName = titleView.text.toString(),
            dictionaryName = dictionaryNameEditText.editableText.toString(),
            originalLanguageTo = languageToView.text.toString(),
            originalLanguageFrom = languageFromView.text.toString(),
            languageFromUi = languageFromSelector.selectedItem as SelectableLanguage,
            languageToUi = languageToSelector.selectedItem as SelectableLanguage,
            isSelected = registeringSwitch.isChecked
        )
    }

    private inner class OnLanguageSelectedListener() :
        AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {
            onTableInfoChangeListener.onChanged(
                getTableUiInfo()
            )
        }

        override fun onItemSelected(
            parent: AdapterView<*>?,
            view: View?,
            position: Int,
            id: Long
        ) {
            onTableInfoChangeListener.onChanged(
                getTableUiInfo()
            )
        }
    }
}
