package com.goodchoice.android.ohneulen.ui.mypage

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.MypageTerms3Binding
import com.goodchoice.android.ohneulen.ui.MainActivity
import com.goodchoice.android.ohneulen.util.OnSwipeGesture

class MyPageTerms3 : Fragment() {
    companion object {
        fun newInstance() = MyPageTerms3()
    }

    private lateinit var binding: MypageTerms3Binding

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.mypage_terms3,
            container,
            false
        )
        //스와이프기능
        binding.mypageTerms3.setOnTouchListener(object : OnSwipeGesture(requireContext()) {
            override fun onSwipeRight() {
                super.onSwipeRight()
                MainActivity.supportFragmentManager.popBackStack()
            }
        })
        return binding.root
    }
}