package com.goodchoice.android.ohneulen.ui.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.MypageTermsAppbarBinding
import com.goodchoice.android.ohneulen.util.OnBackPressedListener
import com.goodchoice.android.ohneulen.util.replaceAppbarFragment
import com.goodchoice.android.ohneulen.util.replaceMainFragment
import timber.log.Timber

class MyPageTermsAppBar :Fragment(),OnBackPressedListener{

    companion object{
        fun newInstance()=MyPageTermsAppBar()
    }

    private lateinit var binding:MypageTermsAppbarBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=DataBindingUtil.inflate(
            inflater,
            R.layout.mypage_terms_appbar,
            container,
            false
        )
        binding.fragment=this
        return binding.root
    }

    fun onBackClick(view:View){
        replaceMainFragment(MyPage.newInstance())
        replaceAppbarFragment(MyPageAppBar.newInstance())
    }

    override fun onBackPressed() {
        replaceMainFragment(MyPage.newInstance())
        replaceAppbarFragment(MyPageAppBar.newInstance())
    }

}