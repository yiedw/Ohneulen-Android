package com.goodchoice.android.ohneulen.ui.adapter

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.data.model.Review
import com.goodchoice.android.ohneulen.data.model.StoreDetail
import com.goodchoice.android.ohneulen.databinding.ReviewItemBinding
import com.goodchoice.android.ohneulen.databinding.StoreReviewInfoItemBinding
import com.goodchoice.android.ohneulen.ui.MainActivity
import com.goodchoice.android.ohneulen.ui.dialog.ImageDetailReviewDialog
import com.goodchoice.android.ohneulen.ui.login.LoginViewModel
import com.goodchoice.android.ohneulen.ui.store.StoreAppBar
import com.goodchoice.android.ohneulen.ui.store.review.StoreReviewReport
import com.goodchoice.android.ohneulen.ui.store.review.StoreReviewReportAppBar
import com.goodchoice.android.ohneulen.ui.store.review.StoreReviewWrite
import com.goodchoice.android.ohneulen.ui.store.review.StoreReviewWriteAppbar
import com.goodchoice.android.ohneulen.util.constant.BaseUrl
import com.goodchoice.android.ohneulen.util.loginDialog
import com.goodchoice.android.ohneulen.util.popupFragment
import com.goodchoice.android.ohneulen.util.replaceAppbarFragment
import com.goodchoice.android.ohneulen.util.textColor
import timber.log.Timber

