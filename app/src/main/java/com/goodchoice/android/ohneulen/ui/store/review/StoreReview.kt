package com.goodchoice.android.ohneulen.ui.store.review

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.RadioButton
import android.widget.RatingBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.data.model.StoreDetail
import com.goodchoice.android.ohneulen.databinding.StoreReviewBinding
import com.goodchoice.android.ohneulen.ui.login.Login
import com.goodchoice.android.ohneulen.ui.login.LoginAppBar
import com.goodchoice.android.ohneulen.ui.login.LoginViewModel
import com.goodchoice.android.ohneulen.ui.store.StoreAppBar
import com.goodchoice.android.ohneulen.ui.store.StoreFragment
import com.goodchoice.android.ohneulen.ui.store.StoreViewModel
import com.goodchoice.android.ohneulen.util.*
import org.koin.android.ext.android.bind
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        storeViewModel.storeDetail.observe(viewLifecycleOwner, Observer {
            reviewCnt(it)
            //리뷰가 0개일때
            if (it.reviewCnt == 0) {
                reviewEmpty()
            } else {
                reviewNotEmpty()
            }
        })

        //정렬 bold 주기
        for (i in binding.storeReviewRadioGroup.children) {
            radioButtonBold(i as RadioButton)
        }
    }

    override fun onResume() {
        super.onResume()
    }

    @SuppressLint("SetTextI18n")
    private fun reviewCnt(storeDetail: StoreDetail) {
        val spannable = SpannableString("${storeDetail.reviewCnt}개")
        spannable.setSpan(
            StyleSpan(Typeface.BOLD),
            0,
            spannable.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.storeReviewTv1.text = TextUtils.concat(spannable, "의 후기가 있습니다")
    }

    private fun reviewEmpty() {
        binding.storeReviewEmpty.visibility = View.VISIBLE
        binding.storeReviewNotEmptyCon.visibility = View.GONE

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
        binding.storeReviewNotEmptyCon.visibility = View.VISIBLE
        binding.storeReviewRv.visibility = View.VISIBLE
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

    fun reviewWriteClick(view: View) {
        if (!LoginViewModel.isLogin.value!!) {
            loginDialog(requireContext(), StoreAppBar.newInstance(),false)
        } else {
            replaceAppbarFragment(StoreReviewWriteAppbar.newInstance())
            popupFragment(StoreReviewWrite.newInstance())
//            addMainFragment(StoreReviewWrite.newInstance(), true)

        }
    }


}