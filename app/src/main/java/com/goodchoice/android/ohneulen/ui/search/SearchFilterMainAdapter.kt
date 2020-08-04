package com.goodchoice.android.ohneulen.ui.search

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

class SearchFilterMainAdapter() :
    ListAdapter<Category, SearchFilterMainAdapter.SearchFilterViewHolder>(SearchFilterMainDiffUtil) {

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

                root.setOnClickListener {
                    if (previousPosition != adapterPosition) {

                        filterCategory.setBackgroundColor(
                            ContextCompat.getColor(
                                root.context,
                                R.color.colorOhneulen
                            )
                        )
                        filterCategory.setTextColor(
                            ContextCompat.getColor(
                                root.context,
                                R.color.white
                            )
                        )
                        searchViewModel.mainCategoryPosition.postValue(adapterPosition)
                        previousPosition = adapterPosition
                    }
                }
            }
//
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
        holder.mainFilterBind(itemList[position].minorName)
    }
}

object SearchFilterMainDiffUtil : DiffUtil.ItemCallback<Category>() {
    override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem.minorCode == newItem.minorCode
    }

    override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem == newItem
    }

}

