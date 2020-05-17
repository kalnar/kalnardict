package eu.kalnarapps.kalnardict.androidui.dictionarymanager

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.dictionary.ManageableDictionaryListAdapter
import kotlinx.android.synthetic.main.dictionary_manager_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class DictionaryManagerFragment : Fragment() {

    private val dictionaryManagerViewModel: DictionaryManagerViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        list_recycler_view.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = ManageableDictionaryListAdapter(
                dictionaryManagerViewModel.getRegisteredDictionaries()
            )
        }
    }

}