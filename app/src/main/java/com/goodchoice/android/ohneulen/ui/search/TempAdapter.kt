//package com.goodchoice.android.ohneulen.ui.search
//
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.paging.PagedListAdapter
//import androidx.recyclerview.widget.DiffUtil
//import androidx.recyclerview.widget.RecyclerView
//import com.goodchoice.android.ohneulen.R
//import com.goodchoice.android.ohneulen.data.model.Store
//import com.goodchoice.android.ohneulen.databinding.SearchStoreItemBinding
//
//class TempAdapter : PagedListAdapter<Store, TempAdapter.TempViewHolder>(DiffUtilCallback()) {
//
//    inner class TempViewHolder(private val binding:SearchStoreItemBinding) : RecyclerView.ViewHolder(itemView) {
//        fun bind(){
//
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TempViewHolder {
//        val view =
//            LayoutInflater.from(parent.context).inflate(R.layout.search_store_item, parent, false)
//        return TempViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: TempViewHolder, position: Int) {
//    }
//
//}
//
//class DiffUtilCallback : DiffUtil.ItemCallback<Store>() {
//    override fun areItemsTheSame(oldItem: Store, newItem: Store): Boolean {
//        return oldItem.storeName == newItem.storeName
//    }
//
//    override fun areContentsTheSame(oldItem: Store, newItem: Store): Boolean {
//        return oldItem == newItem
//    }
//
//}