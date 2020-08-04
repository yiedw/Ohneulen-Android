package com.goodchoice.android.ohneulen.ui.login

import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.LoginBinding
import com.goodchoice.android.ohneulen.ui.MainActivity
import com.goodchoice.android.ohneulen.util.replaceAppbarFragment
import com.goodchoice.android.ohneulen.util.replaceMainFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class Login : Fragment() {

    companion object {
        fun newInstance() = Login()
    }

    private lateinit var binding: LoginBinding
    private val loginViewModel: LoginViewModel by viewModel()
    private var emailCheck = false
    private var pwCheck = false
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

        MainActivity.bottomNav.visibility = View.GONE


        //이메일 검사
        binding.loginEmailEt.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus and !binding.loginEmailEt.text.isNullOrEmpty()) {
                val emailRegex =
                    Regex("[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*\\.[a-zA-Z]{2,3}")
                if (binding.loginEmailEt.text.matches(emailRegex)) {
                    binding.loginEmailTv.text = requireContext().getString(R.string.email_tv_basic)
                    binding.loginEmailTv.setTextColor(requireContext().getColor(R.color.colorWeightGrey))
                    binding.loginEmailCheck.visibility = View.VISIBLE
                    binding.loginEmailClear.visibility = View.GONE
                    emailCheck = true
                    if (pwCheck) {
                        binding.loginSubmit.isEnabled = pwCheck
                        binding.loginSubmit.background =
                            requireContext().getDrawable(R.drawable.login_bt_true)
                    }
                } else {
                    binding.loginEmailTv.text = requireContext().getString(R.string.email_tv_fail)
                    binding.loginEmailTv.setTextColor(requireContext().getColor(R.color.colorRed))
                    binding.loginEmailCheck.visibility = View.GONE
                    binding.loginEmailClear.visibility = View.VISIBLE
                    emailCheck = false
                }
            }
            if (hasFocus) {
                binding.loginEmail.background =
                    requireContext().getDrawable(R.drawable.edittext_border_select)
                binding.loginLogo.visibility = View.GONE
            } else {
                binding.loginEmail.background =
                    requireContext().getDrawable(R.drawable.edittext_border)
            }
        }


        //비밀번호 검사
        binding.loginPwEt.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus and !binding.loginPwEt.text.isNullOrEmpty()) {
                val pwRegex = Regex("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$")
                if (binding.loginPwEt.text.matches(pwRegex)) {
                    binding.loginPwTv.text = requireContext().getString(R.string.pw_tv_basic)
                    binding.loginPwTv.setTextColor(requireContext().getColor(R.color.colorWeightGrey))
                    binding.loginPwCheck.visibility = View.VISIBLE
                    binding.loginPwClear.visibility = View.GONE
                    binding.loginPwHidden.visibility = View.GONE
                    pwCheck = true

                    if (emailCheck) {
                        binding.loginSubmit.isEnabled = emailCheck
                        binding.loginSubmit.background =
                            requireContext().getDrawable(R.drawable.login_bt_true)
                    }

                } else {
                    binding.loginPwTv.text = requireContext().getString(R.string.pw_tv_fail)
                    binding.loginPwTv.setTextColor(requireContext().getColor(R.color.colorRed))
                    binding.loginPwHidden.visibility = View.VISIBLE
                    binding.loginPwClear.visibility = View.VISIBLE
                    binding.loginPwCheck.visibility = View.GONE
                    pwCheck = false
                }
            }
            if (hasFocus) {
                binding.loginPw.background =
                    requireContext().getDrawable(R.drawable.edittext_border_select)
                binding.loginLogo.visibility = View.GONE
            } else {
                binding.loginPw.background =
                    requireContext().getDrawable(R.drawable.edittext_border)
            }
        }

        //비밀번호 엔터 눌렀을 때
        binding.loginPwEt.setOnKeyListener { v, keyCode, event ->
            if ((event.action == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                binding.loginPwEt.clearFocus()
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }
    }


    fun emailClear(view: View) {
        binding.loginEmailEt.text.clear()
    }

    fun pwClear(view: View) {
        binding.loginPwEt.text.clear()
    }

    fun pwHidden(view: View) {
        if (binding.loginPwHidden.visibility == View.VISIBLE) {
            binding.loginPwHidden.visibility = View.GONE
            binding.loginPwOpen.visibility = View.VISIBLE
            binding.loginPwEt.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        } else {
            binding.loginPwHidden.visibility = View.VISIBLE
            binding.loginPwOpen.visibility = View.GONE
            binding.loginPwEt.inputType =
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD

        }
    }

    fun submitClick(view: View) {
        loginViewModel.login(
            binding.loginEmailEt.text.toString(),
            binding.loginPwEt.text.toString(),
            binding.loginAuto.isChecked
        )
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

    fun autoTvClick(view: View) {
        binding.loginAuto.isChecked = !binding.loginAuto.isChecked
    }


}