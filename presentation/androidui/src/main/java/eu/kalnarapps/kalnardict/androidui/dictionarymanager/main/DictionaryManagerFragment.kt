package eu.kalnarapps.kalnardict.androidui.dictionarymanager.main

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import eu.kalnarapps.kalnardict.android.utils.uri.UriAdapter
import eu.kalnarapps.kalnardict.androidui.R
import eu.kalnarapps.kalnardict.androidui.common.BaseFragment
import eu.kalnarapps.kalnardict.androidui.common.viewextensions.visibleIf
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.main.dictionary.ManageableDictionaryListAdapter
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.main.dictionary.OnDictionaryClickListener
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.main.dictionary.OnNewButtonAction
import kotlinx.android.synthetic.main.dictionary_manager_fragment.dictionary_manager_no_dictionary_description
import kotlinx.android.synthetic.main.dictionary_manager_fragment.list_recycler_view
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


open class DictionaryManagerFragment : BaseFragment<DictionaryManagerState>() {

    override val viewModel: DictionaryManagerViewModel by viewModel()
    private val uriAdapter: UriAdapter by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dictionary_manager_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dictionaries = viewModel.getRegisteredDictionaries()
        dictionary_manager_no_dictionary_description.visibleIf(dictionaries.isEmpty())
        list_recycler_view.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = ManageableDictionaryListAdapter(
                dictionaries,
                object : OnDictionaryClickListener {
                    override fun onClick(dictionaryView: ManageableDictionaryView) {
                        Toast.makeText(
                            context,
                            "${dictionaryView.dictionaryName} clicked",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                object : OnNewButtonAction {
                    override fun invoke() {
                        getContent.launch("*/*")
                    }

                }
            )
        }
    }

    private val getContent = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        viewModel.onDbSelected(uriAdapter.convertUriToSdcardPath(uri))
    }
}