package com.goodchoice.android.ohneulen.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.LoginFindBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class LoginFind : Fragment() {
    companion object {
        fun newInstance() = LoginFind()
    }

    private lateinit var binding: LoginFindBinding
    private val loginViewModel: LoginViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.login_find,
            container,
            false
        )
        binding.fragment = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fm = childFragmentManager.beginTransaction()
        if (loginViewModel.emailClick) {
            fm.replace(R.id.login_find_frameLayout, LoginFindEmail.newInstance()).commit()
            binding.loginFindEmail.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorOhneulen
                )
            )
            binding.loginFindPw.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorCGrey
                )
            )
        } else {
            fm.replace(R.id.login_find_frameLayout, LoginFindPw.newInstance()).commit()
            binding.loginFindEmail.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorCGrey
                )
            )
            binding.loginFindPw.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorOhneulen
                )
            )

        }
    }

    fun findEmailClick(view: View) {
        val fm = childFragmentManager.beginTransaction()
        fm.replace(R.id.login_find_frameLayout, LoginFindEmail.newInstance()).commit()
        binding.loginFindEmail.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.colorOhneulen
            )
        )
        binding.loginFindPw.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.colorCGrey
            )
        )
    }

    fun findPwClick(view: View) {
        val fm = childFragmentManager.beginTransaction()
        fm.replace(R.id.login_find_frameLayout, LoginFindPw.newInstance()).commit()
        binding.loginFindEmail.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.colorCGrey
            )
        )
        binding.loginFindPw.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.colorOhneulen
            )
        )
    }

}