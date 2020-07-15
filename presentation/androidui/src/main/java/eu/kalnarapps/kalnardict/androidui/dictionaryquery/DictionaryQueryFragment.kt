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
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import eu.kalnarapps.kalnardict.androidui.R
import eu.kalnarapps.kalnardict.androidui.common.BaseFragment
import eu.kalnarapps.kalnardict.androidui.common.model.ChangeObserver
import eu.kalnarapps.kalnardict.androidui.dictionaryquery.model.DictionaryQueryState
import eu.kalnarapps.kalnardict.androidui.dictionaryquery.model.WordView
import eu.kalnarapps.kalnardict.androidui.dictionaryquery.observers.QueryResultObserver
import eu.kalnarapps.kalnardict.androidui.dictionaryquery.view.dropdownchoice.DictionarySelectorSpinnerAdapter
import eu.kalnarapps.kalnardict.androidui.dictionaryquery.view.resultlist.QueryResultListAdapter
import eu.kalnarapps.kalnardict.androidui.dictionaryquery.view.resultlist.listeners.OnWordClickedListener
import kotlinx.android.synthetic.main.dictionary_query_fragment.query_screen_spinner
import kotlinx.coroutines.ExperimentalCoroutinesApi


@ExperimentalCoroutinesApi
class DictionaryQueryFragment : BaseFragment<DictionaryQueryState>() {

    private var queryResultAdapter: QueryResultListAdapter? = null
    private var queryResultObserver: QueryResultObserver? = null
    override val viewModel: DictionaryQueryViewModel by navGraphViewModels(
        R.id.dictionary_query_navigation
    ) { DictionaryQueryViewModelFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        queryResultAdapter = QueryResultListAdapter(
            object : OnWordClickedListener {
                override fun onWordClicked(wordView: WordView) {
                    viewModel.onWordSelected(wordView)
                }
            }
        )
        queryResultObserver = QueryResultObserver {
            uiLogger.log("updating list: $it")
            queryResultAdapter?.updateWords(it.wordList)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        queryResultAdapter = null
        queryResultObserver = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(
            R.layout.dictionary_query_fragment,
            container,
            false
        ).apply {
            findViewById<TextInputEditText>(R.id.query_screen_input).addTextChangedListener {
                viewModel.onQueryChanged(it.toString())
            }
            val recycleView = findViewById<RecyclerView>(R.id.query_result_list_view)
            recycleView.adapter = queryResultAdapter

            queryResultObserver?.let { viewModel.getQueryResult().observe(viewLifecycleOwner, it) }

            viewModel.getLiveIsDictionaryListInitialized()
                .observe(viewLifecycleOwner, ChangeObserver {
                    setUpSpinner()
                })
        }
    }

    private fun setUpSpinner() {
        query_screen_spinner.apply {
            val dictionaries = viewModel.getRegisteredDictionaries()
            adapter = DictionarySelectorSpinnerAdapter(
                context,
                dictionaries,
                viewModel.getUiState().value?.currentDictionaryItemView
            ).apply {
                onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) = Unit

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

