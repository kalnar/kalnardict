package eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import eu.kalnarapps.kalnardict.androidui.R
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.model.ExternalTableUiInfo
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.table.info.OnRegisterTablesListener
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.table.info.TableInfoListAdapter
import eu.kalnarapps.kalnardict.androidui.navigation.NavigationCommand
import eu.kalnarapps.kalnardict.androidui.navigation.ScreenNavigator
import kotlinx.android.synthetic.main.dictionary_manager_fragment.list_recycler_view
import kotlinx.android.synthetic.main.dictionary_registry_fragment.table_info_list_submit_button
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class DictionaryRegistryFragment : Fragment() {

    private val args: DictionaryRegistryFragmentArgs by navArgs()
    private val registryViewModel: DictionaryRegistryViewModel by viewModel { parametersOf(args.dbPath) }
    private val navigator: ScreenNavigator by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dictionary_registry_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listenToNavigationCommands()
        registryViewModel.getUiState().observe(viewLifecycleOwner, Observer {
            Log.d("ui.state", it.toString())
        })
        list_recycler_view.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = TableInfoListAdapter(
                registryViewModel.getTables(),
                registryViewModel.getKnownLanguages(),
                object : OnRegisterTablesListener {
                    override fun onChanged(newTableInfoUiModel: ExternalTableUiInfo) {
                        registryViewModel.onTableRegisteringUpdate(newTableInfoUiModel)
                    }
                }
            )
        }
        table_info_list_submit_button.apply {
            setOnClickListener {
                registryViewModel.registerDictionaries()
            }
        }

    }

    private fun listenToNavigationCommands() {
        registryViewModel.navigationCommand.observe(viewLifecycleOwner, Observer { navCommand ->
            if (navCommand != NavigationCommand.DoNothing) {
                navigator.execute(navCommand)
                registryViewModel.resetNavigation()
            }
        })
    }
}