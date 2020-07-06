package com.goodchoice.android.ohneulen.ui.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.MypageWithdrawalFragmentBinding

class MyPageWithdrawalFragment :Fragment() {
    companion object{
        fun newInstance()=MyPageWithdrawalFragment()
    }
    private lateinit var binding:MypageWithdrawalFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=DataBindingUtil.inflate(
            inflater,
            R.layout.mypage_withdrawal_fragment,
            container,
            false
        )
        return binding.root
    }
}