package eu.kalnarapps.kalnardict.androidui.dictionarymanager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import eu.kalnarapps.kalnardict.androidui.R
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.dictionary.ManageableDictionaryListAdapter
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.dictionary.OnDictionaryClickListener
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.dictionary.OnNewButtonAction
import kotlinx.android.synthetic.main.dictionary_manager_fragment.*
import org.koin.androidx.viewmodel.ext.android.getKoin


class DictionaryManagerFragment : Fragment() {

    //    private val dictionaryManagerViewModel: DictionaryManagerViewModel by viewModel()
    private val dictionaryManagerViewModel: DictionaryManagerViewModel = getKoin().get()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dictionary_manager_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
                        Toast.makeText(context, "new button clicked", Toast.LENGTH_SHORT).show()
                    }

                }
            )
        }
    }

}