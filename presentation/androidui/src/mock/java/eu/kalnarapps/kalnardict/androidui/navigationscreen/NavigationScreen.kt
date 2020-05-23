package eu.kalnarapps.kalnardict.androidui.navigationscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import eu.kalnarapps.kalnardict.androidui.R
import kotlinx.android.synthetic.mock.navigation_screen_fragment.list

class NavigationScreen : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.navigation_screen_fragment, container, false).apply {
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val listOfScreens = ArrayList<NavigationItemView>()
        findNavController().graph.iterator().forEach {
            if (it.label != null) {
                listOfScreens.add(
                    NavigationItemView(
                        name = it.label.toString(),
                        id = it.id
                    )
                )
            }
        }
        val adapter = NavigationScreensListAdapter(
            listOfScreens,
            onClickAction = object : OnScreenItemViewClickListener {
                override fun onClick(screenItemView: NavigationItemView) {
                    findNavController().navigate(screenItemView.id)
                }
            }
        )
        list.apply {
            layoutManager = LinearLayoutManager(context)
            this.adapter = adapter
        }
    }
}

data class NavigationItemView(
    val name: String,
    val id: Int
)

