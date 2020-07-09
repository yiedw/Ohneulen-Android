package com.goodchoice.android.ohneulen.ui.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.MypageFaqBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class MyPageFAQ :Fragment() {
    companion object{
        fun newInstance()=MyPageFAQ()
    }

    private lateinit var binding:MypageFaqBinding
    private val myPageViewModel:MyPageViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=DataBindingUtil.inflate(
            inflater,
            R.layout.mypage_faq,
            container,
            false
        )
        binding.viewModel=myPageViewModel
        binding.lifecycleOwner=this
        return binding.root
    }

}