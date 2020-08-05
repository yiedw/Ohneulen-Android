package com.goodchoice.android.ohneulen.ui.search

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.data.model.Category
import com.goodchoice.android.ohneulen.databinding.SearchFilterItemBinding
import com.goodchoice.android.ohneulen.util.constant.ConstList
import timber.log.Timber

class SearchFilterSubAdapter() :
    ListAdapter<Category, SearchFilterSubAdapter.SearchFilterViewHolder>(SearchFilterSubDiffUtil) {

    lateinit var searchViewModel: SearchViewModel

    inner class SearchFilterViewHolder(private val binding: SearchFilterItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun subFilterBind(items: Category) {
            binding.apply {
                if (items.check) {
                    filterCheck.visibility = View.VISIBLE
                }
                filterCategory.text = items.minorName
                filterCategory.setTextColor(
                    ContextCompat.getColor(
                        root.context,
                        R.color.colorBlack
                    )
                )
                root.setOnClickListener {
                    searchViewModel.subCategoryPosition = adapterPosition
                    val subCategory =
                        searchViewModel.subCategoryList[searchViewModel.mainCategoryPosition.value!!][searchViewModel.subCategoryPosition]

                    if (!subCategory.check) {
                        if (searchViewModel.filterHashMap.size > 4) {
                            Toast.makeText(root.context, "최대 5개까지 선택 가능합니다.", Toast.LENGTH_SHORT)
                                .show()
                            return@setOnClickListener
                        }

                    }
                    subCategory.check = !subCategory.check
                    searchViewModel.subCategory.postValue(searchViewModel.subCategoryList[searchViewModel.mainCategoryPosition.value!!])
                    if (subCategory.check) {
                        searchViewModel.filterHashMap[searchViewModel.mainCategoryPosition.value!! * 10 + searchViewModel.subCategoryPosition] =
                            items.minorCode
                    } else {
                        searchViewModel.filterHashMap.remove(
                            searchViewModel.mainCategoryPosition.value!! * 10 + searchViewModel.subCategoryPosition
                        )
                    }


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

    override fun onBindViewHolder(holder: SearchFilterViewHolder, position: Int) {
        holder.subFilterBind(getItem(position))
    }
}

object SearchFilterSubDiffUtil : DiffUtil.ItemCallback<Category>() {
    override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem.minorName == newItem.minorName
    }

    override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem == newItem
    }

}

