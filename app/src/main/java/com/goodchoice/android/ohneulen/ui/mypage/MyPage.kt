package com.goodchoice.android.ohneulen.ui.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.MypageBinding
import com.goodchoice.android.ohneulen.ui.MainActivity
import com.goodchoice.android.ohneulen.ui.login.Login
import com.goodchoice.android.ohneulen.ui.login.LoginAppBar
import com.goodchoice.android.ohneulen.ui.login.LoginViewModel
import com.goodchoice.android.ohneulen.util.addMainFragment
import com.goodchoice.android.ohneulen.util.popupFragment
import com.goodchoice.android.ohneulen.util.replaceAppbarFragment
import com.goodchoice.android.ohneulen.util.replaceMainFragment
import kotlinx.android.synthetic.main.login.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class MyPage : Fragment() {

    companion object {
        fun newInstance() = MyPage()
    }

    private lateinit var binding: MypageBinding
    private val loginViewModel: LoginViewModel by viewModel()
    private val myPageViewModel: MyPageViewModel by viewModel()


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


        LoginViewModel.isLogin.observe(viewLifecycleOwner, Observer {
            if (it) {
                //로그인했을때 정보 가져오기
                myPageViewModel.getMemberInfo()
            } else {
                //로그아웃이나 로그인이 풀렸을경우
                binding.mypageNickName.visibility = View.GONE
                binding.mypageEmail.visibility = View.GONE
                binding.mypageLogin.visibility = View.VISIBLE
            }
        })

        myPageViewModel.memberInfo.observe(viewLifecycleOwner, Observer {
            //로그인 상태일때
            if (LoginViewModel.isLogin.value!!) {
                binding.mypageNickName.visibility = View.VISIBLE
                binding.mypageEmail.visibility = View.VISIBLE
                binding.mypageLogin.visibility = View.GONE
            }

            binding.mypageEmail.text = it.email
            if (it.nickName.isNullOrEmpty()) {
                binding.mypageNickName.text = it.name
            } else {
                binding.mypageNickName.text = it.nickName
            }
        })
    }

    override fun onResume() {
        super.onResume()
        MainActivity.bottomNav.visibility = View.VISIBLE
    }


    override fun onDestroy() {
        super.onDestroy()
        MainActivity.bottomNav.visibility = View.GONE
    }


    fun infoClick(view: View) {
        val fragmentManager = MainActivity.supportFragmentManager.beginTransaction()
        fragmentManager.setCustomAnimations(
            R.anim.enter_right_to_left,
            R.anim.exit_right_to_left,
            R.anim.enter_left_to_right,
            R.anim.exit_left_to_right
        )
        if (!LoginViewModel.isLogin.value!!) {
            fragmentManager.addToBackStack(null)
            fragmentManager.replace(R.id.appbar_frameLayout, LoginAppBar.newInstance())
            fragmentManager.add(
                R.id.main_frameLayout,
                Login.newInstance(bottomNavVisibility = true)
            )
            fragmentManager.commit()
            return
        }

    }

    fun likeClick(view: View) {
        val fragmentManager = MainActivity.supportFragmentManager.beginTransaction()
        fragmentManager.setCustomAnimations(
            R.anim.enter_right_to_left,
            R.anim.exit_right_to_left,
            R.anim.enter_left_to_right,
            R.anim.exit_left_to_right
        )
        if (!LoginViewModel.isLogin.value!!) {
            fragmentManager.addToBackStack(null)
            fragmentManager.replace(R.id.appbar_frameLayout, LoginAppBar.newInstance())
            fragmentManager.add(
                R.id.main_frameLayout,
                Login.newInstance(bottomNavVisibility = true)
            )
            fragmentManager.commit()
            return
        }
        MainActivity.bottomNav.selectedItemId = R.id.menu_bottom_nav_like
    }

    fun recentClick(view: View) {
        val fragmentManager = MainActivity.supportFragmentManager.beginTransaction()
        fragmentManager.setCustomAnimations(
            R.anim.enter_right_to_left,
            R.anim.exit_right_to_left,
            R.anim.enter_left_to_right,
            R.anim.exit_left_to_right
        )
        fragmentManager.addToBackStack(null)
        if (!LoginViewModel.isLogin.value!!) {
            fragmentManager.replace(R.id.appbar_frameLayout, LoginAppBar.newInstance())
            fragmentManager.add(
                R.id.main_frameLayout,
                Login.newInstance(bottomNavVisibility = true)
            )
            fragmentManager.commit()
            return
        }
        fragmentManager.replace(R.id.appbar_frameLayout, MyPageRecentAppBar.newInstance())
        fragmentManager.replace(R.id.main_frameLayout, MyPageRecent.newInstance())
    }

    fun reviewClick(view: View) {
        val fragmentManager = MainActivity.supportFragmentManager.beginTransaction()
        fragmentManager.setCustomAnimations(
            R.anim.enter_right_to_left,
            R.anim.exit_right_to_left,
            R.anim.enter_left_to_right,
            R.anim.exit_left_to_right
        )
        fragmentManager.addToBackStack(null)
        if (!LoginViewModel.isLogin.value!!) {
            fragmentManager.replace(R.id.appbar_frameLayout, LoginAppBar.newInstance())
            fragmentManager.add(
                R.id.main_frameLayout,
                Login.newInstance(bottomNavVisibility = true)
            )
            fragmentManager.commit()
            return
        }
        fragmentManager.replace(R.id.appbar_frameLayout, MyPageReviewAppBar.newInstance())
        fragmentManager.replace(R.id.main_frameLayout, MyPageReview.newInstance())
        fragmentManager.commit()
    }

    fun inquireClick(view: View) {
        val fragmentManager = MainActivity.supportFragmentManager.beginTransaction()
        fragmentManager.setCustomAnimations(
            R.anim.enter_right_to_left,
            R.anim.exit_right_to_left,
            R.anim.enter_left_to_right,
            R.anim.exit_left_to_right
        )
        if (!LoginViewModel.isLogin.value!!) {
            fragmentManager.addToBackStack(null)
            fragmentManager.replace(R.id.appbar_frameLayout, LoginAppBar.newInstance())
            fragmentManager.add(
                R.id.main_frameLayout,
                Login.newInstance(bottomNavVisibility = true)
            )
            fragmentManager.commit()
            return
        }
        fragmentManager.replace(R.id.appbar_frameLayout, MyPageInquireAppBar.newInstance())
        fragmentManager.replace(R.id.main_frameLayout, MyPageInquire.newInstance())
        fragmentManager.addToBackStack(null)
        fragmentManager.commit()
    }

    fun FAQClick(view: View) {
        val fragmentManager = MainActivity.supportFragmentManager.beginTransaction()
        fragmentManager.setCustomAnimations(
            R.anim.enter_right_to_left,
            R.anim.exit_right_to_left,
            R.anim.enter_left_to_right,
            R.anim.exit_left_to_right
        )
        fragmentManager.replace(R.id.appbar_frameLayout, MyPageFAQAppBar.newInstance())
        fragmentManager.replace(R.id.main_frameLayout, MyPageFAQ.newInstance())
        fragmentManager.addToBackStack(null)
        fragmentManager.commit()
    }

    fun termsClick(view: View) {
        val fragmentManager = MainActivity.supportFragmentManager.beginTransaction()
        fragmentManager.setCustomAnimations(
            R.anim.enter_right_to_left,
            R.anim.exit_right_to_left,
            R.anim.enter_left_to_right,
            R.anim.exit_left_to_right
        )
        fragmentManager.replace(R.id.appbar_frameLayout, MyPageTermsAppBar.newInstance())
        fragmentManager.replace(R.id.main_frameLayout, MyPageTerms.newInstance())
        fragmentManager.addToBackStack(null)
        fragmentManager.commit()
    }

    fun term2Click(view: View) {
        val fragmentManager = MainActivity.supportFragmentManager.beginTransaction()
        fragmentManager.setCustomAnimations(
            R.anim.enter_right_to_left,
            R.anim.exit_right_to_left,
            R.anim.enter_left_to_right,
            R.anim.exit_left_to_right
        )
        fragmentManager.replace(R.id.appbar_frameLayout, MyPageTerms2AppBar.newInstance())
        fragmentManager.replace(R.id.main_frameLayout, MyPageTerms2.newInstance())
        fragmentManager.addToBackStack(null)
        fragmentManager.commit()
    }

    fun companyClick(view: View) {
        val fragmentManager = MainActivity.supportFragmentManager.beginTransaction()
        fragmentManager.setCustomAnimations(
            R.anim.enter_right_to_left,
            R.anim.exit_right_to_left,
            R.anim.enter_left_to_right,
            R.anim.exit_left_to_right
        )
        fragmentManager.replace(R.id.appbar_frameLayout, MyPageCompanyAppBar.newInstance())
        fragmentManager.replace(R.id.main_frameLayout, MyPageCompany.newInstance())
        fragmentManager.addToBackStack(null)
        fragmentManager.commit()
    }

    fun contactusClick(view: View) {
        val fragmentManager = MainActivity.supportFragmentManager.beginTransaction()
        fragmentManager.setCustomAnimations(
            R.anim.enter_right_to_left,
            R.anim.exit_right_to_left,
            R.anim.enter_left_to_right,
            R.anim.exit_left_to_right
        )
        fragmentManager.replace(R.id.appbar_frameLayout, MyPageContactusAppBar.newInstance())
        fragmentManager.replace(R.id.main_frameLayout, MyPageContactus.newInstance())
        fragmentManager.addToBackStack(null)
        fragmentManager.commit()
    }


}