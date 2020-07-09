package eu.kalnarapps.kalnardict.androidui.dictionaryquery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import eu.kalnarapps.kalnardict.androidui.R
import eu.kalnarapps.kalnardict.androidui.common.BaseFragment
import eu.kalnarapps.kalnardict.androidui.common.model.ChangeObserver
import eu.kalnarapps.kalnardict.androidui.dictionaryquery.dropdownchoice.DictionarySelectorSpinnerAdapter
import eu.kalnarapps.kalnardict.androidui.dictionaryquery.listview.QueryResultListAdapter
import kotlinx.android.synthetic.main.dictionary_query_fragment.query_result_list_view
import kotlinx.android.synthetic.main.dictionary_query_fragment.query_screen_input
import kotlinx.android.synthetic.main.dictionary_query_fragment.query_screen_spinner
import org.koin.androidx.viewmodel.ext.android.viewModel


class DictionaryQueryFragment : BaseFragment<DictionaryQueryState>() {

    override val viewModel: DictionaryQueryViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.dictionary_query_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        query_screen_input.addTextChangedListener {
            viewModel.onQueryChanged(it.toString())
        }

        query_result_list_view.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = QueryResultListAdapter()
            viewModel.getQueryResult().observe(viewLifecycleOwner, Observer {
                (adapter as QueryResultListAdapter).updateWords(it)
            })
        }
        viewModel.getLiveIsDictionaryListInitialized().observe(viewLifecycleOwner, ChangeObserver {
            setUpSpinner()
        })

        viewModel.getDictionary().observe(viewLifecycleOwner, ChangeObserver {
            viewModel.refreshQueryResults()
        })

    }

    private fun setUpSpinner() {
        query_screen_spinner.apply {
            adapter = DictionarySelectorSpinnerAdapter(
                context,
                viewModel.getRegisteredDictionaries()
            ).apply {
                onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        viewModel.onDictionaryChanged(getItem(0))
                    }

                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        viewModel.onDictionaryChanged(getItem(position))
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.dicitonay_query_menus, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.dictionary_manager_menu -> {
                viewModel.onDictionaryManagerMenu()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}

