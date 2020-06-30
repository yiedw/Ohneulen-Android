package com.goodchoice.android.ohneulen.ui.store

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.data.model.Photo
import com.goodchoice.android.ohneulen.data.model.StoreMenu
import com.goodchoice.android.ohneulen.databinding.StoreMenuDetailItemBinding
import com.goodchoice.android.ohneulen.ui.MainActivity

class StoreMenuDetailAdapter() :
    RecyclerView.Adapter<StoreMenuDetailAdapter.StoreMenuDetailViewHolder>() {

    lateinit var photoList: MutableList<Photo>
    var menuList = listOf<StoreMenu>()

    inner class StoreMenuDetailViewHolder(
        private val binding: StoreMenuDetailItemBinding,
        val context: Context
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(menuItem: StoreMenu) {
            binding.apply {
                storeMenuDetailBack.setOnClickListener {
                    MainActivity.supportFragmentManager.popBackStack()
                    MainActivity.appbarFrameLayout.visibility=View.VISIBLE
                }
//                photo = photoItem
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
            StoreMenuDetailViewHolder(it, parent.context)
        }

    override fun getItemCount() = menuList.size

    override fun onBindViewHolder(holder: StoreMenuDetailViewHolder, position: Int) {
        holder.bind(menuList[position])
    }
}