package com.goodchoice.android.ohneulen.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.LoginFindPwBinding
import timber.log.Timber

class LoginFindPw : Fragment() {
    companion object {
        fun newInstance() = LoginFindPw()
    }

    private lateinit var binding: LoginFindPwBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.login_find_pw,
            container,
            false
        )
        binding.loginFindPwRb1.isChecked = true
        binding.loginFindPwRg.setOnCheckedChangeListener { group, checkedId ->
            if(checkedId==R.id.login_find_pw_rb1){
                binding.loginFindPwEt1.visibility=View.VISIBLE
                binding.loginFindPwBt1.visibility=View.VISIBLE
                binding.loginFindPwEt2.visibility=View.GONE
                binding.loginFindPwBt2.visibility=View.GONE
            }
            else{
                binding.loginFindPwEt1.visibility=View.GONE
                binding.loginFindPwBt1.visibility=View.GONE
                binding.loginFindPwEt2.visibility=View.VISIBLE
                binding.loginFindPwBt2.visibility=View.VISIBLE
            }
        }
        return binding.root
    }


}