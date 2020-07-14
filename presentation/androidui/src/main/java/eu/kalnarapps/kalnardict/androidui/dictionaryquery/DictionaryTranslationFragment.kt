package eu.kalnarapps.kalnardict.androidui.dictionaryquery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.navGraphViewModels
import eu.kalnarapps.kalnardict.androidui.R
import eu.kalnarapps.kalnardict.androidui.common.BaseFragment
import eu.kalnarapps.kalnardict.androidui.common.model.LoadableContent
import eu.kalnarapps.kalnardict.androidui.dictionaryquery.model.DictionaryQueryState
import eu.kalnarapps.kalnardict.common.extentions.exhaustive
import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import kotlinx.android.synthetic.main.dictionary_translation_fragment.translation_view

class DictionaryTranslationFragment : BaseFragment<DictionaryQueryState>() {

    override val viewModel: DictionaryQueryViewModel by navGraphViewModels(
        R.id.dictionary_query_navigation
    ) { DictionaryQueryViewModelFactory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dictionary_translation_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getTranslation().observe(viewLifecycleOwner, Observer {
            when (it) {
                LoadableContent.UnInitialized -> {

                }
                is LoadableContent.Loading -> {

                }
                is LoadableContent.Completed -> {
                    when (it.content) {
                        is DataOperationResult.Success -> {
                            translation_view.text = it.content.data
                        }
                        is DataOperationResult.Failure -> {
                            Toast.makeText(
                                requireContext(),
                                it.content.errorMessage,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }.exhaustive
        })
    }
}