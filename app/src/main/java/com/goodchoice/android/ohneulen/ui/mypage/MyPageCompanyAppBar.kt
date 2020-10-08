package com.goodchoice.android.ohneulen.ui.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.MainActivityBinding
import com.goodchoice.android.ohneulen.databinding.MypageCompanyAppbarBinding
import com.goodchoice.android.ohneulen.ui.MainActivity
import com.goodchoice.android.ohneulen.util.OnBackPressedListener
import com.goodchoice.android.ohneulen.util.replaceAppbarFragment
import com.goodchoice.android.ohneulen.util.replaceMainFragment
import timber.log.Timber

class MyPageCompanyAppBar :Fragment(),OnBackPressedListener {
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
        binding.fragment=this
        return binding.root
    }

    fun backClick(view:View){
        MainActivity.supportFragmentManager.popBackStack()
    }

    override fun onBackPressed() {
        MainActivity.supportFragmentManager.popBackStack()
    }
}