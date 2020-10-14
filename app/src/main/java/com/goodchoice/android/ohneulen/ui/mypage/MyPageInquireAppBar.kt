package com.goodchoice.android.ohneulen.ui.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.MypageInquireAppbarBinding
import com.goodchoice.android.ohneulen.ui.MainActivity
import com.goodchoice.android.ohneulen.ui.login.Login
import com.goodchoice.android.ohneulen.ui.login.LoginAppBar
import com.goodchoice.android.ohneulen.ui.login.LoginViewModel
import com.goodchoice.android.ohneulen.util.OnBackPressedListener
import com.goodchoice.android.ohneulen.util.popupFragment
import com.goodchoice.android.ohneulen.util.replaceAppbarFragment
import com.goodchoice.android.ohneulen.util.replaceMainFragment

class MyPageInquireAppBar :Fragment() ,OnBackPressedListener{

    companion object{
        fun newInstance()=MyPageInquireAppBar()
    }

    private lateinit var binding:MypageInquireAppbarBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=DataBindingUtil.inflate(
            inflater,
            R.layout.mypage_inquire_appbar,
            container,
            false
        )
        binding.fragment=this
        return binding.root
    }

    fun newClick(view:View){
        val fragmentManager = MainActivity.supportFragmentManager.beginTransaction()
        fragmentManager.setCustomAnimations(
            R.anim.enter_right_to_left,
            R.anim.exit_right_to_left,
            R.anim.enter_left_to_right,
            R.anim.exit_left_to_right
        )
        if (!LoginViewModel.isLogin.value!!) {
            fragmentManager.addToBackStack(null)
            fragmentManager.replace(R.id.appbar_frameLayout, LoginAppBar.newInstance())
            fragmentManager.replace(
                R.id.main_frameLayout,
                Login.newInstance(bottomNavVisibility = true)
            )
            fragmentManager.commit()
            return
        }
        fragmentManager.addToBackStack(null)
        fragmentManager.replace(R.id.appbar_frameLayout,MyPageInquireNewAppBar.newInstance())
        fragmentManager.replace(R.id.main_frameLayout,MyPageInquireNew.newInstance())
        fragmentManager.commit()
//        replaceMainFragment(MyPageInquireNew.newInstance())
    }

    fun backClick(view:View){
        MainActivity.supportFragmentManager.popBackStack()
    }
    override fun onBackPressed() {
        MainActivity.supportFragmentManager.popBackStack()
    }
}