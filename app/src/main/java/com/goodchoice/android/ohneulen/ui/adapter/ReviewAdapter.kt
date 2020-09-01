package com.goodchoice.android.ohneulen.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.data.model.Review
import com.goodchoice.android.ohneulen.databinding.ReviewItemBinding
import com.goodchoice.android.ohneulen.ui.MainActivity
import com.goodchoice.android.ohneulen.ui.dialog.ImageDetailReviewDialog
import com.goodchoice.android.ohneulen.ui.store.review.StoreReviewReport
import com.goodchoice.android.ohneulen.ui.store.review.StoreReviewReportAppBar
import com.goodchoice.android.ohneulen.util.constant.BaseUrl
import com.goodchoice.android.ohneulen.util.popupFragment
import com.goodchoice.android.ohneulen.util.replaceAppbarFragment

class ReviewAdapter(val report: Boolean = true) :
    ListAdapter<Review, ReviewAdapter.ReviewViewHolder>(ReviewDiffUtil) {
//    var reviewList = listOf<Review>()

    inner class ReviewViewHolder(private val binding: ReviewItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(reviewItem: Review) {
            binding.apply {
                review = reviewItem
//                val rating: Double =
//                    (reviewItem.point_1.toDouble() + reviewItem.point_2.toDouble() +
//                            reviewItem.point_3.toDouble() + reviewItem.point_4.toDouble() + reviewItem.point_5.toDouble()) / 5
//                reviewItemRatingbar.rating = rating.toFloat()
                if (!report)
                    reviewItemReport.visibility = View.GONE
                reviewItemReport.setOnClickListener {
                    replaceAppbarFragment(StoreReviewReportAppBar.newInstance())
                    popupFragment(StoreReviewReport.newInstance(reviewItem))
                }


                //리뷰에 사진이 있을때
                if (!reviewItem.imgList.isNullOrEmpty()) {
                    reviewItemImage.setOnClickListener {
                        val dialog = ImageDetailReviewDialog.newInstance(adapterPosition)
                        dialog.show(MainActivity.supportFragmentManager, "")
                    }
                    Glide.with(root).load(BaseUrl.OHNEULEN + reviewItem.imgList[0].photoURL)
                        .centerCrop()
                        .into(reviewItemImage)

                    //사진이 여러개 있을때
                    if (reviewItem.imgList.size > 1) {
                        reviewItemAmount.text = "+${reviewItem.imgList.size}"
                        reviewItemAmount.visibility = View.VISIBLE
                    } else
                        reviewItemAmount.visibility = View.INVISIBLE
                }

                //날짜 넣어주기(형식 약간 변경)
                reviewItemDate.text =
                    reviewItem.modifyDate
//                    "${reviewItem.modifyDate.substring(0, 4)}.${reviewItem.modifyDate.substring(
//                        5,
//                        7
//                    )}.${reviewItem.modifyDate.substring(8, 10)}"
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

object ReviewDiffUtil : DiffUtil.ItemCallback<Review>() {
    override fun areItemsTheSame(oldItem: Review, newItem: Review): Boolean {
        return oldItem.seq == newItem.seq
    }

    override fun areContentsTheSame(oldItem: Review, newItem: Review): Boolean {
        return oldItem == newItem
    }
}