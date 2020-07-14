package com.goodchoice.android.ohneulen.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.data.model.Store
import com.goodchoice.android.ohneulen.databinding.StoreItemBinding
import com.goodchoice.android.ohneulen.ui.store.StoreAppBarFragment
import com.goodchoice.android.ohneulen.ui.store.StoreFragment
import com.goodchoice.android.ohneulen.util.addMainFragment
import com.goodchoice.android.ohneulen.util.replaceAppbarFragment


class SearchStoreAdapter :
    RecyclerView.Adapter<SearchStoreAdapter.SearchPartnerViewHolder>() {
    var itemList = listOf<Store>()


    inner class SearchPartnerViewHolder(private val binding: StoreItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Store) {
            binding.apply {
                store = item
                executePendingBindings()
                storeItemGood.setOnClickListener{
                    Toast.makeText(binding.root.context,item.storeName+"이 찜목록에 저장됨",Toast.LENGTH_LONG).show()
                }
                root.setOnClickListener {
//                    Timber.e(SystemClock.currentThreadTimeMillis().toString())
                    replaceAppbarFragment(StoreAppBarFragment.newInstance(), name = "storeAppBar")
                    addMainFragment(StoreFragment.newInstance(), true)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchPartnerViewHolder {
        return DataBindingUtil.inflate<StoreItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.store_item,
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