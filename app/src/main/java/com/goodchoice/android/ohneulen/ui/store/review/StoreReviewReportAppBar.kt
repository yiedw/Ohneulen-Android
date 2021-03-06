package com.goodchoice.android.ohneulen.ui.store.review

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.ReviewReportAppbarBinding
import com.goodchoice.android.ohneulen.ui.MainActivity
import com.goodchoice.android.ohneulen.ui.store.StoreAppBar
import com.goodchoice.android.ohneulen.util.OnBackPressedListener
import com.goodchoice.android.ohneulen.util.replaceAppbarFragment

class StoreReviewReportAppBar : Fragment(), OnBackPressedListener {
    companion object {
        fun newInstance() = StoreReviewReportAppBar()
    }

    private lateinit var binding: ReviewReportAppbarBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.review_report_appbar,
            container,
            false
        )
        binding.fragment = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.reviewReportAppbarClose.setOnClickListener {
//            replaceAppbarFragment(StoreAppBar.newInstance(), tag = "storeAppBar")
            MainActivity.supportFragmentManager.popBackStack()

        }
    }


    override fun onBackPressed() {
//        replaceAppbarFragment(StoreAppBar.newInstance(), tag = "storeAppBar")
        MainActivity.supportFragmentManager.popBackStack()
    }

}