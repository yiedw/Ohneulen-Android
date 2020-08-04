//package com.goodchoice.android.ohneulen.ui.search
//
//import android.graphics.Color
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.core.content.ContextCompat
//import androidx.databinding.DataBindingUtil
//import androidx.recyclerview.widget.DiffUtil
//import androidx.recyclerview.widget.RecyclerView
//import com.goodchoice.android.ohneulen.R
//import com.goodchoice.android.ohneulen.data.model.Category
//import com.goodchoice.android.ohneulen.databinding.SearchFilterItemBinding
//import com.goodchoice.android.ohneulen.util.constant.ConstList
//import timber.log.Timber
//
//class SearchFilterAdapter(private val categoryKind: Int) :
//    RecyclerView.Adapter<SearchFilterAdapter.SearchFilterViewHolder>() {
//
//    lateinit var searchViewModel: SearchViewModel
//    var itemList = mutableListOf<Category>()
//    var previousPosition = 0
//    var selectPosition = 0
//
//    inner class SearchFilterViewHolder(private val binding: SearchFilterItemBinding) :
//        RecyclerView.ViewHolder(binding.root) {
//        fun mainFilterBind(item: String) {
//            binding.apply {
//                if (adapterPosition == selectPosition) {
//                    filterCategory.setBackgroundColor(
//                        ContextCompat.getColor(
//                            root.context,
//                            R.color.colorOhneulen
//                        )
//                    )
//                    filterCategory.setTextColor(ContextCompat.getColor(root.context, R.color.white))
//                } else {
//                    binding.apply {
//                        filterCategory.setBackgroundColor(
//                            Color.parseColor("#f6f6f6")
//                        )
//                        filterCategory.setTextColor(
//                            ContextCompat.getColor(
//                                root.context,
//                                R.color.colorGrey88
//                            )
//                        )
//                    }
//                }
//                filterCategory.text = item
//                root.setOnClickListener {
//                    filterCategory.setBackgroundColor(
//                        ContextCompat.getColor(
//                            root.context,
//                            R.color.colorOhneulen
//                        )
//                    )
//                    filterCategory.setTextColor(ContextCompat.getColor(root.context, R.color.white))
//                    searchViewModel.mainCategoryPosition.postValue(adapterPosition)
//                    searchViewModel.subCategoryPosition = 0
//                    notifyItemChanged(previousPosition)
//                    previousPosition = adapterPosition
//                    selectPosition = adapterPosition
//                }
//            }
//        }
//
//        fun subFilterBind(item: String, check: Boolean) {
//            binding.apply {
//                filterCategory.text = item
//                filterCategory.setTextColor(
//                    ContextCompat.getColor(
//                        root.context,
//                        R.color.colorBlack
//                    )
//                )
//                if (check) {
//                    filterCheck.visibility = View.VISIBLE
//                } else {
//                    filterCheck.visibility = View.GONE
//
//                }
//                root.setOnClickListener {
//                    searchViewModel.subCategoryPosition = adapterPosition
//                    val tempCategoryList = searchViewModel.categoryList.value
//                    tempCategoryList!![searchViewModel.mainCategoryPosition.value!!].subCategoryList[searchViewModel.subCategoryPosition].check =
//                        !check
//                    searchViewModel.categoryList.postValue(tempCategoryList)
//                }
//            }
//        }
//
////        fun mainFilterSelect() {
////            binding.apply {
////                filterCategory.setBackgroundColor(
////                    Color.parseColor("#f6f6f6")
////                )
////                filterCategory.setTextColor(
////                    ContextCompat.getColor(
////                        root.context,
////                        R.color.colorGrey88
////                    )
////                )
////            }
////        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
//        DataBindingUtil.inflate<SearchFilterItemBinding>(
//            LayoutInflater.from(parent.context),
//            R.layout.search_filter_item,
//            parent,
//            false
//        ).let {
//            SearchFilterViewHolder(it)
//        }
//
//    override fun getItemCount() =
//        itemList.size
//
//
//    override fun onBindViewHolder(holder: SearchFilterViewHolder, position: Int) {
//        if (categoryKind == ConstList.MAIN_CATEGORY) {
//            holder.mainFilterBind(itemList[position].minorName)
//        } else
//            holder.subFilterBind(itemList[position].minorName, itemList[position].check)
//    }
//}
//
