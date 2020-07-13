package com.goodchoice.android.ohneulen.ui.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.LoginSignUpBinding
import timber.log.Timber

class LoginSignUp : Fragment() {
    companion object {
        fun newInstance() = LoginSignUp()
    }

    private lateinit var binding: LoginSignUpBinding
    private var emailCheck = false
    private var pwCheck1 = false
    private var pwCheck2 = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.login_sign_up,
            container,
            false
        )
        binding.fragment = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //이메일 체크
        binding.loginSignUpEmail.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                if (!emailCheck)
                    Toast.makeText(requireContext(), "이메일 형식이 맞지 않습니다", Toast.LENGTH_SHORT).show()
            }
        }
        binding.loginSignUpPw1.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                if (!pwCheck1) {
                    Toast.makeText(requireContext(), "비밀번호 형식이 맞지 않습니다", Toast.LENGTH_SHORT).show()
                }
            }
        }
        binding.loginSignUpPw2.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                if (!pwCheck2) {
                    pwCheck2 = false
                    Toast.makeText(
                        requireContext(),
                        "비밀번호가 같지 않습니다",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        binding.loginSignUpEmail.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val emailRegex =
                    Regex("[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*\\.[a-zA-Z]{2,3}")
                if (binding.loginSignUpEmail.text.toString().matches(emailRegex)) {
                    emailCheck = true
                    check()
                } else {
                    binding.loginSignUpAth.isEnabled = false
                    emailCheck = false
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })

        binding.loginSignUpPw1.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val pwRegex = Regex("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}\$")
                if(binding.loginSignUpPw1.text.toString().matches(pwRegex)){
                    pwCheck1=true
                    check()
                }
                else{
                    binding.loginSignUpAth.isEnabled = false
                    pwCheck1 = false
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })

        binding.loginSignUpPw2.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val pwRegex = Regex("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}\$")
                if(binding.loginSignUpPw2.text.toString().matches(pwRegex)){
                    pwCheck2 = true
                    check()
                }
                else{
                    binding.loginSignUpAth.isEnabled = false
                    pwCheck2 = false

                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })

    }

    private fun check() {
        if (emailCheck && pwCheck1 && pwCheck2) {
            binding.loginSignUpAth.isEnabled = true
        }
    }


}