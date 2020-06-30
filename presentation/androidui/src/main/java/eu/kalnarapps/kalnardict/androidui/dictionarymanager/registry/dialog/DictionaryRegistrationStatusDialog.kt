package eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import eu.kalnarapps.kalnardict.androidui.R
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.DictionaryRegistryViewModel
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.DictionaryRegistryViewModelFactory
import eu.kalnarapps.kalnardict.common.operations.OperationResult


class DictionaryRegistrationStatusDialog : DialogFragment() {

    private val args: DictionaryRegistrationStatusDialogArgs by navArgs()

    private val viewModel: DictionaryRegistryViewModel by navGraphViewModels(
        R.id.dictionary_registration_navigation
    ) {
        DictionaryRegistryViewModelFactory(args.dbPath)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialDialog(
            requireContext()
        ).customView(R.layout.simple_dialog_with_ok_button).also {
            it.getCustomView().setUpView()
        }
    }

    private fun View.setUpView() {
        val titleView: TextView = findViewById(R.id.title_content)
        val descriptionView: TextView = findViewById(R.id.simple_dialog_description)
        val button: AppCompatButton = findViewById(R.id.simple_dialog_cta)

        titleView.text = "registration status"
        descriptionView.text = viewModel.getRegistrationStatus().map {
            if (it.result is OperationResult.Success) {
                "${it.originalName} was saved as ${it.registeringName} successfully"
            } else {
                "importing ${it.originalName} failed"
            }
        }.joinToString(separator = "\n").ifBlank {
            "an error has occurred: no import was detected"
        }

        button.setOnClickListener {
            this@DictionaryRegistrationStatusDialog.dismiss()
            viewModel.onDialogButtonClicked()
        }
    }
}

