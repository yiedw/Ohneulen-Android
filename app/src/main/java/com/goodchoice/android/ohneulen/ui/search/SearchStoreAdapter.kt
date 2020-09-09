package com.goodchoice.android.ohneulen.ui.search

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.goodchoice.android.ohneulen.App
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.data.model.Store
import com.goodchoice.android.ohneulen.data.service.NetworkService
import com.goodchoice.android.ohneulen.databinding.StoreItemBinding
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
    ListAdapter<Store, SearchStoreAdapter.SearchStoreViewHolder>(SearchStoreDiffUtil) {

    var first = false
    lateinit var parentView: RecyclerView
    lateinit var mNetworkService: NetworkService

    inner class SearchStoreViewHolder(private val binding: StoreItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: Store) {
            binding.apply {
                store = item


                //평점
                val point= String.format("%.1f",(item.P_1+item.P_2+item.P_3+item.P_4+item.P_5+item.P_6)/6)
                binding.storeItemRating.text=point

                //리뷰 갯수
                if(item.review_cnt>=1000){
                    binding.storeItemReviewCnt.text="999+"
                }
                else if(item.review_cnt>100){
                    binding.storeItemReviewCnt.text=(item.review_cnt/100*100).toString()
                }
                else{
                    binding.storeItemReviewCnt.text=item.review_cnt.toString()
                }

                //좋아요 갯수
                if(item.like_cnt>=1000){
                    binding.storeItemGoodCnt.text="999+"
                }
                else if(item.like_cnt>100){
                    binding.storeItemGoodCnt.text=(item.like_cnt/100*100).toString()
                }
                else{
                    binding.storeItemGoodCnt.text=item.like_cnt.toString()
                }



                if (item.photoURL.isNullOrEmpty()) {
                    if (!item.image.isNullOrEmpty()) {
                        Glide.with(binding.storeItemImage.context)
                            .load("${BaseUrl.OHNEULEN}${item.image[0].photoURL}").centerCrop()
                            .into(binding.storeItemImage)
                    }
                    else{
                        Glide.with(binding.storeItemImage.context)
                            .load(ContextCompat.getDrawable(root.context,R.drawable.search_no_img))
                            .into(binding.storeItemImage)
                    }
                } else {
                    Glide.with(binding.storeItemImage.context)
                        .load("${BaseUrl.OHNEULEN}${item.photoURL}").centerCrop()
                        .into(binding.storeItemImage)
                }
                if (StoreAppBar.stat == 2) {
                    //찜목록일때
                    storeItemLike.isSelected = true
                } else if (StoreAppBar.stat == 1) {
                    //검색일때
                    storeItemLike.isSelected = item.likes
                }
                //하트표시 클릭
                storeItemLike.setOnClickListener {
                    if (!LoginViewModel.isLogin.value!!) {
                        loginDialog(root.context, SearchAppBar.newInstance(), true)
                        return@setOnClickListener
                    }
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            val response = mNetworkService.requestSetMemberLike(item.seq)
                            if (response.resultCode == ConstList.SUCCESS) {
                                storeItemLike.isSelected = !storeItemLike.isSelected
                                if (storeItemLike.isSelected) {
                                    Handler(Looper.getMainLooper()).post {
                                        Toast.makeText(
                                            root.context,
                                            "찜 목록에 저장되었습니다",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                } else {
                                    if (StoreAppBar.stat == 2) {
                                        val newList=ArrayList(currentList).also { it.removeAt(adapterPosition) }
//                                        notifyItemRemoved(adapterPosition)
                                        submitList(newList)
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
                    parentView.isEnabled = false
                    val dialog=LoadingDialog.newInstance("매장 들어가는 중...")
                    dialog.show(MainActivity.supportFragmentManager,"loading")
//                    root.isEnabled=false
                    StoreFragment.storeSeq = item.seq
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