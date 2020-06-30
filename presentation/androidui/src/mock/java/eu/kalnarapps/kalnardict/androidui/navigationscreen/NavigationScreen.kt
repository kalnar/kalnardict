package eu.kalnarapps.kalnardict.androidui.navigationscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import eu.kalnarapps.kalnardict.androidui.R
import eu.kalnarapps.kalnardict.androidui.navigation.NavigationCommand
import eu.kalnarapps.kalnardict.androidui.navigation.ScreenNavigator
import eu.kalnarapps.kalnardict.androidui.navigationscreen.model.NavigationItemView
import kotlinx.android.synthetic.mock.navigation_screen_fragment.list
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class NavigationScreen : Fragment() {

    private val navigator: ScreenNavigator by inject()
    private val viewModel: NavigationViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.navigation_screen_fragment, container, false)
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
        list.apply {
            layoutManager = LinearLayoutManager(context)
            this.adapter = adapter
        }

        viewModel.navigationCommand.observe(viewLifecycleOwner, Observer { navCommand ->
            if (navCommand != NavigationCommand.DoNothing) {
                navigator.execute(navCommand)
                viewModel.resetNavigation()
            }
        })
    }
}
