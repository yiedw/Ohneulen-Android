package com.goodchoice.android.ohneulen.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.LoginFindAppbarBinding
import com.goodchoice.android.ohneulen.ui.mypage.MyPageAppBarFragment
import com.goodchoice.android.ohneulen.ui.mypage.MyPageFragment
import com.goodchoice.android.ohneulen.util.replaceAppbarFragment
import com.goodchoice.android.ohneulen.util.replaceMainFragment

class LoginFindAppBar : Fragment() {

    companion object {
        fun newInstance() = LoginFindAppBar()
    }

    private lateinit var binding: LoginFindAppbarBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.login_find_appbar,
            container,
            false
        )
        binding.fragment=this
        return binding.root
    }

    fun backClick(view: View){
        replaceAppbarFragment(LoginAppBar.newInstance())
        replaceMainFragment(Login.newInstance())
    }
}