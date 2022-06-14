package eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.map
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import eu.kalnarapps.kalnardict.androidui.R
import eu.kalnarapps.kalnardict.androidui.common.BaseFragment
import eu.kalnarapps.kalnardict.androidui.common.model.ChangeObserver
import eu.kalnarapps.kalnardict.androidui.databinding.DictionaryRegistryFragmentBinding
import eu.kalnarapps.kalnardict.presentation.models.dictionaryregistry.DictionaryRegistryState
import eu.kalnarapps.kalnardict.presentation.models.dictionaryregistry.ExternalTableUiInfo
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.table.info.OnRegisterInfoUpdateListener
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.table.info.TableInfoListAdapter
import eu.kalnarapps.kalnardict.presentation.models.common.LoadableContent
import org.koin.android.ext.android.getKoin
import org.koin.core.parameter.parametersOf

class DictionaryRegistryFragment : BaseFragment<DictionaryRegistryState>() {

    private val args: DictionaryRegistryFragmentArgs by navArgs()

    override val viewModel: DictionaryRegistryViewModel by navGraphViewModels(
        R.id.dictionary_registration_navigation
    ) {
        getKoin().get<AbstractDictionaryRegistryViewModelFactory> { parametersOf(args.dbPath) }
    }

    private var _binding: DictionaryRegistryFragmentBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DictionaryRegistryFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getUiState().map { it.registerDictionaryUiModels }.observe(viewLifecycleOwner) {
            when (it) {
                is LoadableContent.Failed -> {
                    binding.invalidDbGroup.isVisible = true
                    binding.validDbGroup.isVisible = false
                    uiLogger.d("fragment", it.failure.errorMessage())
                }
                is LoadableContent.Completed -> {
                    binding.invalidDbGroup.isVisible = false
                    binding.validDbGroup.isVisible = true
                }
            }
        }

        viewModel.getLiveIsTableListInitialized().observe(viewLifecycleOwner, ChangeObserver {
            if (it) {
                setUpTableInfoList()
            }
        })
        binding.tableInfoListSubmitButton.apply {
            setOnClickListener {
                viewModel.registerDictionaries()
            }
        }
        binding.dictionaryRegistryNewLanguageButton.setOnClickListener {
            viewModel.onLanguageAdditionRequest()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setUpTableInfoList() {
        binding.listRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = TableInfoListAdapter(
                viewModel.getRegisterDictionaryUiModels(),
                object : OnRegisterInfoUpdateListener {
                    override fun onChanged(newTableInfoUiModel: ExternalTableUiInfo) {
                        viewModel.onTableRegisteringUpdate(newTableInfoUiModel)
                    }
                }
            ).apply {
                viewModel.getAvailableLanguages().observe(viewLifecycleOwner, Observer {
                    this.updateList(viewModel.getRegisterDictionaryUiModels())
                })
            }
        }
    }


}