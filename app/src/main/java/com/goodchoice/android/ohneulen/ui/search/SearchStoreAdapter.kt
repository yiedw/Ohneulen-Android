package com.goodchoice.android.ohneulen.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.data.model.Store
import com.goodchoice.android.ohneulen.databinding.StoreItemBinding
import com.goodchoice.android.ohneulen.ui.store.StoreAppBar
import com.goodchoice.android.ohneulen.ui.store.StoreFragment
import com.goodchoice.android.ohneulen.util.addMainFragment
import com.goodchoice.android.ohneulen.util.constant.BaseUrl
import com.goodchoice.android.ohneulen.util.replaceAppbarFragment
import timber.log.Timber


class SearchStoreAdapter :
    ListAdapter<Store, SearchStoreAdapter.SearchStoreViewHolder>(SearchStoreDiffUtil) {


    inner class SearchStoreViewHolder(private val binding: StoreItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Store) {
            binding.apply {
                store = item
                storeItemGoodBasic.setOnClickListener {
                    storeItemGoodRed.visibility = View.VISIBLE
                    storeItemGoodBasic.visibility = View.GONE
                }
                storeItemGoodRed.setOnClickListener {
                    storeItemGoodRed.visibility = View.GONE
                    storeItemGoodBasic.visibility = View.VISIBLE
                }
                if (item.image.isNotEmpty()) {
                    Glide.with(root).load("${BaseUrl.Ohneulen}${item.image[0].photoURL}")
                        .centerCrop().into(storeItemImage)
                }
                else{
                    storeItemImage.setImageResource(0)
                }
                root.setOnClickListener {
                    StoreFragment.storeSeq = item.seq
                    replaceAppbarFragment(StoreAppBar.newInstance(), tag = "storeAppBar")
                    addMainFragment(StoreFragment.newInstance(), true)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchStoreViewHolder {
        return DataBindingUtil.inflate<StoreItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.store_item,
            parent,
            false
        ).let {
            SearchStoreViewHolder(it)
        }
    }

    override fun getItemCount() = super.getItemCount()

    override fun onBindViewHolder(holder: SearchStoreViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


}

object SearchStoreDiffUtil : DiffUtil.ItemCallback<Store>() {
    override fun areItemsTheSame(oldItem: Store, newItem: Store): Boolean {
        return oldItem.seq == newItem.seq
    }

    override fun areContentsTheSame(oldItem: Store, newItem: Store): Boolean {
        return oldItem == newItem
    }

}