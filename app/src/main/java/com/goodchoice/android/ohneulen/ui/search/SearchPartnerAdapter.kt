package com.goodchoice.android.ohneulen.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.SearchPartnerItemBinding
import com.goodchoice.android.ohneulen.model.Store
import com.goodchoice.android.ohneulen.ui.MainActivity
import com.goodchoice.android.ohneulen.ui.store.StoreAppBarFragment
import com.goodchoice.android.ohneulen.ui.store.StoreFragment
import com.goodchoice.android.ohneulen.util.addAppbarFragment
import com.goodchoice.android.ohneulen.util.addMainFragment


class SearchPartnerAdapter :
    RecyclerView.Adapter<SearchPartnerAdapter.SearchPartnerViewHolder>() {
    var itemList = listOf<Store>()


    inner class SearchPartnerViewHolder(private val binding: SearchPartnerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Store) {
            binding.apply {
                store = item
                executePendingBindings()
                root.setOnClickListener {
                    MainActivity.searchMapView.postValue(false)
                    addAppbarFragment(StoreAppBarFragment.newInstance(), true)
                    addMainFragment(StoreFragment.newInstance(), true)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchPartnerViewHolder {
        return DataBindingUtil.inflate<SearchPartnerItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.search_partner_item,
            parent,
            false
        ).let {
            SearchPartnerViewHolder(it)
        }
    }

    override fun getItemCount() = itemList.size

    override fun onBindViewHolder(holder: SearchPartnerViewHolder, position: Int) {
        holder.bind(itemList[position])
    }


}