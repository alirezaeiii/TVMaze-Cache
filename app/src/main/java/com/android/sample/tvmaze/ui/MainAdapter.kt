package com.android.sample.tvmaze.ui

import android.app.Activity
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.sample.tvmaze.databinding.ShowItemBinding
import com.android.sample.tvmaze.domain.Show
import com.android.sample.tvmaze.util.layoutInflater

class MainAdapter(
    private val activity: Activity
) : ListAdapter<Show, MainAdapter.MainViewHolder>(DiffCallback) {

    /**
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        MainViewHolder.from(parent, activity)

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the {@link ViewHolder#itemView} to reflect the item at the given
     * position.
     */
    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    /**
     * ViewHolder for category items. All work is done by data binding.
     */
    class MainViewHolder(
        private val binding: ShowItemBinding,
        private val activity: Activity
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Show) {
            binding.root.setOnClickListener {
                DetailActivity.startActivityModel(activity, binding.itemContainer, item)
            }
            with(binding) {
                show = item
                executePendingBindings()
            }
        }

        companion object {
            fun from(parent: ViewGroup, activity: Activity): MainViewHolder {
                val binding = ShowItemBinding.inflate(
                    parent.context.layoutInflater,
                    parent,
                    false
                )
                return MainViewHolder(binding, activity)
            }
        }
    }

    /**
     * Allows the RecyclerView to determine which items have changed when the [List] of [Show]
     * has been updated.
     */
    companion object DiffCallback : DiffUtil.ItemCallback<Show>() {
        override fun areItemsTheSame(oldItem: Show, newItem: Show): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Show, newItem: Show): Boolean {
            return oldItem == newItem
        }
    }
}