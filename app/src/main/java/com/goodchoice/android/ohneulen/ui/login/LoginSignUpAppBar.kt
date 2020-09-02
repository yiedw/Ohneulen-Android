package com.goodchoice.android.ohneulen.ui.login

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.LoginSignUpAppbarBinding
import com.goodchoice.android.ohneulen.ui.MainActivity
import com.goodchoice.android.ohneulen.ui.mypage.MyPage
import com.goodchoice.android.ohneulen.ui.mypage.MyPageAppBar
import com.goodchoice.android.ohneulen.util.*

class LoginSignUpAppBar(private val fragment: Fragment) : Fragment() {
    companion object {
        fun newInstance(fragment: Fragment=MyPageAppBar.newInstance()) = LoginSignUpAppBar(fragment)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        MainActivity.appbarFrameLayout.visibility=View.GONE
    }

    private lateinit var binding: LoginSignUpAppbarBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.login_sign_up_appbar,
            container,
            false
        )
        binding.fragment = this
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        MainActivity.appbarFrameLayout.visibility=View.VISIBLE
    }


}