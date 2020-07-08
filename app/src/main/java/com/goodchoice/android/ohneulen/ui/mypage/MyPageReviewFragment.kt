package com.goodchoice.android.ohneulen.ui.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.MypageReviewFragmentBinding
import com.goodchoice.android.ohneulen.databinding.StoreAppbarFragmentBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class MyPageReviewFragment : Fragment() {
    companion object {
        fun newInstance() = MyPageReviewFragment()
    }

    private lateinit var binding: MypageReviewFragmentBinding
    private val mypageViewModel :MyPageViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=DataBindingUtil.inflate(
            inflater,
            R.layout.mypage_review_fragment,
            container,
            false
        )
        binding.fragment=this
        binding.lifecycleOwner=this
        binding.viewModel=mypageViewModel
        return binding.root
    }
}