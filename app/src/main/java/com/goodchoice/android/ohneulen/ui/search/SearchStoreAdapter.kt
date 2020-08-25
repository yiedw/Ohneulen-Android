package com.goodchoice.android.ohneulen.ui.search

import android.annotation.SuppressLint
import android.app.Dialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.data.model.Store
import com.goodchoice.android.ohneulen.databinding.StoreItemBinding
import com.goodchoice.android.ohneulen.ui.MainActivity
import com.goodchoice.android.ohneulen.ui.login.Login
import com.goodchoice.android.ohneulen.ui.login.LoginAppBar
import com.goodchoice.android.ohneulen.ui.login.LoginViewModel
import com.goodchoice.android.ohneulen.ui.store.StoreAppBar
import com.goodchoice.android.ohneulen.ui.store.StoreFragment
import com.goodchoice.android.ohneulen.util.*
import com.goodchoice.android.ohneulen.util.constant.BaseUrl
import timber.log.Timber


class SearchStoreAdapter :
    ListAdapter<Store, SearchStoreAdapter.SearchStoreViewHolder>(SearchStoreDiffUtil) {

    var first = false
    lateinit var parentView: RecyclerView

    inner class SearchStoreViewHolder(private val binding: StoreItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: Store) {
            binding.apply {
//                if (!first) {
//                    first = true
//                    MainActivity.supportFragmentManager.beginTransaction().addToBackStack(null)
//                        .add(R.id.main_frameLayout,StoreFragment.newInstance())
//                }
                store = item
                storeItemLike.isSelected = item.likes
                storeItemLike.setOnClickListener {
                    if (!LoginViewModel.isLogin.value!!) {
                        loginDialog(root.context, SearchAppBar.newInstance())
                        return@setOnClickListener
                    }
                    storeItemLike.isSelected = !storeItemLike.isSelected
                    if (storeItemLike.isSelected) {
//                        storeItemLike.isSelected = false
                        Toast.makeText(root.context, "찜 목록에 저장되었습니다", Toast.LENGTH_SHORT).show()
                    }

                }

                root.setOnClickListener {
                    parentView.isEnabled = false
                    Timber.e(System.nanoTime().toString())
//                    root.isEnabled=false
                    StoreFragment.storeSeq = item.seq
//                    replaceAppbarFragment(StoreAppBar.newInstance())
                    addMainFragment(StoreFragment.newInstance(), true)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchStoreViewHolder {
        return DataBindingUtil.inflate<StoreItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.store_item,
            parent,
            false
        ).let {
            SearchStoreViewHolder(it)
        }
    }

    override fun getItemCount() = super.getItemCount()

    override fun onBindViewHolder(holder: SearchStoreViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}

object SearchStoreDiffUtil : DiffUtil.ItemCallback<Store>() {
    override fun areItemsTheSame(oldItem: Store, newItem: Store): Boolean {
        return oldItem.seq == newItem.seq
    }

    override fun areContentsTheSame(oldItem: Store, newItem: Store): Boolean {
        return oldItem == newItem
    }

}