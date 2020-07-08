package com.goodchoice.android.ohneulen.ui.store.menu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.StoreMenuItemBinding
import com.goodchoice.android.ohneulen.data.model.StoreMenu
import com.goodchoice.android.ohneulen.ui.MainActivity
import com.goodchoice.android.ohneulen.util.addMainFragment

class StoreMenuAdapter : RecyclerView.Adapter<StoreMenuAdapter.StoreMenuViewHolder>() {
    var itemList = listOf<StoreMenu>()

    override fun getItemCount() = itemList.size

    inner class StoreMenuViewHolder(private val binding: StoreMenuItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: StoreMenu) {
            binding.apply {
                storeMenu = item
                storeMenuItem.setOnClickListener {
                    MainActivity.appbarFrameLayout.visibility = View.INVISIBLE
                    addMainFragment(
                        StoreMenuDetailFragment.newInstance(
                            adapterPosition
                        ), true)
                }
                executePendingBindings()
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
        holder.bind(itemList[position])
    }
}

