package com.goodchoice.android.ohneulen.ui.login

import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.LoginBinding
import com.goodchoice.android.ohneulen.util.replaceAppbarFragment
import com.goodchoice.android.ohneulen.util.replaceMainFragment
import org.koin.android.ext.android.bind
import org.koin.androidx.viewmodel.ext.android.viewModel

class Login : Fragment() {

    companion object {
        fun newInstance() = Login()
    }

    private lateinit var binding: LoginBinding
    private val loginViewModel: LoginViewModel by viewModel()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.login,
            container,
            false
        )
        binding.apply {
            fragment = this@Login
            viewModel = loginViewModel
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.loginEmailEt.setOnFocusChangeListener { v, hasFocus ->

            if (!hasFocus and !binding.loginEmailEt.text.isNullOrEmpty()) {
                val emailRegex =
                    Regex("[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*\\.[a-zA-Z]{2,3}")
                if (binding.loginEmailEt.text.matches(emailRegex)) {
                    binding.loginEmailTv.text = requireContext().getString(R.string.email_tv_basic)
                    binding.loginEmailTv.setTextColor(requireContext().getColor(R.color.colorWeightGrey))
                    binding.loginEmailCheck.visibility = View.VISIBLE
                    binding.loginEmailClear.visibility = View.GONE
                } else {
                    binding.loginEmailTv.text = requireContext().getString(R.string.email_tv_fail)
                    binding.loginEmailTv.setTextColor(requireContext().getColor(R.color.colorRed))
                }
            }
        }

        binding.loginPwEt.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus and !binding.loginPwEt.text.isNullOrEmpty()) {
                val pwRegex = Regex("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$")
                if (binding.loginPwEt.text.matches(pwRegex)) {
                    binding.loginPwTv.text = requireContext().getString(R.string.pw_tv_basic)
                    binding.loginPwTv.setTextColor(requireContext().getColor(R.color.colorWeightGrey))
                    binding.loginPwCheck.visibility = View.VISIBLE
                    binding.loginPwClear.visibility = View.GONE
                    binding.loginPwHidden.visibility = View.GONE
                } else {
                    binding.loginPwTv.text = requireContext().getString(R.string.pw_tv_fail)
                    binding.loginPwTv.setTextColor(requireContext().getColor(R.color.colorRed))
                }
            }
        }
    }

    //    fun emailClear(view: View) {
//        binding.loginEmail.text.clear()
//    }
//
//    fun pwClear(view: View) {
//        binding.loginPw.text.clear()
//    }
//
    fun submitClick(view: View) {
    }

    fun findEmailClick(view: View) {
        loginViewModel.emailClick = true
        replaceAppbarFragment(LoginFindAppBar.newInstance())
        replaceMainFragment(LoginFind.newInstance())
    }

    fun findPwClick(view: View) {
        loginViewModel.emailClick = false
        replaceAppbarFragment(LoginFindAppBar.newInstance())
        replaceMainFragment(LoginFind.newInstance())
    }

    fun signUpClick(view: View) {
        replaceAppbarFragment(LoginSignUpAppBar.newInstance())
        replaceMainFragment(LoginSignUp.newInstance())
    }


}