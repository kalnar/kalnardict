package eu.kalnarapps.kalnardict.androidui.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import eu.kalnarapps.kalnardict.presentation.interactors.errorhandlers.UiLogger
import eu.kalnarapps.kalnardict.presentation.models.errors.ErrorUiFeedBack
import eu.kalnarapps.kalnardict.presentation.models.navigation.NavigationCommand
import eu.kalnarapps.kalnardict.presentation.interactors.navigation.ScreenNavigator
import eu.kalnarapps.kalnardict.common.extentions.exhaustive
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

abstract class BaseFragment<UiModel> : Fragment() {

    protected abstract val viewModel: BaseViewModel<UiModel>
    private val navigator: ScreenNavigator by inject { parametersOf(findNavController()) }
    protected val uiLogger: UiLogger by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        uiLogger.d(FRAGMENT_CYCLE, "onViewCreated")
        listenToNavigationCommands()
        listenToUiStateChanges()
        listenToErrors()
    }

    private fun listenToErrors() {
        viewModel.error.observe(viewLifecycleOwner, Observer {
            uiLogger.logErrorFromUi(it)
            when (val feedBack = it.errorFeedback) {
                is ErrorUiFeedBack.ShowSnackBar -> {
                    // TODO(need to show snackbar)
                }
                ErrorUiFeedBack.OnlyLog -> Unit
                is ErrorUiFeedBack.ShowToast -> showToast(feedBack.msg)
                is ErrorUiFeedBack.Navigate -> navigator.execute(feedBack.navCommand)
            }.exhaustive
        })
    }

    private fun showToast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

    private fun listenToUiStateChanges() {
        viewModel.getUiState().observe(viewLifecycleOwner, Observer {
            uiLogger.d("ui.state", it.toString())
        })
    }

    private fun listenToNavigationCommands() {
        viewModel.navigationCommand.observe(viewLifecycleOwner, Observer { navCommand ->
            if (navCommand != NavigationCommand.Common.DoNothing) {
                viewModel.resetNavigation()
                navigator.execute(navCommand)
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        uiLogger.d(FRAGMENT_CYCLE, "onCreate")
    }

    override fun onResume() {
        super.onResume()
        uiLogger.d(FRAGMENT_CYCLE, "onResume")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        uiLogger.d(FRAGMENT_CYCLE, "onCreateView")
        return super.onCreateView(inflater, container, savedInstanceState)
    }


    companion object {
        const val FRAGMENT_CYCLE = "fragment_cycle"
    }
}
