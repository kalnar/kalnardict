package eu.kalnarapps.kalnardict.androidui.navigationscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import eu.kalnarapps.kalnardict.androidui.databinding.NavigationScreenFragmentBinding
import eu.kalnarapps.kalnardict.androidui.navigationscreen.model.NavigationItemView
import eu.kalnarapps.kalnardict.presentation.interactors.navigation.ScreenNavigator
import eu.kalnarapps.kalnardict.presentation.models.navigation.NavigationCommand
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class NavigationScreen : Fragment() {

    private val navigator: ScreenNavigator by inject { parametersOf(findNavController()) }
    private val viewModel: NavigationViewModel by viewModel()

    private var _binding: NavigationScreenFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = NavigationScreenFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navigationCommandList = viewModel.createNavCommandList()

        val adapter = NavigationScreensListAdapter(
            navigationCommandList,
            onClickAction = object : OnScreenItemViewClickListener {
                override fun onClick(screenItemView: NavigationItemView) {
                    viewModel.onScreenItemClicked(screenItemView)
                }
            }
        )
        binding.list.apply {
            layoutManager = LinearLayoutManager(context)
            this.adapter = adapter
        }

        viewModel.navigationCommand.observe(viewLifecycleOwner, Observer { navCommand ->
            if (navCommand != NavigationCommand.Common.DoNothing) {
                navigator.execute(navCommand)
                viewModel.resetNavigation()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
