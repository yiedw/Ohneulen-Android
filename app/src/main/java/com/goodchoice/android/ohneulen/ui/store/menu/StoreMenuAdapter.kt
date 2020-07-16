package com.goodchoice.android.ohneulen.ui.store.menu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.StoreMenuItemBinding
import com.goodchoice.android.ohneulen.data.model.StoreMenu
import com.goodchoice.android.ohneulen.ui.MainActivity
import com.goodchoice.android.ohneulen.util.addMainFragment

class StoreMenuAdapter :
    ListAdapter<StoreMenu, StoreMenuAdapter.StoreMenuViewHolder>(StoreMenuDiffUtil) {

    override fun getItemCount() = super.getItemCount()

    inner class StoreMenuViewHolder(private val binding: StoreMenuItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: StoreMenu) {
            binding.apply {
                storeMenu = item
                storeMenuItem.setOnClickListener {
                    MainActivity.appbarFrameLayout.visibility = View.INVISIBLE
                    addMainFragment(
                        StoreMenuDetail.newInstance(
                            adapterPosition
                        ), true
                    )
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DataBindingUtil.inflate<StoreMenuItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.store_menu_item,
            parent,
            false
        ).let {
            StoreMenuViewHolder(it)
        }

    override fun onBindViewHolder(holder: StoreMenuViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

object StoreMenuDiffUtil : DiffUtil.ItemCallback<StoreMenu>() {
    override fun areItemsTheSame(oldItem: StoreMenu, newItem: StoreMenu): Boolean {
        return oldItem.seq == newItem.seq
    }

    override fun areContentsTheSame(oldItem: StoreMenu, newItem: StoreMenu): Boolean {
        return oldItem == newItem
    }

}

