package eu.kalnarapps.kalnardict.androidui.navigationscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import eu.kalnarapps.kalnardict.androidui.R
import eu.kalnarapps.kalnardict.androidui.navigation.NavigationCommand
import eu.kalnarapps.kalnardict.androidui.navigation.ScreenNavigator
import eu.kalnarapps.kalnardict.androidui.stub.UiStubs
import kotlinx.android.synthetic.mock.navigation_screen_fragment.list
import org.koin.android.ext.android.inject

class NavigationScreen : Fragment() {

    private val navigator: ScreenNavigator by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.navigation_screen_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navigationCommandList = createNavCommandList()

        val adapter = NavigationScreensListAdapter(
            navigationCommandList,
            onClickAction = object : OnScreenItemViewClickListener {
                override fun onClick(screenItemView: NavigationItemView) {
                    navigator.execute(screenItemView.navCommand)
                }
            }
        )
        list.apply {
            layoutManager = LinearLayoutManager(context)
            this.adapter = adapter
        }
    }

    private fun createNavCommandList(): List<NavigationItemView> {
        return listOf(
            NavigationItemView(
                name = "DictionaryQuery",
                navCommand = NavigationCommand.NavigateToDictionaryQuery
            ),
            NavigationItemView(
                name = "DictionaryManager",
                navCommand = NavigationCommand.NavigateToDictionaryManager
            ),
            NavigationItemView(
                name = "DictionaryRegistry - valid db",
                navCommand = NavigationCommand.NavigateToDictionaryRegistry(
                    uri = UiStubs.Uris.validUri
                )
            ),
            NavigationItemView(
                name = "DictionaryRegistry - invalid db",
                navCommand = NavigationCommand.NavigateToDictionaryRegistry(
                    uri = UiStubs.Uris.invalidUri
                )
            ),
            NavigationItemView(
                name = "dialog - table registration - ok",
                navCommand = NavigationCommand.ShowDialog.SuccessTableRegistration(
                    table = "original mock table",
                    dictionaryName = "saving name for mock table"
                )
            ),
            NavigationItemView(
                name = "dialog - table registration - fail",
                navCommand = NavigationCommand.ShowDialog.FailureTableRegistration(
                    table = "original mock table",
                    errorMessage = "import failed due to conflicting names"
                )
            )
        )
    }

}

data class NavigationItemView(
    val name: String,
    val navCommand: NavigationCommand
)

data class NavigationItemWithArgumentView(
    val navigationItemView: NavigationItemView,
    val argument: String
)
