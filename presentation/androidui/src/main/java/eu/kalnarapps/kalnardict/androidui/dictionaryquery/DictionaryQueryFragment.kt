package eu.kalnarapps.kalnardict.androidui.dictionaryquery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import eu.kalnarapps.kalnardict.android.utils.Logger
import eu.kalnarapps.kalnardict.androidui.R
import eu.kalnarapps.kalnardict.androidui.dictionaryquery.dropdownchoice.DictionarySelectorSpinnerAdapter
import eu.kalnarapps.kalnardict.androidui.dictionaryquery.listview.QueryResultListAdapter
import kotlinx.android.synthetic.main.dictionary_query_fragment.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class DictionaryQueryFragment : Fragment() {

    private val queryViewModel: DictionaryQueryViewModel by viewModel()
    private val logger: Logger by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dictionary_query_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        query_screen_input.addTextChangedListener {
            queryViewModel.onQueryChanged(it.toString())
        }

        query_result_list_view.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = QueryResultListAdapter()
            queryViewModel.getQueryResult().observe(viewLifecycleOwner, Observer {
                (adapter as QueryResultListAdapter).updateWords(it)
            })
        }
        query_screen_spinner.apply {
            adapter = DictionarySelectorSpinnerAdapter(
                context,
                queryViewModel.getRegisteredDictionaries()
            ).apply {
                onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        queryViewModel.onDictionaryChanged(getItem(0))
                    }

                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        queryViewModel.onDictionaryChanged(getItem(position))
                    }
                }
            }
        }

        queryViewModel.getDictionary().observe(viewLifecycleOwner, Observer {
            queryViewModel.refreshQueryResults()
        })

    }
}

