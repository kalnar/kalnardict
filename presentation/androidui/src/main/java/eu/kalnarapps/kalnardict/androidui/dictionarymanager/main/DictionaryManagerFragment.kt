package eu.kalnarapps.kalnardict.androidui.dictionarymanager.main

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import eu.kalnarapps.kalnardict.androidui.R
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.main.dictionary.ManageableDictionaryListAdapter
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.main.dictionary.OnDictionaryClickListener
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.main.dictionary.OnNewButtonAction
import eu.kalnarapps.kalnardict.androidui.navigation.ScreenNavigator
import kotlinx.android.synthetic.main.dictionary_manager_fragment.list_recycler_view
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


open class DictionaryManagerFragment : Fragment() {

    private val dictionaryManagerViewModel: DictionaryManagerViewModel by viewModel()
    private val navigator: ScreenNavigator by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dictionary_manager_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listenToNavigationCommands()
        list_recycler_view.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = ManageableDictionaryListAdapter(
                dictionaryManagerViewModel.getRegisteredDictionaries(),
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
                        Toast.makeText(context, "new button clicked", Toast.LENGTH_SHORT).show()
                    }

                }
            )
        }
    }

    private val getContent = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        Toast.makeText(requireContext(), "get uri: $uri", Toast.LENGTH_SHORT).show()
        dictionaryManagerViewModel.onDbSelected(uri?.path.orEmpty())
    }

    private fun listenToNavigationCommands() {
        dictionaryManagerViewModel.navigationCommand.observe(
            viewLifecycleOwner,
            Observer { navCommand ->
                navigator.execute(navCommand)
            })
    }

}