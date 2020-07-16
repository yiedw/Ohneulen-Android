package com.goodchoice.android.ohneulen.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.data.model.Review
import com.goodchoice.android.ohneulen.databinding.ReviewItemBinding
import com.goodchoice.android.ohneulen.ui.store.review.StoreReviewReport
import com.goodchoice.android.ohneulen.ui.store.review.StoreReviewReportAppBar
import com.goodchoice.android.ohneulen.util.addMainFragment
import com.goodchoice.android.ohneulen.util.replaceAppbarFragment

class ReviewAdapter(val report: Boolean = true) :
    ListAdapter<Review, ReviewAdapter.ReviewViewHolder>(ReviewDiffUtil) {
//    var reviewList = listOf<Review>()

    inner class ReviewViewHolder(private val binding: ReviewItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(reviewItem: Review) {
            binding.apply {
                review = reviewItem
                if (!report)
                    reviewItemReport.visibility = View.GONE
                reviewItemReport.setOnClickListener {
                    replaceAppbarFragment(StoreReviewReportAppBar.newInstance())
                    addMainFragment(StoreReviewReport.newInstance(), true)
                }
//                executePendingBindings()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DataBindingUtil.inflate<ReviewItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.review_item,
            parent,
            false
        ).let {
            ReviewViewHolder(it)
        }

    override fun getItemCount() = super.getItemCount()

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

object ReviewDiffUtil :DiffUtil.ItemCallback<Review>() {
    override fun areItemsTheSame(oldItem: Review, newItem: Review): Boolean {
        return oldItem.seq==newItem.seq
    }

    override fun areContentsTheSame(oldItem: Review, newItem: Review): Boolean {
        return oldItem==newItem
    }
}