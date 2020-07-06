package eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.dialog.newlanguage

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.navigation.navGraphViewModels
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.google.android.material.button.MaterialButton
import eu.kalnarapps.kalnardict.androidui.R
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.DictionaryRegistryViewModel


class NewLanguageDialog : DialogFragment() {

    private val viewModel: DictionaryRegistryViewModel by navGraphViewModels(
        R.id.dictionary_registration_navigation
    )

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialDialog(
            requireContext()
        ).customView(R.layout.new_language_dialog).also {
            it.getCustomView().setUpView()
        }
    }

    private fun View.setUpView() {
        val button: MaterialButton = findViewById(R.id.add_new_language_dialog_button)

        button.setOnClickListener {
            Toast.makeText(requireContext(), "clicked language registry", Toast.LENGTH_SHORT).show()
        }
    }
}

