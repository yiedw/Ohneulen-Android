package com.goodchoice.android.ohneulen.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.goodchoice.android.ohneulen.App
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.SearchFilterItemBinding
import com.goodchoice.android.ohneulen.ui.MainViewModel
import com.goodchoice.android.ohneulen.util.ConstList
import timber.log.Timber

class SearchFilterAdapter(private val categoryKind: Int) :
    RecyclerView.Adapter<SearchFilterAdapter.SearchFilterViewHolder>() {

    var itemList = listOf<String>()

    inner class SearchFilterViewHolder(private val binding: SearchFilterItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String) {
            binding.apply {
                binding.filterCategory.text = item
                binding.root.setOnClickListener {
                    if (categoryKind==ConstList.MAIN_CATEGORY) {
                        App.categorySwitch.postValue(adapterPosition)
                    }
                    else{
                        binding.filterCheck.visibility=View.VISIBLE
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