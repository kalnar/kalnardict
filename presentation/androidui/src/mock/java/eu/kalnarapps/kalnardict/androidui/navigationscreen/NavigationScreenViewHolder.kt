package eu.kalnarapps.kalnardict.androidui.navigationscreen

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import eu.kalnarapps.kalnardict.androidui.R

class NavigationScreenViewHolder(
    inflater: LayoutInflater, parent: ViewGroup
) : RecyclerView.ViewHolder(
    inflater.inflate(
        R.layout.navigation_screen_item_view,
        parent,
        false
    )
) {
    private val titleView: TextView = itemView.findViewById(R.id.screen_item_title)

    fun bind(
        navigationScreenItemView: NavigationItemView,
        onClickAction: OnScreenItemViewClickListener
    ) {
        titleView.text = navigationScreenItemView.name
        titleView.setOnClickListener { onClickAction.onClick(navigationScreenItemView) }
    }
}