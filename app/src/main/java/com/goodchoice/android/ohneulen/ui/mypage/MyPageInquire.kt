package com.goodchoice.android.ohneulen.ui.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.MypageInquireBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class MyPageInquire : Fragment(){

    companion object{
        fun newInstance()=MyPageInquire()
    }

    private lateinit var binding:MypageInquireBinding

    private val mypageViewModel:MyPageViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=DataBindingUtil.inflate(
            inflater,
            R.layout.mypage_inquire,
            container,
            false
        )
        binding.fragment=this
        binding.lifecycleOwner=this
        binding.viewModel=mypageViewModel
        return binding.root
    }
}