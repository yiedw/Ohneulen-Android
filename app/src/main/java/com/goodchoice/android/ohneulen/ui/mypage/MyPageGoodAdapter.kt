package com.goodchoice.android.ohneulen.ui.mypage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.data.model.Store
import com.goodchoice.android.ohneulen.databinding.StoreItemBinding
import com.goodchoice.android.ohneulen.ui.store.StoreAppBar
import com.goodchoice.android.ohneulen.ui.store.StoreFragment
import com.goodchoice.android.ohneulen.util.addAppbarFragment
import com.goodchoice.android.ohneulen.util.addMainFragment

class MyPageGoodAdapter :
    RecyclerView.Adapter<MyPageGoodAdapter.MyPageGoodViewHolder>() {
    var itemList = listOf<Store>()

    inner class MyPageGoodViewHolder(private val binding: StoreItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Store) {
            binding.apply {
                store = item
                executePendingBindings()
                root.setOnClickListener {
                    addAppbarFragment(StoreAppBar.newInstance(), true)
                    addMainFragment(StoreFragment.newInstance(), true)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyPageGoodViewHolder {
        return DataBindingUtil.inflate<StoreItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.store_item,
            parent,
            false
        ).let {
            MyPageGoodViewHolder(it)
        }
    }

    override fun getItemCount() = itemList.size

    override fun onBindViewHolder(holder: MyPageGoodViewHolder, position: Int) {
        holder.bind(itemList[position])
    }
}