package eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import eu.kalnarapps.kalnardict.androidui.R
import eu.kalnarapps.kalnardict.androidui.common.BaseFragment
import eu.kalnarapps.kalnardict.androidui.common.model.ChangeObserver
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.model.DictionaryRegistryState
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.model.ExternalTableUiInfo
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.table.info.OnRegisterInfoUpdateListener
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.table.info.TableInfoListAdapter
import kotlinx.android.synthetic.main.dictionary_manager_fragment.list_recycler_view
import kotlinx.android.synthetic.main.dictionary_registry_fragment.dictionary_registry_new_language_button
import kotlinx.android.synthetic.main.dictionary_registry_fragment.table_info_list_submit_button

class DictionaryRegistryFragment : BaseFragment<DictionaryRegistryState>() {

    private val args: DictionaryRegistryFragmentArgs by navArgs()

    override val viewModel: DictionaryRegistryViewModel by navGraphViewModels(
        R.id.dictionary_registration_navigation
    ) {
        DictionaryRegistryViewModelFactory(args.dbPath)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dictionary_registry_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getLiveIsTableListInitialized().observe(viewLifecycleOwner, ChangeObserver {
            if (it) {
                setUpTableInfoList()
            }
        })
        table_info_list_submit_button.apply {
            setOnClickListener {
                viewModel.registerDictionaries()
            }
        }
        dictionary_registry_new_language_button.setOnClickListener {
            viewModel.onLanguageAdditionRequest()
        }

    }

    private fun setUpTableInfoList() {
        list_recycler_view.apply {
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