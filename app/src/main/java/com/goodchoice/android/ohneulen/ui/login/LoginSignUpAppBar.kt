package com.goodchoice.android.ohneulen.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.LoginSignUpAppbarBinding
import com.goodchoice.android.ohneulen.util.replaceAppbarFragment
import com.goodchoice.android.ohneulen.util.replaceMainFragment

class LoginSignUpAppBar:Fragment() {
    companion object{
        fun newInstance()=LoginSignUpAppBar()
    }

    private lateinit var binding:LoginSignUpAppbarBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=DataBindingUtil.inflate(
            inflater,
            R.layout.login_sign_up_appbar,
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