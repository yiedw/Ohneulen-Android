package com.goodchoice.android.ohneulen.ui.store

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.data.model.Image
import com.goodchoice.android.ohneulen.databinding.StoreImageItemBinding
import com.goodchoice.android.ohneulen.ui.MainActivity
import com.goodchoice.android.ohneulen.util.constant.BaseUrl

class StoreImageAdapter :
    ListAdapter<Image, StoreImageAdapter.StoreImageViewHolder>(StoreImageDiffUtil) {


    inner class StoreImageViewHolder(private val binding: StoreImageItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Image) {
            binding.apply {
                Glide.with(root)
                    .load("${BaseUrl.Ohneulen}${item.photoURL}")
                    .apply(RequestOptions().transform(CenterCrop(), RoundedCorners(20)))
                    .into(storeImageItem)

                root.setOnClickListener {
                    val dialog=StoreImageDetailDialog.newInstance(adapterPosition)
                    dialog.show(MainActivity.supportFragmentManager,"asdf")

//                    MainActivity.appbarFrameLayout.visibility = View.GONE
//                    addMainFragment(
//                        StoreImageDetail.newInstance(
//                            adapterPosition
//                        ), true
//                    )
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DataBindingUtil.inflate<StoreImageItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.store_image_item,
            parent,
            false
        ).let {
            StoreImageViewHolder(it)
        }


    override fun onBindViewHolder(holder: StoreImageViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

object StoreImageDiffUtil : DiffUtil.ItemCallback<Image>() {
    override fun areItemsTheSame(oldItem: Image, newItem: Image): Boolean {
        return oldItem.seq == newItem.seq
    }

    override fun areContentsTheSame(oldItem: Image, newItem: Image): Boolean {
        return oldItem == newItem
    }

}