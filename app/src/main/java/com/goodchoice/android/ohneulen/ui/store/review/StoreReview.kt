package com.goodchoice.android.ohneulen.ui.store.review

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.data.model.StoreDetail
import com.goodchoice.android.ohneulen.databinding.StoreReviewBinding
import com.goodchoice.android.ohneulen.ui.adapter.ReviewAdapter
import com.goodchoice.android.ohneulen.ui.login.LoginViewModel
import com.goodchoice.android.ohneulen.ui.store.StoreAppBar
import com.goodchoice.android.ohneulen.ui.store.StoreFragment
import com.goodchoice.android.ohneulen.ui.store.StoreViewModel
import com.goodchoice.android.ohneulen.util.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.util.*
import kotlin.concurrent.schedule
import kotlin.concurrent.timer

class StoreReview : Fragment() {
    companion object {
        fun newInstance() =
            StoreReview()
    }

    private lateinit var binding: StoreReviewBinding
    private val storeViewModel: StoreViewModel by viewModel()
    private val loginViewModel: LoginViewModel by viewModel()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.store_review,
            container,
            false
        )
        binding.fragment = this
        binding.lifecycleOwner = this
        binding.viewModel = storeViewModel
        return binding.root
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //rv adapter 세팅
        val adapter = ReviewAdapter()
        binding.storeReviewRv.adapter = adapter
        storeViewModel.storeDetail.observe(viewLifecycleOwner, Observer {
//            reviewCnt(it)
            //리뷰가 0개일때
            if (it.reviewCnt == 0) {
                reviewEmpty()
            } else {
                reviewNotEmpty()
                ratingSetting(it)
            }
            //info의 리뷰 갯수와 점수를 알려주기 위해 필요
            adapter.storeDetail = it
//            adapter.notifyItemChanged(0) //데이터를 바로 반영
            //데이터가 바뀌자마자 바로 반영되야 하므로 여기서 넣어줌
            adapter.submitList(it.reviewList)

            //후기작성후 스크롤 맨 위로 올려주기 (바로하면 rv가 업데이트되기 전에 스크롤이 올라가버림 -> 살짝 지연이 필요)
            Timer("scrollToTop", false).schedule(100) {
                //timer 안에서는 ui변경 불가능하기 때문에 main에서 호출
                requireActivity().runOnUiThread {
                    binding.storeReviewRv.scrollToPosition(0)
                }
            }

        })

        //정렬 bold 주기
//        for (i in binding.storeReviewRadioGroup.children) {
//            radioButtonBold(i as RadioButton)
//        }
    }


    @SuppressLint("SetTextI18n")
//    private fun reviewCnt(storeDetail: StoreDetail) {
//        val spannable = SpannableString("${storeDetail.reviewCnt}개")
//        spannable.setSpan(
//            StyleSpan(Typeface.BOLD),
//            0,
//            spannable.length,
//            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
//        )
//        binding.storeReviewTv1.text = TextUtils.concat(spannable, "의 후기가 있습니다")
//    }

    private fun reviewEmpty() {
        binding.storeReviewEmpty.visibility = View.VISIBLE
        binding.storeReviewFrameLayout.visibility = View.VISIBLE
        binding.storeReviewRv.visibility = View.GONE

        val textReview =
            textColor("맛집", 0, 2, ContextCompat.getColor(requireContext(), R.color.colorOhneulen))
        val textWrite = textColor(
            "후기 작성하기",
            0,
            7,
            ContextCompat.getColor(requireContext(), R.color.colorOhneulen)
        )
        textReview.setSpan(
            StyleSpan(Typeface.BOLD),
            0,
            textReview.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        textWrite.setSpan(
            StyleSpan(Typeface.BOLD),
            0,
            textWrite.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        val text = TextUtils.concat(
            "아직 작성된 후기가 없어요\n",
            "지금 나만의 ",
            textReview,
            "을 공유하시려면\n",
            "상단의 ",
            textWrite,
            "버튼을 클릭해 주세요!"
        )
        binding.storeReviewEmptyTv.text = text
    }

    private fun reviewNotEmpty() {
        binding.storeReviewEmpty.visibility = View.GONE
        binding.storeReviewFrameLayout.visibility = View.GONE
        binding.storeReviewRv.visibility = View.VISIBLE
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
//        binding.storeReviewRatingbar.rating = point1.toFloat()
//        binding.storeReviewProgressbarPrice.progress = (point2 * 20).toInt()
//        binding.storeReviewProgressbarFlavor.progress = (point3 * 20).toInt()
//        binding.storeReviewProgressbarKindness.progress = (point4 * 20).toInt()
//        binding.storeReviewProgressbarClean.progress = (point5 * 20).toInt()
//        binding.storeReviewProgressbarMood.progress = (point6 * 20).toInt()
////        binding.storeReviewRatingScore.text=String.format("%.1f",point1)
//        binding.storeReviewRatingScore.text = ((point1 * 10).toInt() / 10.0).toString()

    }

    fun reviewWriteClick(view: View) {
        if (!LoginViewModel.isLogin.value!!) {
            loginDialog(requireContext(), StoreAppBar.newInstance(), false)
        } else {
            replaceAppbarFragment(StoreReviewWriteAppbar.newInstance())
            popupFragment(StoreReviewWrite.newInstance())
//            addMainFragment(StoreReviewWrite.newInstance(), true)
        }
    }


}