package com.goodchoice.android.ohneulen.ui.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.MypageContactusBinding
import com.goodchoice.android.ohneulen.ui.MainActivity

class MyPageContactus : Fragment() {
    companion object {
        fun newInstance() = MyPageContactus()
    }

    private lateinit var binding: MypageContactusBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.mypage_contactus,
            container,
            false
        )
        return binding.root
    }

}