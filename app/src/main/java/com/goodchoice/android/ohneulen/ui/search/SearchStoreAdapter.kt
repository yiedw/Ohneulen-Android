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
    var mAdapterPosition = 0

    inner class SearchStoreViewHolder(private val binding: SearchStoreItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: SearchStore) {
            binding.apply {
                searchStore = item
                //디버깅모드일때 신규 아이콘 보여주기
                if (BuildConfig.DEBUG) {
                    searchStoreItemNew.visibility = View.VISIBLE
                }
                //맨위에 아이템 제외하고 간격주기
                if (adapterPosition != 0) {
                    binding.searchStoreItem.setPadding(0, 10.dpToPx(), 0, 0)
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

                //로그인 되어있을대 좋아요 여부
                binding.searchStoreItemLike.isSelected = item.like
                //노이미지일때는 이미지를 불러오는게 아닌 로컬에 저장되어 있는걸 씀
                if(item.photoURL!="/public/img/content/favorite-noimage.png")
                    Glide.with(binding.searchStoreItemImage.context)
                        .load("${BaseUrl.OHNEULEN}${item.photoURL}").centerCrop()
                        .into(binding.searchStoreItemImage)

                //하트표시 클릭
                searchStoreItemLike.setOnClickListener {
                    if (!LoginViewModel.isLogin.value!!) {
                        loginDialog(root.context, true)
                        return@setOnClickListener
                    }
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            val response = mNetworkService.requestSetMemberLike(item.seq)
                            if (response.resultCode == ConstList.SUCCESS) {
                                item.like = !item.like
                                if (item.like) {
                                    //ui를 변경해야하기 떄문에 handler 사용
                                    Handler(Looper.getMainLooper()).post {
                                        item.likeCnt++
                                        bind(item)
                                        Toast.makeText(
                                            root.context,
                                            "찜 목록에 저장되었습니다",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }

                                } else {
                                    //ui를 변경해야하기 떄문에 handler 사용
                                    Handler(Looper.getMainLooper()).post {
                                        item.likeCnt--
                                        bind(item)
                                    }
                                }
                                //로그인이 안되어 있을경우
                            } else if (response.resultCode == ConstList.REQUIRE_LOGIN) {
                                LoginViewModel.isLogin.postValue(false)
                                loginDialog(root.context, true)
                            }
                        } catch (e: Exception) {
                            Timber.e(e.toString())
                        }
                    }

                }

                //매장클릭
                root.setOnClickListener {
                    mAdapterPosition = adapterPosition
                    val dialog = LoadingDialog.newInstance("매장 들어가는 중...")
                    dialog.show(MainActivity.supportFragmentManager, "loading")
//                    root.isEnabled=false
                    StoreFragment.storeSeq = item.seq
                    replaceAppbarFragment(StoreAppBar.newInstance())
                    addMainFragment(StoreFragment.newInstance(), true)
//                    val fragment=StoreFragment.newInstance()
//                    val fragmentTransaction = MainActivity.supportFragmentManager.beginTransaction()
//                    fragmentTransaction.addToBackStack(null)
//                    fragmentTransaction.add(R.id.main_frameLayout,fragment).hide(fragment).show(fragment).commit()
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