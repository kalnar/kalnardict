package eu.kalnarapps.kalnardict.androidui.dictionarymanager.main

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import eu.kalnarapps.kalnardict.android.utils.uri.UriAdapter
import eu.kalnarapps.kalnardict.androidui.R
import eu.kalnarapps.kalnardict.androidui.common.BaseFragment
import eu.kalnarapps.kalnardict.androidui.common.viewextensions.visibleXorGone
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.main.dictionary.ManageableDictionaryListAdapter
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.main.dictionary.OnDictionaryUpdateListener
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.main.dictionary.OnNewButtonAction
import eu.kalnarapps.kalnardict.common.extentions.exhaustive
import eu.kalnarapps.kalnardict.presentation.models.common.LoadableContent
import eu.kalnarapps.kalnardict.presentation.models.dictionarymanager.DictionaryManagerState
import eu.kalnarapps.kalnardict.presentation.models.dictionarymanager.DictionaryUpdateUi
import kotlinx.android.synthetic.main.dictionary_manager_fragment.dictionary_manager_list_recycler_view
import kotlinx.android.synthetic.main.dictionary_manager_fragment.dictionary_manager_loader
import kotlinx.android.synthetic.main.dictionary_manager_fragment.dictionary_manager_no_dictionary_description
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
        dictionary_manager_list_recycler_view.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = ManageableDictionaryListAdapter(
                object : OnDictionaryUpdateListener {
                    override fun onClick(dictionaryUpdate: DictionaryUpdateUi.Info) {
                        viewModel.updateDictionary(dictionaryUpdate)
                    }
                },
                object : OnNewButtonAction {
                    override fun invoke() {
                        getContent.launch("*/*")
                    }

                }
            )
            viewModel.getRegisteredDictionaries().observe(viewLifecycleOwner, Observer {
                when (it) {
                    LoadableContent.UnInitialized,
                    LoadableContent.Loading -> Unit
                    is LoadableContent.Completed -> {
                        dictionary_manager_loader.visibility = View.GONE
                        dictionary_manager_no_dictionary_description.visibleXorGone(it.content.isEmpty())

                        (adapter as ManageableDictionaryListAdapter).updateList(it.content)

                        Unit
                    }
                }.exhaustive
            })
        }
    }

    private val getContent = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        viewModel.onDbSelected(uriAdapter.convertUriToSdcardPath(uri))
    }
}