package com.goodchoice.android.ohneulen.ui.adapter

import android.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.goodchoice.android.ohneulen.data.model.Photo
import com.goodchoice.android.ohneulen.databinding.ImageDetailItemBinding

class ImageDetailAdapter {
    inner class ImageDetailViewHolder(private val binding:ImageDetailItemBinding)
        :RecyclerView.ViewHolder(binding.root){
        fun bind(item:Photo){
            binding.apply {
            }
        }
    }
}