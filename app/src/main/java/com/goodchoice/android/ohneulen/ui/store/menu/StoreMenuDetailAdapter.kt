package com.goodchoice.android.ohneulen.ui.store.menu

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.data.model.Photo
import com.goodchoice.android.ohneulen.data.model.StoreMenu
import com.goodchoice.android.ohneulen.databinding.StoreMenuDetailItemBinding
import com.goodchoice.android.ohneulen.ui.MainActivity
import timber.log.Timber

class StoreMenuDetailAdapter() :
    RecyclerView.Adapter<StoreMenuDetailAdapter.StoreMenuDetailViewHolder>() {

    lateinit var photoList: MutableList<Photo>
    var menuList = listOf<StoreMenu>()
    var liveIndex = MutableLiveData<Int>(0)
    interface OnNextClickListener{
        fun onNextClick(pos:Int)
    }
    private var mListener:OnNextClickListener?=null

    fun setOnNextClickListener(listener:OnNextClickListener){
        this.mListener=listener
    }

    inner class StoreMenuDetailViewHolder(
        private val binding: StoreMenuDetailItemBinding,
        val context: Context
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(menuItem: StoreMenu) {
            binding.apply {
                if (menuList.size - 1 == adapterPosition) {
                    storeMenuDetailRight.visibility = View.GONE
                }
                else{
                    storeMenuDetailRight.visibility=View.VISIBLE
                }
                if (adapterPosition == 0) {
                    storeMenuDetailLeft.visibility = View.GONE
                }
                else{
                    storeMenuDetailLeft.visibility=View.VISIBLE
                }
                storeMenuDetailLeft.setOnClickListener {
                    mListener!!.onNextClick(adapterPosition-1)
                }
                storeMenuDetailRight.setOnClickListener {
                    mListener!!.onNextClick(adapterPosition+1)

                }
                storeMenuDetailBack.setOnClickListener {
                    MainActivity.supportFragmentManager.popBackStack()
                    MainActivity.appbarFrameLayout.visibility = View.VISIBLE
                }
//                photo = photoItem
                menu = menuItem
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DataBindingUtil.inflate<StoreMenuDetailItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.store_menu_detail_item,
            parent,
            false
        ).let {
            StoreMenuDetailViewHolder(it, parent.context)
        }

    override fun getItemCount() = menuList.size

    override fun onBindViewHolder(holder: StoreMenuDetailViewHolder, position: Int) {
        holder.bind(menuList[position])
    }
}