package eu.kalnarapps.kalnardict.androidui.dictionaryquery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.distinctUntilChanged
import androidx.navigation.navGraphViewModels
import eu.kalnarapps.kalnardict.androidui.R
import eu.kalnarapps.kalnardict.androidui.common.BaseFragment
import eu.kalnarapps.kalnardict.androidui.common.model.LoadableContent
import eu.kalnarapps.kalnardict.androidui.dictionaryquery.model.DictionaryQueryState
import eu.kalnarapps.kalnardict.androidui.dictionaryquery.view.translation.TranslationViewLoaderProvider
import eu.kalnarapps.kalnardict.common.extentions.exhaustive
import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.models.translations.RenderingStrategy
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.ext.android.inject

@ExperimentalCoroutinesApi
class DictionaryTranslationFragment : BaseFragment<DictionaryQueryState>() {

    override val viewModel: DictionaryQueryViewModel by navGraphViewModels(
        R.id.dictionary_query_navigation
    ) { DictionaryQueryViewModelFactory() }
    private val translationViewLoaderProvider: TranslationViewLoaderProvider by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dictionary_translation_fragment, container, false)
    }

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getTranslation().distinctUntilChanged().observe(viewLifecycleOwner, Observer {
            when (it) {
                LoadableContent.UnInitialized -> {

                }
                is LoadableContent.Loading -> {

                }
                is LoadableContent.Completed -> {
                    when (it.content) {
                        is DataOperationResult.Success -> {
                            if (!it.content.data.hasBeenHandled()) {
                                viewModel.getUiState().value
                                    ?.currentDictionaryItemView?.renderingStrategy?.let { strategy ->
                                        loadTranslationStub(
                                            strategy,
                                            view,
                                            it.content.data.content()
                                        )
                                    }
                            } else Unit
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

    private fun loadTranslationStub(
        strategy: RenderingStrategy,
        view: View,
        content: String
    ) {

        val stub = view.findViewById<ViewStub>(R.id.translation_view_stub)
        if (stub != null) {
            translationViewLoaderProvider.provide(
                strategy
            )
                .loadTranslationInto(stub)
                .loadContent(content)
        }
    }
}