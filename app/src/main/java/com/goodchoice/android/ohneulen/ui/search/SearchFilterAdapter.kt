package com.goodchoice.android.ohneulen.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.data.model.OhneulenData
import com.goodchoice.android.ohneulen.databinding.SearchFilterItemBinding
import com.goodchoice.android.ohneulen.util.ConstList

class SearchFilterAdapter(private val categoryKind: Int) :
    RecyclerView.Adapter<SearchFilterAdapter.SearchFilterViewHolder>() {

    lateinit var searchViewModel: SearchViewModel
    var itemList = mutableListOf<OhneulenData>()

    inner class SearchFilterViewHolder(private val binding: SearchFilterItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String) {
            binding.apply {
                binding.filterCategory.text = item
                binding.root.setOnClickListener {
                    if (categoryKind == ConstList.MAIN_CATEGORY) {
//                        App.categorySwitch.postValue(adapterPosition)
                        searchViewModel.mainCategoryPosition.postValue(adapterPosition)
                    } else {
                        if (binding.filterCheck.visibility == View.VISIBLE) {
                            binding.filterCheck.visibility = View.GONE
                        } else {
                            binding.filterCheck.visibility = View.VISIBLE
                        }
                        searchViewModel.subCategoryPosition.postValue(adapterPosition)
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

    override fun getItemCount() =
        itemList.size


    override fun onBindViewHolder(holder: SearchFilterViewHolder, position: Int) {
        holder.bind(itemList[position].minorName)
    }
}