package com.goodchoice.android.ohneulen.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.data.model.Category
import com.goodchoice.android.ohneulen.databinding.SearchFilterItemBinding
import com.goodchoice.android.ohneulen.util.constant.ConstList
import timber.log.Timber

class SearchFilterAdapter(private val categoryKind: Int) :
    RecyclerView.Adapter<SearchFilterAdapter.SearchFilterViewHolder>() {

    lateinit var searchViewModel: SearchViewModel
    var itemList = mutableListOf<Category>()
    var previousPosition = 0

    inner class SearchFilterViewHolder(private val binding: SearchFilterItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun mainFilterBind(item: String) {
            binding.apply {
                if (adapterPosition == 0) {
                    filterCategory.setBackgroundColor(
                        ContextCompat.getColor(
                            root.context,
                            R.color.colorOhneulen
                        )
                    )
                    filterCategory.setTextColor(ContextCompat.getColor(root.context, R.color.white))
                }
                filterCategory.text = item
                root.setOnClickListener {

                    filterCategory.setBackgroundColor(
                        ContextCompat.getColor(
                            root.context,
                            R.color.colorOhneulen
                        )
                    )
                    filterCategory.setTextColor(ContextCompat.getColor(root.context, R.color.white))
                    searchViewModel.mainCategoryPosition.postValue(adapterPosition)
                    searchViewModel.subCategoryPosition = 0

                }
                executePendingBindings()
            }
        }

        fun subFilterBind(item: String, check: Boolean) {
            binding.apply {
                filterCategory.text = item
                filterCategory.setTextColor(
                    ContextCompat.getColor(
                        root.context,
                        R.color.colorBlack
                    )
                )
                if (check) {
                    filterCheck.visibility = View.VISIBLE
                } else {
                    filterCheck.visibility = View.GONE

                }
                root.setOnClickListener {
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

