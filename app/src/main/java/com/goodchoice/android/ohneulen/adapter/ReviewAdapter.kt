package com.goodchoice.android.ohneulen.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.data.model.Review
import com.goodchoice.android.ohneulen.databinding.ReviewItemBinding

class ReviewAdapter : RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {
    var reviewList= listOf<Review>()
    inner class ReviewViewHolder(private val binding: ReviewItemBinding)
        :RecyclerView.ViewHolder(binding.root){
        fun bind(reviewItem: Review){
            binding.apply {
                review=reviewItem
                executePendingBindings()
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)=
        DataBindingUtil.inflate<ReviewItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.review_item,
            parent,
            false
        ).let {
            ReviewViewHolder(it)
        }

    override fun getItemCount()=reviewList.size

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        holder.bind(reviewList[position])
    }
}