class ReviewAdapter(val report: Boolean = true) :
    ListAdapter<Review, RecyclerView.ViewHolder>(ReviewDiffUtil) {
    //    var reviewList = listOf<Review>()
    lateinit var storeDetail: StoreDetail

    inner class ReviewViewHolder(private val binding: ReviewItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(reviewItem: Review) {
            binding.apply {
                review = reviewItem
                reviewItemRatingbar.rating = reviewItem.point_1.toFloat()
                if (!report)
                    reviewItemReport.visibility = View.GONE
                reviewItemReport.setOnClickListener {
                    replaceAppbarFragment(StoreReviewReportAppBar.newInstance())
                    popupFragment(StoreReviewReport.newInstance(reviewItem))
                }


                //리뷰에 사진이 있을때
                if (!reviewItem.imgList.isNullOrEmpty()) {
                    reviewItemImage.visibility = View.VISIBLE
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
                else{
                    reviewItemImage.visibility=View.GONE
                }

                //날짜 넣어주기(형식 약간 변경)
                reviewItemDate.text =
                    reviewItem.modifyDate
//                    "${reviewItem.modifyDate.substring(0, 4)}.${reviewItem.modifyDate.substring(
//                        5,
//                        7
//                    )}.${reviewItem.modifyDate.substring(8, 10)}"

                //닉네임 넣어주기
                if (reviewItem.nickName.isNullOrEmpty()) {
                    //닉네임이 없을때
                    reviewItemNickName.text = reviewItem.memName
                } else {
                    //닉네임이 있을때
                    reviewItemNickName.text = reviewItem.nickName
                }

                //좋아요 클릭
                reviewItemGood.setOnClickListener {
//                    CoroutineScope(Dispatchers.IO).launch {
//                    }
                    reviewItemGood.isSelected = !reviewItemGood.isSelected

//                    Handler(Looper.getMainLooper()).post {
//                        bind(reviewItem)
//                    }
                }
            }
        }
    }

    //정보와 리뷰가 같이있는 홀더
    inner class StoreReviewInfoViewHolder(private val binding: StoreReviewInfoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")

        //바인딩
        fun bind(reviewItem: Review) {
            //info setting
            //정렬 + bold 주기
            for (i in binding.storeReviewInfoRadioGroup.children) {
                radioButtonBold(i as RadioButton)
            }
            //평점세팅
            ratingSetting(storeDetail)
            //리뷰갯수 + 색깔 세팅
            reviewCnt(storeDetail)
            //후기 쓰기 클릭했을때
            binding.storeReviewInfoWrite.setOnClickListener {
                if (!LoginViewModel.isLogin.value!!) {
                    loginDialog(binding.root.context, StoreAppBar.newInstance(), false)
                } else {
                    replaceAppbarFragment(StoreReviewWriteAppbar.newInstance())
                    popupFragment(StoreReviewWrite.newInstance())
//            addMainFragment(StoreReviewWrite.newInstance(), true)
                }
            }


            //review setting
            binding.apply {
                review = reviewItem
                reviewItemRatingbar.rating = reviewItem.point_1.toFloat()
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
                else{
                    reviewItemImage.visibility=View.GONE
                }

                //날짜 넣어주기(형식 약간 변경)
                reviewItemDate.text =
                    reviewItem.modifyDate
//                    "${reviewItem.modifyDate.substring(0, 4)}.${reviewItem.modifyDate.substring(
//                        5,
//                        7
//                    )}.${reviewItem.modifyDate.substring(8, 10)}"

                //닉네임 넣어주기
                if (reviewItem.nickName.isNullOrEmpty()) {
                    //닉네임이 없을때
                    reviewItemNickName.text = reviewItem.memName
                } else {
                    //닉네임이 있을때
                    reviewItemNickName.text = reviewItem.nickName
                }

                //좋아요 클릭
                reviewItemGood.setOnClickListener {
//                    CoroutineScope(Dispatchers.IO).launch {
//                    }
                    reviewItemGood.isSelected = !reviewItemGood.isSelected

//                    Handler(Looper.getMainLooper()).post {
//                        bind(reviewItem)
//                    }
                }
            }
        }

        private fun radioButtonBold(radioButton: RadioButton) {
            radioButton.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    radioButton.setTypeface(null, Typeface.BOLD)
                } else {
                    radioButton.setTypeface(null, Typeface.NORMAL)
                }
            }
        }

        @SuppressLint("SetTextI18n")
        private fun reviewCnt(storeDetail: StoreDetail) {
            //리뷰 갯수 + 색 효과 주기
            val spannable = SpannableString("${storeDetail.reviewCnt}개")
            spannable.setSpan(
                StyleSpan(Typeface.BOLD),
                0,
                spannable.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            binding.storeReviewInfoTv1.text = TextUtils.concat(spannable, "의 후기가 있습니다")
        }

        private fun ratingSetting(storeDetail: StoreDetail) {
            var point1 = 0.0
            var point2 = 0.0
            var point3 = 0.0
            var point4 = 0.0
            var point5 = 0.0
            var point6 = 0.0
            for (i in storeDetail.reviewList) {
                point1 += i.point_1.toDouble()
                point2 += i.point_2.toDouble()
                point3 += i.point_3.toDouble()
                point4 += i.point_4.toDouble()
                point5 += i.point_5.toDouble()
                point6 += i.point_6.toDouble()
            }
            point1 /= storeDetail.reviewList.size
            point2 /= storeDetail.reviewList.size
            point3 /= storeDetail.reviewList.size
            point4 /= storeDetail.reviewList.size
            point5 /= storeDetail.reviewList.size
            point6 /= storeDetail.reviewList.size
            binding.storeReviewInfoRatingbar.rating = point1.toFloat()
            binding.storeReviewInfoProgressbarPrice.progress = (point2 * 20).toInt()
            binding.storeReviewInfoProgressbarFlavor.progress = (point3 * 20).toInt()
            binding.storeReviewInfoProgressbarKindness.progress = (point4 * 20).toInt()
            binding.storeReviewInfoProgressbarClean.progress = (point5 * 20).toInt()
            binding.storeReviewInfoProgressbarMood.progress = (point6 * 20).toInt()
            binding.storeReviewInfoRatingScore.text = ((point1 * 10).toInt() / 10.0).toString()
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if (viewType == 0) {
            //info + review 를 넣어줌
            return DataBindingUtil.inflate<StoreReviewInfoItemBinding>(
                LayoutInflater.from(parent.context),
                R.layout.store_review_info_item,
                parent,
                false
            ).let {
                StoreReviewInfoViewHolder(it)
            }
        } else {
            //review 를 넣어줌
            return DataBindingUtil.inflate<ReviewItemBinding>(
                LayoutInflater.from(parent.context),
                R.layout.review_item,
                parent,
                false
            ).let {
                ReviewViewHolder(it)
            }
        }
    }

    override fun getItemCount() = super.getItemCount()

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position == 0) {
            (holder as StoreReviewInfoViewHolder).bind(getItem(position))
        } else {
            (holder as ReviewViewHolder).bind(getItem(position))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0)
            0
        else
            1
//        return super.getItemViewType(position)
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