package eu.kalnarapps.kalnardict.androidui.navigationscreen

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
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
    private val cardView: MaterialCardView = itemView.findViewById(R.id.screen_item_card_view)

    fun bind(
        navigationScreenItemView: NavigationItemView,
        onClickAction: OnScreenItemViewClickListener
    ) {
        titleView.text = navigationScreenItemView.name
        cardView.setOnClickListener { onClickAction.onClick(navigationScreenItemView) }
    }
}