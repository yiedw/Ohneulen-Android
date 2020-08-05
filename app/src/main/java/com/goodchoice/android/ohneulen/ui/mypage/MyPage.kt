package com.goodchoice.android.ohneulen.ui.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.MypageBinding
import com.goodchoice.android.ohneulen.ui.MainActivity
import com.goodchoice.android.ohneulen.ui.login.Login
import com.goodchoice.android.ohneulen.ui.login.LoginAppBar
import com.goodchoice.android.ohneulen.ui.login.LoginViewModel
import com.goodchoice.android.ohneulen.util.replaceAppbarFragment
import com.goodchoice.android.ohneulen.util.replaceMainFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class MyPage : Fragment() {

    companion object {
        fun newInstance() = MyPage()
    }

    private lateinit var binding: MypageBinding
    private val loginViewModel: LoginViewModel by viewModel()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.mypage,
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
                binding.mypageEmail.visibility = View.VISIBLE
                binding.mypageLogin.visibility = View.GONE
                binding.mypageEmail.text = "AAA@AAA.com"
            } else {
                binding.mypageNickName.visibility = View.GONE
                binding.mypageEmail.visibility = View.GONE
                binding.mypageLogin.visibility = View.VISIBLE
            }
        })
    }



    fun infoClick(view: View) {
        if (loginViewModel.isLogin.value!!) {
            replaceAppbarFragment(MyPageInfoAppBar.newInstance())
            replaceMainFragment(MyPageInfo.newInstance())
        } else {
            replaceAppbarFragment(LoginAppBar.newInstance())
            replaceMainFragment(Login.newInstance())
        }

    }

    fun likeClick(view: View) {
        if (binding.mypageNickName.visibility == View.GONE) {
            Toast.makeText(requireContext(), "로그인 하시오", Toast.LENGTH_SHORT).show()
            return
        }
//        MainActivity.bottomNav.selectedItemId = R.id.menu_bottom_nav_like
        replaceAppbarFragment(MyPageLikeAppBar.newInstance())
        replaceMainFragment(MyPageLike.newInstance())
    }

    fun recentClick(view: View) {
        if (binding.mypageNickName.visibility == View.GONE) {
            Toast.makeText(requireContext(), "로그인 하시오", Toast.LENGTH_SHORT).show()
            return
        }
        replaceAppbarFragment(MyPageRecentAppBar.newInstance())
        replaceMainFragment(MyPageRecent.newInstance())
    }

    fun reviewClick(view: View) {
        if (binding.mypageNickName.visibility == View.GONE) {
            Toast.makeText(requireContext(), "로그인 하시오", Toast.LENGTH_SHORT).show()
            return
        }
        replaceAppbarFragment(MyPageReviewAppBar.newInstance())
        replaceMainFragment(MyPageReview.newInstance())
    }

    fun inquireClick(view: View) {
        if (binding.mypageNickName.visibility == View.GONE) {
            Toast.makeText(requireContext(), "로그인 하시오", Toast.LENGTH_SHORT).show()
            return
        }
        replaceAppbarFragment(MyPageInquireAppBar.newInstance())
        replaceMainFragment(MyPageInquire.newInstance())
    }

    fun FAQClick(view: View) {

        replaceAppbarFragment(MyPageFAQAppBar.newInstance())
        replaceMainFragment(MyPageFAQ.newInstance())
    }

    fun termsClick(view: View) {
        replaceAppbarFragment(MyPageTermsAppBar.newInstance())
        replaceMainFragment(MyPageTerms.newInstance())
    }

    fun companyClick(view: View) {
        replaceAppbarFragment(MyPageCompanyAppBar.newInstance())
        replaceMainFragment(MyPageCompany.newInstance())
    }

    fun contactusClick(view: View) {
        replaceAppbarFragment(MyPageContactusAppBar.newInstance())
        replaceMainFragment(MyPageContactus.newInstance())
    }


}