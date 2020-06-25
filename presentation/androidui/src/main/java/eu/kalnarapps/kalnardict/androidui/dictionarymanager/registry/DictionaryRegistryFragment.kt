package eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import eu.kalnarapps.kalnardict.androidui.R
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.table.info.OnTableInfoClickListener
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.table.info.TableInfoListAdapter
import kotlinx.android.synthetic.main.dictionary_manager_fragment.list_recycler_view
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class DictionaryRegistryFragment : Fragment() {

    private val args: DictionaryRegistryFragmentArgs by navArgs()
    private val registryViewModel: DictionaryRegistryViewModel by viewModel { parametersOf(args.dbPath) }

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
                registryViewModel.getTables(),
                object : OnTableInfoClickListener {
                    override fun onClick(dictionaryView: ExternalTableUiInfo) {
                        Toast.makeText(
                            context,
                            "${dictionaryView.dictionaryName} clicked",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            )
        }
    }
}