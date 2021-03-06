package com.goodchoice.android.ohneulen.ui.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.MypageInquireNewAppbarBinding
import com.goodchoice.android.ohneulen.ui.MainActivity
import com.goodchoice.android.ohneulen.util.OnBackPressedListener
import com.goodchoice.android.ohneulen.util.replaceAppbarFragment
import com.goodchoice.android.ohneulen.util.replaceMainFragment

class MyPageInquireNewAppBar :Fragment(),OnBackPressedListener{
    companion object{
        fun newInstance()=MyPageInquireNewAppBar()
    }

    private lateinit var binding:MypageInquireNewAppbarBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=DataBindingUtil.inflate(
            inflater,
            R.layout.mypage_inquire_new_appbar,
            container,
            false
        )
        binding.fragment=this
        return binding.root
    }

    fun onCloseClick(view:View){
//        replaceAppbarFragment(MyPageInquireAppBar.newInstance())
        MainActivity.supportFragmentManager.popBackStack()
//        replaceMainFragment(MyPageInquire.newInstance())

    }

    override fun onBackPressed() {
//        replaceAppbarFragment(MyPageInquireAppBar.newInstance())
        MainActivity.supportFragmentManager.popBackStack()
//        replaceMainFragment(MyPageInquire.newInstance())
    }
}