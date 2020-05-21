package eu.kalnarapps.kalnardict.androidui.dictionaryquery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import eu.kalnarapps.kalnardict.androidui.R
import eu.kalnarapps.kalnardict.androidui.dictionaryquery.listview.QueryResultListAdapter
import kotlinx.android.synthetic.main.dictionary_query_fragment.*
import org.koin.androidx.viewmodel.ext.android.getKoin


class DictionaryQueryFragment : Fragment() {

    private val queryViewModel: DictionaryQueryViewModel = getKoin().get()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dictionary_query_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        query_result_list_view.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = QueryResultListAdapter(
                queryViewModel.getQueryResult()
            )
        }
        query_screen_spinner.apply {
//            adapter = DictionaryDropDownAdapter()
            adapter = ArrayAdapter<DictionarySelectorItem>(
                context,
                android.R.layout.simple_spinner_item,
                queryViewModel.getRegisteredDictionaries()
            )

        }
    }
}

