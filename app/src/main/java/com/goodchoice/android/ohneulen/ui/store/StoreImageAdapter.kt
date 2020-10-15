package com.goodchoice.android.ohneulen.ui.store

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.data.model.Image
import com.goodchoice.android.ohneulen.databinding.StoreImageItemBinding
import com.goodchoice.android.ohneulen.ui.MainActivity
import com.goodchoice.android.ohneulen.ui.dialog.ImageDetailStoreDialog
import com.goodchoice.android.ohneulen.util.constant.BaseUrl
import com.goodchoice.android.ohneulen.util.dpToPx
import timber.log.Timber

class StoreImageAdapter :
    ListAdapter<Image, StoreImageAdapter.StoreImageViewHolder>(StoreImageDiffUtil) {


    inner class StoreImageViewHolder(private val binding: StoreImageItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Image) {

            binding.apply {
                if(adapterPosition==0){
                    binding.storeImageItem.setPadding(20.dpToPx(),0,10.dpToPx(),0)
                }
                else if(adapterPosition==itemCount-1){
                    binding.storeImageItem.setPadding(0,0,20.dpToPx(),0)
                }
                Glide.with(root)
                    .load("${BaseUrl.OHNEULEN}${item.photoURL}")
                    .apply(RequestOptions().transform(CenterCrop(),RoundedCorners(20)))
                    .into(storeImageItemImage)

                root.setOnClickListener {
                    val dialog = ImageDetailStoreDialog.newInstance(adapterPosition)
                    dialog.show(MainActivity.supportFragmentManager, "")
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