package com.goodchoice.android.ohneulen.ui.like

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.data.model.LikeStore
import com.goodchoice.android.ohneulen.data.service.NetworkService
import com.goodchoice.android.ohneulen.databinding.LikeStoreItemBinding
import com.goodchoice.android.ohneulen.ui.MainActivity
import com.goodchoice.android.ohneulen.ui.dialog.LoadingDialog
import com.goodchoice.android.ohneulen.ui.login.LoginViewModel
import com.goodchoice.android.ohneulen.ui.store.StoreFragment
import com.goodchoice.android.ohneulen.util.addMainFragment
import com.goodchoice.android.ohneulen.util.constant.BaseUrl
import com.goodchoice.android.ohneulen.util.constant.ConstList
import com.goodchoice.android.ohneulen.util.loginDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception

class LikeStoreAdapter
    : ListAdapter<LikeStore, LikeStoreAdapter.LikeStoreViewHolder>(LikeStoreDiffUtil) {

    lateinit var mNetworkService:NetworkService

    inner class LikeStoreViewHolder(private val binding: LikeStoreItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: LikeStore) {
            binding.apply {
                likeStore = item
                likeStoreItemLike.isSelected=true
                if (item.image.isNotEmpty()) {
                    Glide.with(binding.likeStoreItemImage.context)
                        .load("${BaseUrl.OHNEULEN}${item.image[0].photoURL}").centerCrop()
                        .into(binding.likeStoreItemImage)
                } else {
                    Glide.with(binding.likeStoreItemImage.context)
                        .load(ContextCompat.getDrawable(root.context,R.drawable.search_no_img))
                        .into(binding.likeStoreItemImage)
                }
                likeStoreItemLike.setOnClickListener {
                    CoroutineScope(Dispatchers.IO).launch {
                        try{
                            val response=mNetworkService.requestSetMemberLike(item.seq)
                            if(response.resultCode==ConstList.SUCCESS){
                                likeStoreItemLike.isSelected=false
                                val newList=ArrayList(currentList).also { it.removeAt(adapterPosition) }
                                    submitList(newList)
                            }
                            else if(response.resultCode==ConstList.REQUIRE_LOGIN){
                                LoginViewModel.isLogin.postValue(false)
                                loginDialog(root.context, true)
                            }
                        }
                        catch (e:Exception){
                            Timber.e(e.toString())
                        }
                    }
                }

                root.setOnClickListener {
                    val dialog=LoadingDialog.newInstance("매장 들어가는 중...")
                    dialog.show(MainActivity.supportFragmentManager,"loading")
                    StoreFragment.storeSeq=item.seq
                    addMainFragment(StoreFragment.newInstance())
                }


            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LikeStoreViewHolder {
        return DataBindingUtil.inflate<LikeStoreItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.like_store_item,
            parent,
            false
        ).let {
            LikeStoreViewHolder(it)
        }
    }

    override fun onBindViewHolder(holder: LikeStoreViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}


object LikeStoreDiffUtil : DiffUtil.ItemCallback<LikeStore>() {
    override fun areItemsTheSame(oldItem: LikeStore, newItem: LikeStore): Boolean {
        return oldItem.seq == newItem.seq
    }

    override fun areContentsTheSame(oldItem: LikeStore, newItem: LikeStore): Boolean {
        return oldItem == newItem
    }

}