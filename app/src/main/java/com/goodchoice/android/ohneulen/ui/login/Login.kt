package com.goodchoice.android.ohneulen.ui.login

import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.LoginBinding
import com.goodchoice.android.ohneulen.ui.mypage.MyPageFragment
import com.goodchoice.android.ohneulen.util.replaceAppbarFragment
import com.goodchoice.android.ohneulen.util.replaceMainFragment
import kotlinx.android.synthetic.main.login.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

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


        loginViewModel.loginErrorToast.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                val toast =
                    Toast.makeText(requireContext(), "아이디 혹은 비밀번호가 틀립니다", Toast.LENGTH_SHORT)
                toast.setGravity(
                    Gravity.CENTER_HORIZONTAL or Gravity.TOP,
                    0,
                    binding.loginEmail.y.toInt() - 30
                )
                toast.show()
            }
        })
        binding.loginPw.setOnEditorActionListener { v, actionId, _->
            if (v!!.id == R.id.login_pw && actionId == EditorInfo.IME_ACTION_DONE) {
                val emailRegex =
                    Regex("[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*\\.[a-zA-Z]{2,3}")
                if (binding.loginEmail.text.matches(emailRegex)) {
                    loginViewModel.login(binding.loginAuto.isChecked)
                } else {
                    val toast = Toast.makeText(requireContext(), "이메일 형식이 잘못됨", Toast.LENGTH_SHORT)
                    toast.setGravity(
                        Gravity.CENTER_HORIZONTAL or Gravity.TOP,
                        0,
                        binding.loginEmail.y.toInt() - 30
                    )
                    toast.show()
                }
            }
            return@setOnEditorActionListener false
        }
    }

    fun emailClear(view: View) {
        binding.loginEmail.text.clear()
    }

    fun pwClear(view: View) {
        binding.loginPw.text.clear()
    }

    fun submitClick(view: View) {
        val emailRegex =
            Regex("[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*\\.[a-zA-Z]{2,3}")
        if (binding.loginEmail.text.matches(emailRegex)) {
            loginViewModel.login(binding.loginAuto.isChecked)
        } else {
            val toast = Toast.makeText(requireContext(), "이메일 형식이 잘못됨", Toast.LENGTH_SHORT)
            toast.setGravity(
                Gravity.CENTER_HORIZONTAL or Gravity.TOP,
                0,
                binding.loginEmail.y.toInt() - 30
            )
            toast.show()
        }
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