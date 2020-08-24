package com.goodchoice.android.ohneulen.ui.login

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.LoginBinding
import com.goodchoice.android.ohneulen.ui.MainActivity
import com.goodchoice.android.ohneulen.ui.mypage.MyPageAppBar
import com.goodchoice.android.ohneulen.util.hideKeyboard
import com.goodchoice.android.ohneulen.util.replaceAppbarFragment
import com.goodchoice.android.ohneulen.util.replaceMainFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class Login(private val fragment: Fragment) : Fragment() {

    companion object {
        fun newInstance(fragment: Fragment = MyPageAppBar.newInstance()) = Login(fragment)
    }

    private lateinit var binding: LoginBinding
    private val loginViewModel: LoginViewModel by viewModel()
    private var emailCheck = false
    private var pwCheck = false

    override fun onResume() {
        super.onResume()
        MainActivity.bottomNav.visibility = View.GONE
    }

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


    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //뒤에 클릭못하게
        binding.login.setOnTouchListener { v, event -> true }

        //클리어 표시
        binding.loginEmailEt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!binding.loginPwEt.text.isNullOrEmpty() && !binding.loginEmailEt.text.isNullOrEmpty()) {
                    binding.loginSubmit.isEnabled = true
                    binding.loginSubmit.background =
                        requireContext().getDrawable(R.drawable.login_bt_true)
                }
                if (binding.loginEmailEt.text.isNotBlank()) {
                    binding.loginEmailClear.visibility = View.VISIBLE
                } else {
                    binding.loginEmailClear.visibility = View.GONE
                }
            }

        })

        binding.loginPwEt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (binding.loginPwEt.text.length == 1) {
                    binding.loginPwHidden.visibility = View.VISIBLE

                }
                if (!binding.loginPwEt.text.isNullOrEmpty() && !binding.loginEmailEt.text.isNullOrEmpty()) {
                    binding.loginSubmit.isEnabled = true
                    binding.loginSubmit.background =
                        requireContext().getDrawable(R.drawable.login_bt_true)
                }

                if (binding.loginPwEt.text.isNotBlank()) {
                    binding.loginPwClear.visibility = View.VISIBLE

                } else {
                    binding.loginPwClear.visibility = View.GONE

                }
            }

        })

        binding.loginEmailEt.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                binding.loginEmail.background =
                    requireContext().getDrawable(R.drawable.edittext_border_select)
                binding.loginLogo.visibility = View.GONE
            } else {
                binding.loginEmail.background =
                    requireContext().getDrawable(R.drawable.edittext_border)
            }
        }


        binding.loginPwEt.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                binding.loginPw.background =
                    requireContext().getDrawable(R.drawable.edittext_border_select)
                binding.loginLogo.visibility = View.GONE
            } else {
                binding.loginPw.background =
                    requireContext().getDrawable(R.drawable.edittext_border)
            }
        }
//
        //비밀번호 엔터 눌렀을 때 -> 로그인까지
        binding.loginPwEt.setOnKeyListener { v, keyCode, event ->
            if ((event.action == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                binding.loginPwEt.clearFocus()
                hideKeyboard(v, requireContext())
                submitClick(v)
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }

        //로그인 성공
        LoginViewModel.isLogin.observe(viewLifecycleOwner, Observer {
            if (it) {
                replaceAppbarFragment(fragment)
                MainActivity.supportFragmentManager.popBackStack()
            }
        })

        //로그인 에러 수신
        loginViewModel.loginErrorToast.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled().let {
                Toast.makeText(requireContext(), "아이디 혹은 비밀번호가 맞지 않습니다", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        MainActivity.bottomNav.visibility = View.VISIBLE
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
//        replaceAppbarFragment(LoginSignUpAppBar.newInstance())
        replaceMainFragment(LoginSignUp.newInstance())
    }

    fun autoTvClick(view: View) {
        binding.loginAuto.isChecked = !binding.loginAuto.isChecked
    }


}