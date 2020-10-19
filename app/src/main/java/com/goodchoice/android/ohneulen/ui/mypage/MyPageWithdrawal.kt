package com.goodchoice.android.ohneulen.ui.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.MypageWithdrawalBinding
import com.goodchoice.android.ohneulen.ui.MainActivity
import com.goodchoice.android.ohneulen.ui.login.LoginViewModel
import com.goodchoice.android.ohneulen.util.constant.ConstList
import org.koin.android.ext.android.inject
import timber.log.Timber

class MyPageWithdrawal : Fragment() {
    companion object {
        fun newInstance() = MyPageWithdrawal()
    }

    private lateinit var binding: MypageWithdrawalBinding
    private val mypageViewModel: MyPageViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.mypage_withdrawal,
            container,
            false
        )
        binding.fragment = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mypageViewModel.memberExitResponse.observe(viewLifecycleOwner, Observer {
            if (it.resultCode == ConstList.SUCCESS) {
                LoginViewModel.isLogin.postValue(false)
                Toast.makeText(requireContext(), "회원탈퇴가 처리 되었습니다", Toast.LENGTH_LONG).show()
                //지금까지 있던 fragment backStack clear
                MainActivity.supportFragmentManager.popBackStack(
                    null,
                    FragmentManager.POP_BACK_STACK_INCLUSIVE
                )
                MainActivity.bottomNav.selectedItemId = R.id.menu_bottom_nav_more
            }
        })
    }

    fun onSubmitClick(view: View) {
        mypageViewModel.memberExit()
    }
}