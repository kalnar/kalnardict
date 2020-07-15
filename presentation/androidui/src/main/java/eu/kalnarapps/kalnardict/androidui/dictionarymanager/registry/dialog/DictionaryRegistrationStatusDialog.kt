package eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import eu.kalnarapps.kalnardict.androidui.R
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.DictionaryRegistryViewModel

class DictionaryRegistrationStatusDialog : DialogFragment() {

    private val viewModel: DictionaryRegistryViewModel by navGraphViewModels(
        R.id.dictionary_registration_navigation
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dictionary_registry_dialog, container, false).apply {
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

    private fun View.setUpView() {
        val statusList: RecyclerView = findViewById(R.id.registration_dialog_status_list)
        val button: AppCompatButton = findViewById(R.id.simple_dialog_cta)

        statusList.layoutManager = LinearLayoutManager(requireContext())
        statusList.adapter = ImportResultListAdapter(
            viewModel.getRegistrationStatus()
        )
        viewModel.getLiveRegistrationStatus().observe(viewLifecycleOwner, Observer {
            (statusList.adapter as ImportResultListAdapter).update(it)
        })

        button.setOnClickListener {
            this@DictionaryRegistrationStatusDialog.dismiss()
            viewModel.onDialogButtonClicked()
        }
    }
}

