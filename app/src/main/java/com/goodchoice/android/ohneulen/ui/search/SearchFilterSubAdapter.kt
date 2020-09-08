package com.goodchoice.android.ohneulen.ui.search

import android.graphics.Typeface
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
import com.goodchoice.android.ohneulen.data.model.OhneulenData
import com.goodchoice.android.ohneulen.data.repository.InitData
import com.goodchoice.android.ohneulen.databinding.SearchFilterItemBinding

class SearchFilterSubAdapter() :
    ListAdapter<OhneulenData, SearchFilterSubAdapter.SearchFilterViewHolder>(SearchFilterSubDiffUtil) {

    lateinit var searchViewModel: SearchViewModel
    lateinit var initData:InitData

    inner class SearchFilterViewHolder(private val binding: SearchFilterItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun subFilterBind(items: OhneulenData) {
            binding.apply {

                if (items.check) {
                    filterCheck.visibility = View.VISIBLE
                    binding.filterCategory.setTypeface(null,Typeface.BOLD)
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
                        initData.subCategoryList[searchViewModel.mainCategoryPosition.value!!][searchViewModel.subCategoryPosition]

                    if (!subCategory.check) {
                        if (searchViewModel.tempCateOhneulenData.size > 4) {
                            Toast.makeText(root.context, "최대 5개까지 선택 가능합니다.", Toast.LENGTH_SHORT)
                                .show()
                            return@setOnClickListener
                        }
                    }


                    subCategory.check = !subCategory.check
                    initData.subCategory.postValue(initData.subCategoryList[searchViewModel.mainCategoryPosition.value!!])
                    val code = "${items.majorCode}${items.minorCode}"
                    if (subCategory.check) {
                        //아이템이 없을때 추가
                        searchViewModel.tempCateOhneulenData.add(items)
                        searchViewModel.cate.add(code)
                    } else {
                        //아이템이 있으면 삭제
                        searchViewModel.tempCateOhneulenData.remove(items)
                        searchViewModel.cate.remove(code)
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

object SearchFilterSubDiffUtil : DiffUtil.ItemCallback<OhneulenData>() {
    override fun areItemsTheSame(oldItem: OhneulenData, newItem: OhneulenData): Boolean {
        return oldItem.minorName == newItem.minorName
    }

    override fun areContentsTheSame(oldItem: OhneulenData, newItem: OhneulenData): Boolean {
        return oldItem == newItem
    }

}

