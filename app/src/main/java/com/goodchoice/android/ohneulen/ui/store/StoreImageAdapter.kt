package com.goodchoice.android.ohneulen.ui.store

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.data.model.Photo
import com.goodchoice.android.ohneulen.databinding.StoreImageItemBinding
import com.goodchoice.android.ohneulen.ui.MainActivity
import com.goodchoice.android.ohneulen.ui.store.menu.StoreMenuDetail
import com.goodchoice.android.ohneulen.util.addMainFragment
import timber.log.Timber

class StoreImageAdapter :
    ListAdapter<Photo, StoreImageAdapter.StoreImageViewHolder>(StoreImageDiffUtil) {


    inner class StoreImageViewHolder(private val binding: StoreImageItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Photo) {
            binding.apply {
                Glide.with(root)
                    .load(item.photoURL)
                    .apply(RequestOptions().transform(CenterCrop(), RoundedCorners(20)))
                    .into(storeImageItem)

                root.setOnClickListener {
                    MainActivity.appbarFrameLayout.visibility = View.GONE
                    addMainFragment(
                        StoreImageDetail.newInstance(
                            adapterPosition
                        ), true
                    )
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

object StoreImageDiffUtil : DiffUtil.ItemCallback<Photo>() {
    override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {
        return oldItem.seq == newItem.seq
    }

    override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {
        return oldItem == newItem
    }

}