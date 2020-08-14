package com.goodchoice.android.ohneulen.ui.store.review

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.data.model.Review
import com.goodchoice.android.ohneulen.databinding.ReviewReportBinding
import com.goodchoice.android.ohneulen.ui.store.StoreViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class StoreReviewReport(private val review: Review) : Fragment() {
    companion object {
        fun newInstance(review: Review) = StoreReviewReport(review)
    }

    private lateinit var binding: ReviewReportBinding
    private val storeViewModel: StoreViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.review_report,
            container,
            false
        )
        binding.viewModel = storeViewModel
        binding.review = this.review
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //뒤에 레이아웃에 터치 안먹게하기
        binding.reviewReport.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                return true
            }

        })

        binding.reviewReportDetail.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.reviewReportDetailEtLength.text =
                    binding.reviewReportDetail.text.toString().length.toString()
            }

        })
    }
}