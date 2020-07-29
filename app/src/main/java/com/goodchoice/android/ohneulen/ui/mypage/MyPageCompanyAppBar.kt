package com.goodchoice.android.ohneulen.ui.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.MypageCompanyAppbarBinding
import com.goodchoice.android.ohneulen.util.replaceAppbarFragment
import com.goodchoice.android.ohneulen.util.replaceMainFragment

class MyPageCompanyAppBar :Fragment() {
    companion object{
        fun newInstance()=MyPageCompanyAppBar()
    }

    private lateinit var binding:MypageCompanyAppbarBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=DataBindingUtil.inflate(
            inflater,
            R.layout.mypage_company_appbar,
            container,
            false
        )
        return binding.root
    }

    fun backClick(view:View){
        replaceAppbarFragment(MyPageAppBar.newInstance())
        replaceMainFragment(MyPage.newInstance())
    }
}