package eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.dialog.newlanguage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.navGraphViewModels
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import eu.kalnarapps.kalnardict.androidui.R
import eu.kalnarapps.kalnardict.androidui.common.model.NewChangeObserver
import eu.kalnarapps.kalnardict.androidui.common.model.UiEventObserver
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.DictionaryRegistryViewModel
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.model.RegistryError
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.model.SelectableLanguage
import eu.kalnarapps.kalnardict.common.extentions.exhaustive


class NewLanguageDialog : DialogFragment() {

    private val viewModel: DictionaryRegistryViewModel by navGraphViewModels(
        R.id.dictionary_registration_navigation
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.new_language_dialog, container, false).apply {
            setUpView()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(
            STYLE_NORMAL,
            theme
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getAvailableLanguages().observe(viewLifecycleOwner, NewChangeObserver {
            dismiss()
        })

        val newLanguageIdEditText: TextInputEditText =
            view.findViewById(R.id.add_new_language_dialog_id_edit_text)

        viewModel.registryError.observe(viewLifecycleOwner, UiEventObserver {
            when (it) {
                RegistryError.LanguageIdDuplicate -> {
                    newLanguageIdEditText.error =
                        "id already used, please use an other id"
                }
            }.exhaustive
        })

    }

    private fun View.setUpView() {
        val button: MaterialButton = findViewById(R.id.add_new_language_dialog_button)
        val newLanguageIdEditText: TextInputEditText =
            findViewById(R.id.add_new_language_dialog_id_edit_text)
        val newLanguageNameEditText: TextInputEditText =
            findViewById(R.id.add_new_language_dialog_description_edit_text)

        button.setOnClickListener {
            viewModel.onNewLanguageRegistryClicked(
                SelectableLanguage.LanguageUi(
                    code = newLanguageIdEditText.editableText.toString(),
                    name = newLanguageNameEditText.editableText.toString()
                )
            )
        }
    }
}

