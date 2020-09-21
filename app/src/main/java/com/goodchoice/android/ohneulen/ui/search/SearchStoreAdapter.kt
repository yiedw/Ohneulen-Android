package com.goodchoice.android.ohneulen.ui.search

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.goodchoice.android.ohneulen.BuildConfig
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.data.model.SearchStore
import com.goodchoice.android.ohneulen.data.service.NetworkService
import com.goodchoice.android.ohneulen.databinding.SearchStoreItemBinding
import com.goodchoice.android.ohneulen.ui.MainActivity
import com.goodchoice.android.ohneulen.ui.dialog.LoadingDialog
import com.goodchoice.android.ohneulen.ui.login.LoginViewModel
import com.goodchoice.android.ohneulen.ui.store.StoreAppBar
import com.goodchoice.android.ohneulen.ui.store.StoreFragment
import com.goodchoice.android.ohneulen.util.*
import com.goodchoice.android.ohneulen.util.constant.BaseUrl
import com.goodchoice.android.ohneulen.util.constant.ConstList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception


class SearchStoreAdapter :
    ListAdapter<SearchStore, SearchStoreAdapter.SearchStoreViewHolder>(SearchStoreDiffUtil) {

    lateinit var mNetworkService: NetworkService

    inner class SearchStoreViewHolder(private val binding: SearchStoreItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: SearchStore) {
            binding.apply {
                searchStore = item
                if (BuildConfig.DEBUG) {
                    searchStoreItemNew.visibility = View.VISIBLE
                }

                //평점
                val point = ((item.P_1 * 10).toInt() / 10.0).toString()
                binding.searchStoreItemRating.text = point

                //리뷰 갯수
                if (item.reviewCnt >= 1000) {
                    binding.searchStoreItemReviewCnt.text = "999+"
                } else if (item.reviewCnt > 100) {
                    binding.searchStoreItemReviewCnt.text = (item.reviewCnt / 100 * 100).toString()
                } else {
                    binding.searchStoreItemReviewCnt.text = item.reviewCnt.toString()
                }

                //좋아요 갯수
                if (item.likeCnt >= 1000) {
                    binding.searchStoreItemGoodCnt.text = "999+"
                } else if (item.likeCnt > 100) {
                    binding.searchStoreItemGoodCnt.text = (item.likeCnt / 100 * 100).toString()
                } else {
                    binding.searchStoreItemGoodCnt.text = item.likeCnt.toString()
                }



                Glide.with(binding.searchStoreItemImage.context)
                    .load("${BaseUrl.OHNEULEN}${item.photoURL}").centerCrop()
                    .into(binding.searchStoreItemImage)

                //하트표시 클릭
                searchStoreItemLike.setOnClickListener {
                    if (!LoginViewModel.isLogin.value!!) {
                        loginDialog(root.context, SearchAppBar.newInstance(), true)
                        return@setOnClickListener
                    }
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            val response = mNetworkService.requestSetMemberLike(item.seq)
                            if (response.resultCode == ConstList.SUCCESS) {
                                searchStoreItemLike.isSelected = !searchStoreItemLike.isSelected
                                if (searchStoreItemLike.isSelected) {
                                    Handler(Looper.getMainLooper()).post {
                                        val goodCnt = binding.searchStoreItemGoodCnt.text.toString()
                                            .toInt()
                                        binding.searchStoreItemGoodCnt.text =
                                            (goodCnt + 1).toString()
                                        Toast.makeText(
                                            root.context,
                                            "찜 목록에 저장되었습니다",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }

//                                } else {
//                                    getItem(adapterPosition).likeCnt--
//                                    notifyItemChanged(adapterPosition)
//                                    searchStoreItemLike.isSelected=false
//                                    binding.searchStoreItemGoodCnt.text =
//                                        (binding.searchStoreItemGoodCnt.text.toString().toInt()-1).toString()
//                                    if (StoreAppBar.stat == 2) {
//                                        val newList = ArrayList(currentList).also {
//                                            it.removeAt(adapterPosition)
//                                        }
////                                        notifyItemRemoved(adapterPosition)
//                                        submitList(newList)
//                                    }
                                } else {
                                    Handler(Looper.getMainLooper()).post {
                                        bind(item)
                                    }
                                }
                            } else if (response.resultCode == ConstList.REQUIRE_LOGIN) {
                                LoginViewModel.isLogin.postValue(false)
                                loginDialog(root.context, SearchAppBar.newInstance(), true)
                            }
                        } catch (e: Exception) {
                            Timber.e(e.toString())
                        }
                    }

                }

                root.setOnClickListener {
                    val dialog = LoadingDialog.newInstance("매장 들어가는 중...")
                    dialog.show(MainActivity.supportFragmentManager, "loading")
//                    root.isEnabled=false
                    StoreFragment.storeSeq = item.seq
                    addMainFragment(StoreFragment.newInstance(), true)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchStoreViewHolder {
        return DataBindingUtil.inflate<SearchStoreItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.search_store_item,
            parent,
            false
        ).let {
            SearchStoreViewHolder(it)
        }
    }

    override fun onBindViewHolder(holder: SearchStoreViewHolder, position: Int) {
        holder.bind(getItem(position))

    }

}

object SearchStoreDiffUtil : DiffUtil.ItemCallback<SearchStore>() {
    override fun areItemsTheSame(oldItem: SearchStore, newItem: SearchStore): Boolean {
        return oldItem.seq == newItem.seq
    }

    override fun areContentsTheSame(oldItem: SearchStore, newItem: SearchStore): Boolean {
        return oldItem == newItem
    }

}