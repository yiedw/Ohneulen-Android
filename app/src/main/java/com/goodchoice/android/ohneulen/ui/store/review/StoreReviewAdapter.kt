package com.goodchoice.android.ohneulen.ui.store.review

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.data.model.Review
import com.goodchoice.android.ohneulen.databinding.ReviewItemBinding
import timber.log.Timber

class StoreReviewAdapter : RecyclerView.Adapter<StoreReviewAdapter.StoreReviewViewHolder>() {
    var reviewList= listOf<Review>()
    inner class StoreReviewViewHolder(private val binding: ReviewItemBinding)
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
            StoreReviewViewHolder(it)
        }

    override fun getItemCount()=reviewList.size

    override fun onBindViewHolder(holder: StoreReviewViewHolder, position: Int) {
        holder.bind(reviewList[position])
    }
}