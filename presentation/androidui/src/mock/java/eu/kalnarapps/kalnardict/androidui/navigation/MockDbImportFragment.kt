package eu.kalnarapps.kalnardict.androidui.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.livedata.observeAsState
import eu.kalnarapps.kalnardict.androidui.common.BaseComposeFragment
import eu.kalnarapps.kalnardict.androidui.importer.DbImporterViewModel
import eu.kalnarapps.kalnardict.presentation.models.mock.DbImporterUi
import org.koin.android.ext.android.inject

class MockDbImportFragment : BaseComposeFragment<DbImporterUi>() {

    override val viewModel: DbImporterViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(
            inflater,
            container,
            savedInstanceState
        ).also {
            binding.composeView.setContent {
                val state = viewModel.getUiState().observeAsState(DbImporterUi())
                MockDbScreen(state.value, viewModel::onFormValidation)
            }
        }
    }

}
