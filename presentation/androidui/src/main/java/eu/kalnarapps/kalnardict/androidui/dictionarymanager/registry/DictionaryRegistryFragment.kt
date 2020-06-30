package eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import eu.kalnarapps.kalnardict.androidui.R
import eu.kalnarapps.kalnardict.androidui.common.BaseFragment
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.model.DictionaryRegistryState
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.model.ExternalTableUiInfo
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.table.info.OnRegisterTablesListener
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.table.info.TableInfoListAdapter
import kotlinx.android.synthetic.main.dictionary_manager_fragment.list_recycler_view
import kotlinx.android.synthetic.main.dictionary_registry_fragment.table_info_list_submit_button
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class DictionaryRegistryFragment : BaseFragment<DictionaryRegistryState>() {

    private val args: DictionaryRegistryFragmentArgs by navArgs()
    override val viewModel: DictionaryRegistryViewModel by viewModel { parametersOf(args.dbPath) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dictionary_registry_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        list_recycler_view.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = TableInfoListAdapter(
                viewModel.getTables(),
                viewModel.getKnownLanguages(),
                object : OnRegisterTablesListener {
                    override fun onChanged(newTableInfoUiModel: ExternalTableUiInfo) {
                        viewModel.onTableRegisteringUpdate(newTableInfoUiModel)
                    }
                }
            )
        }
        table_info_list_submit_button.apply {
            setOnClickListener {
                viewModel.registerDictionaries()
            }
        }

    }

}