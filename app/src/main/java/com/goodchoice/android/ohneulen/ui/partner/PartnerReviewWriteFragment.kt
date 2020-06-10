package com.goodchoice.android.ohneulen.ui.partner

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.PartnerReviewWriteFragmentBinding

class PartnerReviewWriteFragment : Fragment() {

    companion object {
        fun newInstance() = PartnerReviewWriteFragment()
    }

    private lateinit var binding: PartnerReviewWriteFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.partner_review_write_fragment,
            container,
            false
        )
        return binding.root
    }
}