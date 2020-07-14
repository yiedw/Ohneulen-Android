package com.goodchoice.android.ohneulen.ui.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.MypageFragmentBinding
import com.goodchoice.android.ohneulen.ui.login.Login
import com.goodchoice.android.ohneulen.ui.login.LoginAppBar
import com.goodchoice.android.ohneulen.ui.login.LoginViewModel
import com.goodchoice.android.ohneulen.util.replaceAppbarFragment
import com.goodchoice.android.ohneulen.util.replaceMainFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class MyPageFragment : Fragment() {

    companion object {
        fun newInstance() = MyPageFragment()
    }

    private lateinit var binding: MypageFragmentBinding
    private val loginViewModel: LoginViewModel by viewModel()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.mypage_fragment,
            container,
            false
        )
        binding.fragment = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        loginViewModel.test()
        loginViewModel.loginTest()

        loginViewModel.isLogin.observe(viewLifecycleOwner, Observer {
            //로그인 상태일때
            if (it) {
                binding.mypageNickName.visibility = View.VISIBLE
                binding.mypageEmail.text = "AAA@AAA.com"
            } else {
                binding.mypageNickName.visibility = View.GONE
                binding.mypageEmail.text = "로그인"
            }
        })
    }

    fun infoClick(view: View) {
        if (loginViewModel.isLogin.value!!) {
            replaceAppbarFragment(MyPageInfoAppBarFragment.newInstance())
            replaceMainFragment(MyPageInfoFragment.newInstance())
        } else {
            replaceAppbarFragment(LoginAppBar.newInstance())
            replaceMainFragment(Login.newInstance())
        }

    }

    fun goodClick(view: View) {
        replaceAppbarFragment(MyPageGoodAppBarFragment.newInstance())
        replaceMainFragment(MyPageGoodFragment.newInstance())
    }

    fun reviewClick(view: View) {
        replaceAppbarFragment(MyPageReviewAppBarFragment.newInstance())
        replaceMainFragment(MyPageReviewFragment.newInstance())
    }

    fun inquireClick(view: View) {
        replaceAppbarFragment(MyPageInquireAppBar.newInstance())
        replaceMainFragment(MyPageInquireFragment.newInstance())
    }

    fun FAQClick(view: View) {
        replaceAppbarFragment(MyPageFAQAppBar.newInstance())
        replaceMainFragment(MyPageFAQ.newInstance())
    }


}