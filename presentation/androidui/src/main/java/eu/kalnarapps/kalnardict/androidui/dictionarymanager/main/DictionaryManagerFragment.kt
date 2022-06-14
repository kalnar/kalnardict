package eu.kalnarapps.kalnardict.androidui.dictionarymanager.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import eu.kalnarapps.kalnardict.androidui.common.BaseFragment
import eu.kalnarapps.kalnardict.androidui.common.viewextensions.visibleXorGone
import eu.kalnarapps.kalnardict.androidui.databinding.DictionaryManagerFragmentBinding
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.main.dictionary.ManageableDictionaryListAdapter
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.main.dictionary.OnDictionaryUpdateListener
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.main.dictionary.OnNewButtonAction
import eu.kalnarapps.kalnardict.common.extentions.exhaustive
import eu.kalnarapps.kalnardict.presentation.models.common.LoadableContent
import eu.kalnarapps.kalnardict.presentation.models.dictionarymanager.DictionaryManagerState
import eu.kalnarapps.kalnardict.presentation.models.dictionarymanager.DictionaryUpdateUi
import org.koin.androidx.viewmodel.ext.android.viewModel


open class DictionaryManagerFragment : BaseFragment<DictionaryManagerState>() {

    override val viewModel: DictionaryManagerViewModel by viewModel()

    private var _binding: DictionaryManagerFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DictionaryManagerFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.dictionaryManagerListRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = ManageableDictionaryListAdapter(
                object : OnDictionaryUpdateListener {
                    override fun onClick(dictionaryUpdate: DictionaryUpdateUi.Info) {
                        viewModel.updateDictionary(dictionaryUpdate)
                    }
                },
                object : OnNewButtonAction {
                    override fun invoke() {
                        viewModel.onImportDbClicked()
                    }

                }
            )
            viewModel.getRegisteredDictionaries().observe(viewLifecycleOwner, Observer {
                when (it) {
                    LoadableContent.UnInitialized,
                    is LoadableContent.Failed,
                    LoadableContent.Loading -> Unit
                    is LoadableContent.Completed -> {
                        binding.dictionaryManagerLoader.visibility = View.GONE
                        binding.dictionaryManagerNoDictionaryDescription.visibleXorGone(it.content.isEmpty())

                        (adapter as ManageableDictionaryListAdapter).updateList(it.content)

                        Unit
                    }
                }.exhaustive
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}