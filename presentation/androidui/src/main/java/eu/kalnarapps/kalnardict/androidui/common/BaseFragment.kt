package eu.kalnarapps.kalnardict.androidui.common

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import eu.kalnarapps.kalnardict.android.utils.Logger
import eu.kalnarapps.kalnardict.androidui.navigation.NavigationCommand
import eu.kalnarapps.kalnardict.androidui.navigation.ScreenNavigator
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

abstract class BaseFragment<UiModel> : Fragment() {

    protected abstract val viewModel: BaseViewModel<UiModel>
    private val navigator: ScreenNavigator by inject { parametersOf(findNavController()) }
    protected val logger: Logger by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listenToNavigationCommands()
        listenToUiStateChanges()
        listenToErrors()
    }

    private fun listenToErrors() {
        viewModel.error.observe(viewLifecycleOwner, Observer {
            logger.logErrorFromUi(it)
        })
    }

    private fun listenToUiStateChanges() {
        viewModel.getUiState().observe(viewLifecycleOwner, Observer {
            logger.d("ui.state", it.toString())
        })
    }

    private fun listenToNavigationCommands() {
        viewModel.navigationCommand.observe(viewLifecycleOwner, Observer { navCommand ->
            if (navCommand != NavigationCommand.DoNothing) {
                navigator.execute(navCommand)
                viewModel.resetNavigation()
            }
        })
    }
}
