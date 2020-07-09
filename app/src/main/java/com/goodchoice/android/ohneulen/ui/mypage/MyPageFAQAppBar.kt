package com.goodchoice.android.ohneulen.ui.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.MypageFaqAppbarBinding
import com.goodchoice.android.ohneulen.util.replaceAppbarFragment
import com.goodchoice.android.ohneulen.util.replaceMainFragment

class MyPageFAQAppBar :Fragment(){
    companion object{
        fun newInstance()=MyPageFAQAppBar()
    }

    private lateinit var binding:MypageFaqAppbarBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=DataBindingUtil.inflate(
            inflater,
            R.layout.mypage_faq_appbar,
            container,
            false
        )
        binding.fragment=this
        return binding.root
    }

    fun backClick(view:View){
        replaceAppbarFragment(MyPageAppBarFragment.newInstance())
        replaceMainFragment(MyPageFragment.newInstance())
    }
}