package eu.kalnarapps.kalnardict.androidui.navigationscreen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import eu.kalnarapps.kalnardict.androidui.navigationscreen.model.NavigationItemView

class NavigationScreensListAdapter(
    private val list: List<NavigationItemView>,
    private val onClickAction: OnScreenItemViewClickListener
) : RecyclerView.Adapter<NavigationScreenViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NavigationScreenViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return NavigationScreenViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: NavigationScreenViewHolder, position: Int) {
        val movie: NavigationItemView = list[position]
        holder.bind(movie, onClickAction)
    }

    override fun getItemCount(): Int = list.size

}

interface OnScreenItemViewClickListener {
    fun onClick(screenItemView: NavigationItemView)
}
