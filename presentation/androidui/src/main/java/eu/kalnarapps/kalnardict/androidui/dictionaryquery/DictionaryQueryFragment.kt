package eu.kalnarapps.kalnardict.androidui.dictionaryquery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.Spinner
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import eu.kalnarapps.kalnardict.androidui.R
import eu.kalnarapps.kalnardict.androidui.common.BaseFragment
import eu.kalnarapps.kalnardict.androidui.dialogs.listwindows.textlist.SimpleListAdapter
import eu.kalnarapps.kalnardict.androidui.dialogs.listwindows.textlist.SimpleTextListWindowBuilder
import eu.kalnarapps.kalnardict.androidui.dictionaryquery.model.DictionaryQueryState
import eu.kalnarapps.kalnardict.androidui.dictionaryquery.model.QueryResult
import eu.kalnarapps.kalnardict.androidui.dictionaryquery.model.WordView
import eu.kalnarapps.kalnardict.androidui.dictionaryquery.view.dropdownchoice.DictionarySelectorSpinnerAdapter
import eu.kalnarapps.kalnardict.androidui.dictionaryquery.view.resultlist.QueryResultListAdapter
import eu.kalnarapps.kalnardict.androidui.dictionaryquery.view.resultlist.listeners.OnWordClickedListener
import eu.kalnarapps.kalnardict.common.extentions.exhaustive
import eu.kalnarapps.kalnardict.presentation.models.common.LoadableContent
import eu.kalnarapps.kalnardict.presentation.models.dictionaryquery.ListTextItem
import kotlinx.android.synthetic.main.dictionary_query_fragment.dictionary_query_loader
import kotlinx.android.synthetic.main.dictionary_query_fragment.query_result_list_view
import kotlinx.coroutines.ExperimentalCoroutinesApi


@ExperimentalCoroutinesApi
class DictionaryQueryFragment : BaseFragment<DictionaryQueryState>() {

    private var queryResultAdapter: QueryResultListAdapter? = null
    private var queryResultObserver: Observer<QueryResult>? = null
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
        queryResultObserver = Observer {
            uiLogger.log("updating list: $it")
            when (it.wordList) {
                LoadableContent.UnInitialized,
                LoadableContent.Loading -> Unit
                is LoadableContent.Completed -> {
                    dictionary_query_loader.visibility = View.GONE
                    query_result_list_view.visibility = View.VISIBLE
                    queryResultAdapter?.updateWords(it.wordList.content)
                }
            }.exhaustive
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

            queryResultObserver?.let {
                viewModel.getQueryResult().observe(viewLifecycleOwner, it)
            }

            setUpSpinner(this)
            val button = findViewById<Button>(R.id.query_screen_right_button)
            val adapter = SimpleListAdapter<ListTextItem>(
                context
            ) {
                viewModel.onQueryModeChanged(it)
            }
            val popupList = SimpleTextListWindowBuilder(
                requireContext(),
                adapter
            )
                .withAnchor(findViewById<TextInputEditText>(R.id.query_screen_input))
                .build()

            viewModel.getQueryModes().observe(viewLifecycleOwner, Observer {
                adapter.submitListUpdate(it)
                popupList.dismiss()
            })


            button.setOnClickListener {
                popupList.show()
            }

        }
    }

    private fun setUpSpinner(view: View) {
        view.findViewById<Spinner>(R.id.query_screen_spinner).apply {
            adapter = DictionarySelectorSpinnerAdapter(
                requireContext(),
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
            viewModel.getRegisteredDictionaries().observe(viewLifecycleOwner, Observer {
                when (it) {
                    LoadableContent.UnInitialized,
                    LoadableContent.Loading -> Unit
                    is LoadableContent.Completed -> {
                        (this.adapter as DictionarySelectorSpinnerAdapter).updateList(it.content)
                    }
                }.exhaustive
            })
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

