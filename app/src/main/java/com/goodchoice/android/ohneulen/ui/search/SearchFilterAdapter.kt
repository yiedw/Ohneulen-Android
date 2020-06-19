package com.goodchoice.android.ohneulen.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.SearchFilterItemBinding
import com.goodchoice.android.ohneulen.util.ConstList
import com.goodchoice.android.ohneulen.util.subDataRefresh
import timber.log.Timber

class SearchFilterAdapter(private val categoryKind: Int) :
    RecyclerView.Adapter<SearchFilterAdapter.SearchFilterViewHolder>() {

    var itemList = listOf<String>()

    inner class SearchFilterViewHolder(private val binding: SearchFilterItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String) {
            binding.apply {
                binding.category.text = item
                binding.root.setOnClickListener {
                    if (categoryKind==ConstList.MAIN_CATEGORY) {
                        subDataRefresh(adapterPosition)
                        Timber.e(SearchViewModel.subCategory.value.toString())
                    }
                    else{
                        Timber.e(item)
                    }
                }
                executePendingBindings()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DataBindingUtil.inflate<SearchFilterItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.search_filter_item,
            parent,
            false
        ).let {
            SearchFilterViewHolder(it)
        }

    override fun getItemCount() = itemList.size

    override fun onBindViewHolder(holder: SearchFilterViewHolder, position: Int) {

        holder.bind(itemList[position])
    }
}