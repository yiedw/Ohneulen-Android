package com.goodchoice.android.ohneulen.ui.mypage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.data.model.Review
import com.goodchoice.android.ohneulen.databinding.MypageReviewItemBinding

class MyPageReviewAdapter :
    ListAdapter<Review, MyPageReviewAdapter.MyPageViewHolder>(MyPageReviewDiffUtil) {

    inner class MyPageViewHolder(private val binding: MypageReviewItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(mypageReviewItem: Review) {
            binding.apply {
                mypageReview=mypageReviewItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DataBindingUtil.inflate<MypageReviewItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.mypage_review_item,
            parent,
            false
        ).let {
            MyPageViewHolder(it)
        }


    override fun onBindViewHolder(holder: MyPageViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}


object MyPageReviewDiffUtil : DiffUtil.ItemCallback<Review>() {
    override fun areItemsTheSame(oldItem: Review, newItem: Review): Boolean {
        return oldItem.seq == newItem.seq
    }

    override fun areContentsTheSame(oldItem: Review, newItem: Review): Boolean {
        return oldItem == newItem
    }

}