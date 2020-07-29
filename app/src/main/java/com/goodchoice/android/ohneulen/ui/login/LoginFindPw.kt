package com.goodchoice.android.ohneulen.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
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
            if (checkedId == R.id.login_find_pw_rb1) {
                binding.loginFindPwLinearLayout1.visibility = View.VISIBLE
                binding.loginFindPwLinearLayout2.visibility = View.GONE
            } else {
                binding.loginFindPwLinearLayout1.visibility = View.GONE
                binding.loginFindPwLinearLayout2.visibility = View.VISIBLE
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.loginFindPwPhoneEt.setOnFocusChangeListener { v, hasFocus ->

            if (hasFocus) {
                binding.loginFindPwPhone.background =
                    requireContext().getDrawable(R.drawable.edittext_border_select)
            } else {
                binding.loginFindPwPhone.background =
                    requireContext().getDrawable(R.drawable.edittext_border)
                if (!binding.loginFindPwPhoneEt.text.isNullOrEmpty()) {
                    val emailRegex =
                        Regex("[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*\\.[a-zA-Z]{2,3}")
                    if (binding.loginFindPwPhoneEt.text.matches(emailRegex)) {
                        binding.apply {
                            loginFindPwPhoneCheck.visibility = View.VISIBLE
                            loginFindPwPhoneClear.visibility = View.GONE
                            loginFindPwPhoneBt.isEnabled = true
                            loginFindPwPhoneBt.background=
                                ContextCompat.getDrawable(requireContext(),R.drawable.background_rounding_green)
                        }
                    } else {
                        binding.apply {
                            loginFindPwPhoneCheck.visibility = View.GONE
                            loginFindPwPhoneClear.visibility = View.VISIBLE
                            loginFindPwPhoneBt.isEnabled = false
                            loginFindPwPhoneBt.background=
                                ContextCompat.getDrawable(requireContext(),R.drawable.background_rounding_cgrey)
                        }
                    }

                }
            }
        }

        binding.loginFindPwEmailEt.setOnFocusChangeListener { v, hasFocus ->

            if (hasFocus) {
                binding.loginFindPwEmail.background =
                    requireContext().getDrawable(R.drawable.edittext_border_select)
            } else {
                binding.loginFindPwEmail.background =
                    requireContext().getDrawable(R.drawable.edittext_border)
                if (!binding.loginFindPwEmailEt.text.isNullOrEmpty()) {
                    val emailRegex =
                        Regex("[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*\\.[a-zA-Z]{2,3}")
                    if(binding.loginFindPwEmailEt.text.matches(emailRegex)){
                        binding.apply{
                            loginFindPwEmailCheck.visibility = View.VISIBLE
                            loginFindPwEmailClear.visibility = View.GONE
                            loginFindPwEmailBt.isEnabled = true
                            loginFindPwEmailBt.background=
                                ContextCompat.getDrawable(requireContext(),R.drawable.background_rounding_green)
                        }
                    }
                    else{
                        binding.apply {
                            loginFindPwEmailCheck.visibility = View.GONE
                            loginFindPwEmailClear.visibility = View.VISIBLE
                            loginFindPwEmailBt.isEnabled = false
                            loginFindPwEmailBt.background=
                                ContextCompat.getDrawable(requireContext(),R.drawable.background_rounding_cgrey)
                        }
                    }
                }
            }
        }
    }


}