package com.goodchoice.android.ohneulen.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.data.model.Category
import com.goodchoice.android.ohneulen.data.model.OhneulenData
import com.goodchoice.android.ohneulen.databinding.SearchFilterItemBinding
import com.goodchoice.android.ohneulen.util.ConstList

class SearchFilterAdapter(private val categoryKind: Int) :
    RecyclerView.Adapter<SearchFilterAdapter.SearchFilterViewHolder>() {

    lateinit var searchViewModel: SearchViewModel
    var itemList = mutableListOf<Category>()

    inner class SearchFilterViewHolder(private val binding: SearchFilterItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun mainFilterBind(item: String) {
            binding.apply {
                binding.filterCategory.text = item
                binding.root.setOnClickListener {
                    searchViewModel.mainCategoryPosition.postValue(adapterPosition)
                    searchViewModel.subCategoryPosition = 0
                }
                executePendingBindings()
            }
        }

        fun subFilterBind(item: String, check: Boolean) {
            binding.apply {
                binding.filterCategory.text = item
                if (check)
                    binding.filterCheck.visibility = View.VISIBLE
                else
                    binding.filterCheck.visibility = View.GONE
                binding.root.setOnClickListener {
                    searchViewModel.subCategoryPosition = adapterPosition
                    val tempCategoryList = searchViewModel.categoryList.value
                    tempCategoryList!![searchViewModel.mainCategoryPosition.value!!].subCategoryList[searchViewModel.subCategoryPosition].check =
                        !check
                    searchViewModel.categoryList.postValue(tempCategoryList)
                }
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
        if (categoryKind == ConstList.MAIN_CATEGORY)
            holder.mainFilterBind(itemList[position].minorName)
        else
            holder.subFilterBind(itemList[position].minorName, itemList[position].check)
    }
}