package com.goodchoice.android.ohneulen.ui.store.menu

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.data.model.Photo
import com.goodchoice.android.ohneulen.data.model.StoreMenu
import com.goodchoice.android.ohneulen.databinding.StoreMenuDetailItemBinding
import com.goodchoice.android.ohneulen.ui.MainActivity
import timber.log.Timber

class StoreMenuDetailAdapter() :
    RecyclerView.Adapter<StoreMenuDetailAdapter.StoreMenuDetailViewHolder>() {

    var menuList = listOf<StoreMenu>()
    var menuPosition=MutableLiveData<Int>(0)

    inner class StoreMenuDetailViewHolder(
        private val binding: StoreMenuDetailItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(menuItem: StoreMenu) {
            binding.apply {
                menu = menuItem
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DataBindingUtil.inflate<StoreMenuDetailItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.store_menu_detail_item,
            parent,
            false
        ).let {
            StoreMenuDetailViewHolder(it)
        }

    override fun getItemCount() = menuList.size

    override fun onBindViewHolder(holder: StoreMenuDetailViewHolder, position: Int) {
        holder.bind(menuList[position])
    }
}