//package com.goodchoice.android.ohneulen.ui.mypage
//
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import androidx.databinding.DataBindingUtil
//import androidx.recyclerview.widget.DiffUtil
//import androidx.recyclerview.widget.ListAdapter
//import androidx.recyclerview.widget.RecyclerView
//import com.goodchoice.android.ohneulen.R
//import com.goodchoice.android.ohneulen.data.model.Store
//import com.goodchoice.android.ohneulen.databinding.StoreItemBinding
//import com.goodchoice.android.ohneulen.ui.MainActivity
//import com.goodchoice.android.ohneulen.ui.store.StoreAppBar
//import com.goodchoice.android.ohneulen.ui.store.StoreFragment
//import com.goodchoice.android.ohneulen.util.addAppbarFragment
//import com.goodchoice.android.ohneulen.util.addMainFragment
//
//class MyPageGoodAdapter :
//    ListAdapter<Store,MyPageGoodAdapter.MyPageGoodViewHolder>(MyPageGoodDiffUtil) {
//
//    inner class MyPageGoodViewHolder(private val binding: StoreItemBinding) :
//        RecyclerView.ViewHolder(binding.root) {
//        fun bind(item: Store) {
//            binding.apply {
//                store = item
//                root.setOnClickListener {
//                    addAppbarFragment(StoreAppBar.newInstance(MainActivity.), true)
//                    addMainFragment(StoreFragment.newInstance(), true)
//                }
//            }
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyPageGoodViewHolder {
//        return DataBindingUtil.inflate<StoreItemBinding>(
//            LayoutInflater.from(parent.context),
//            R.layout.search_store_item,
//            parent,
//            false
//        ).let {
//            MyPageGoodViewHolder(it)
//        }
//    }
//
//    override fun getItemCount() = super.getItemCount()
//
//    override fun onBindViewHolder(holder: MyPageGoodViewHolder, position: Int) {
//        holder.bind(getItem(position))
//    }
//}
//object MyPageGoodDiffUtil:DiffUtil.ItemCallback<Store>(){
//    override fun areItemsTheSame(oldItem: Store, newItem: Store): Boolean {
//        return oldItem.seq==newItem.seq
//    }
//
//    override fun areContentsTheSame(oldItem: Store, newItem: Store): Boolean {
//        return oldItem==newItem
//    }
//
//}