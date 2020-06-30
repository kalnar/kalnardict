package eu.kalnarapps.kalnardict.androidui.common

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import eu.kalnarapps.kalnardict.android.utils.Logger
import eu.kalnarapps.kalnardict.androidui.navigation.NavigationCommand
import eu.kalnarapps.kalnardict.androidui.navigation.ScreenNavigator
import org.koin.android.ext.android.inject

abstract class BaseFragment<UiModel> : Fragment() {

    protected abstract val viewModel: BaseViewModel<UiModel>
    private val navigator: ScreenNavigator by inject()
    protected val logger: Logger by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listenToNavigationCommands()
        listenToUiStateChanges()
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
