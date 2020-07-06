package eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import eu.kalnarapps.kalnardict.androidui.R
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.DictionaryRegistryViewModel
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.DictionaryRegistryViewModelFactory


class DictionaryRegistrationStatusDialog : DialogFragment() {

    private val args: DictionaryRegistrationStatusDialogArgs by navArgs()

    private val viewModel: DictionaryRegistryViewModel by navGraphViewModels(
        R.id.dictionary_registration_navigation
    )
//
//    {
//        DictionaryRegistryViewModelFactory(args.dbPath)
//    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialDialog(
            requireContext()
        ).customView(R.layout.dictionary_registry_dialog).also {
            it.getCustomView().setUpView()
        }
    }

    private fun View.setUpView() {
        val statusList: RecyclerView = findViewById(R.id.registration_dialog_status_list)
        val button: AppCompatButton = findViewById(R.id.simple_dialog_cta)

        statusList.layoutManager = LinearLayoutManager(requireContext())
        statusList.adapter = ImportResultListAdapter(
            viewModel.getRegistrationStatus()
        )

        button.setOnClickListener {
            this@DictionaryRegistrationStatusDialog.dismiss()
            viewModel.onDialogButtonClicked()
        }
    }
}

