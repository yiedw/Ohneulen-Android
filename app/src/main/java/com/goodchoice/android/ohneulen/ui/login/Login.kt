package com.goodchoice.android.ohneulen.ui.login

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.*
import android.view.animation.AlphaAnimation
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.postDelayed
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.LoginBinding
import com.goodchoice.android.ohneulen.ui.MainActivity
import com.goodchoice.android.ohneulen.util.*
import com.goodchoice.android.ohneulen.util.constant.ConstList
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class Login(private val bottomNavVisibility: Boolean, private val popBackStackName: String?) :
    Fragment(), OnBackPressedListener {

    companion object {
        fun newInstance(
            bottomNavVisibility: Boolean,
            popBackStackName: String? = null
        ) = Login(bottomNavVisibility, popBackStackName)
    }

    private lateinit var binding: LoginBinding
    private val loginViewModel: LoginViewModel by viewModel()
    private var emailCheck = false
    private var pwCheck = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Handler().postDelayed(200) {
            val animation = AlphaAnimation(0f, 1f)
            MainActivity.bottomNav.visibility = View.GONE
            MainActivity.bottomNav.animation = animation
        }
    }

    @SuppressLint("ClickableViewAccessibility")
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
        //사라지는 애니메이션을통해 최대한 자연스럽게 만든다
        val animation = AlphaAnimation(0f, 1f)

        //스와이프 기능
        binding.login.setOnTouchListener(object : OnSwipeGesture(requireContext()) {
            override fun onSwipeRight() {
                super.onSwipeRight()
                MainActivity.supportFragmentManager.popBackStack()
            }

            override fun onSingleTab(motionEvent: MotionEvent) {
                super.onSingleTab(motionEvent)
                //입력바 바깥쪽 클릭했을때 로고바 다시 보이게해주기
                binding.loginLogo.visibility = View.VISIBLE
            }
        })

        binding.fragment = this
        return binding.root
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //클리어 표시
        binding.loginEmailEt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            @SuppressLint("UseCompatLoadingForDrawables")
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!binding.loginPwEt.text.isNullOrEmpty() && !binding.loginEmailEt.text.isNullOrEmpty()) {
                    binding.loginSubmit.isEnabled = true
                    binding.loginSubmit.background =
                        requireContext().getDrawable(R.drawable.login_bt_true)
                }
                if (binding.loginEmailEt.text.isEmpty()) {
                    binding.loginEmailClear.visibility = View.GONE
                    binding.loginEmailEt.typeface =
                        Typeface.create("sans-serif-light", Typeface.NORMAL)
                } else {
                    binding.loginEmailClear.visibility = View.VISIBLE
                    binding.loginEmailEt.typeface = Typeface.create("sans-serif", Typeface.NORMAL)
                }
            }

        })

        binding.loginPwEt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                if (binding.loginPwEt.text.isEmpty()) {
                    binding.loginPwEt.typeface =
                        Typeface.create("sans-serif-light", Typeface.NORMAL)
                    binding.loginPwHidden.visibility = View.GONE
                    binding.loginPwClear.visibility = View.GONE
                } else {
                    binding.loginPwEt.typeface = Typeface.create("sans-serif", Typeface.NORMAL)
                    binding.loginPwHidden.visibility = View.VISIBLE
                    binding.loginPwClear.visibility = View.VISIBLE
                }
                if (!binding.loginPwEt.text.isNullOrEmpty() && !binding.loginEmailEt.text.isNullOrEmpty()) {
                    binding.loginSubmit.isEnabled = true
                    binding.loginSubmit.background =
                        ContextCompat.getDrawable(requireContext(), R.drawable.login_bt_true)
                }

            }

        })

        binding.loginEmailEt.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                binding.loginEmail.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.edittext_border_select)
                binding.loginLogo.visibility = View.GONE
            } else {
                binding.loginEmail.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.edittext_border)
            }
        }


        binding.loginPwEt.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                binding.loginPw.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.edittext_border_select)
                binding.loginLogo.visibility = View.GONE
            } else {
                binding.loginPw.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.edittext_border)
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
//                replaceAppbarFragment(fragment)
                MainActivity.supportFragmentManager.popBackStack()
            }
        })

        //로그인 에러 수신
        loginViewModel.loginErrorToast.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled().let { it ->
                if (it != null) {
                    if (it) {
                        Toast.makeText(requireContext(), "아이디 혹은 비밀번호가 맞지 않습니다", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        })
    }


    override fun onDestroy() {
        super.onDestroy()
        if (bottomNavVisibility) {
            MainActivity.bottomNav.visibility = View.VISIBLE
        } else {
            MainActivity.bottomNav.visibility = View.GONE
        }
        loginViewModel.loginErrorToast.postValue(Event(false))
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
        binding.loginLogo.visibility = View.VISIBLE
    }

    fun findEmailClick(view: View) {
        val intent = Intent(requireActivity(), LoginWebViewActivity::class.java)
        intent.putExtra("stat", ConstList.WEBVIEW_FIND_EMAIL)
        startActivity(intent)
        requireActivity().overridePendingTransition(
            R.anim.enter_right_to_left,
            R.anim.exit_right_to_left
        )

    }

    fun findPwClick(view: View) {
        val intent = Intent(requireActivity(), LoginWebViewActivity::class.java)
        intent.putExtra("stat", ConstList.WEBVIEW_FIND_PW)
        startActivity(intent)
        requireActivity().overridePendingTransition(
            R.anim.enter_right_to_left,
            R.anim.exit_right_to_left
        )

    }

    fun signUpClick(view: View) {
        val intent = Intent(requireActivity(), LoginWebViewActivity::class.java)
        intent.putExtra("stat", ConstList.WEBVIEW_SIGN_UP)
        startActivity(intent)
        requireActivity().overridePendingTransition(
            R.anim.enter_right_to_left,
            R.anim.exit_right_to_left
        )
    }

    fun autoTvClick(view: View) {
        binding.loginAuto.isChecked = !binding.loginAuto.isChecked
    }

    fun onBackClick(view: View) {
        MainActivity.supportFragmentManager.popBackStack()
    }

    override fun onBackPressed() {
        MainActivity.supportFragmentManager.popBackStack()
    }


}