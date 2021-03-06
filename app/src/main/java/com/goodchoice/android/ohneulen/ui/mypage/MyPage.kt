package com.goodchoice.android.ohneulen.ui.mypage

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.animation.AlphaAnimation
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.MypageBinding
import com.goodchoice.android.ohneulen.ui.MainActivity
import com.goodchoice.android.ohneulen.ui.login.Login
import com.goodchoice.android.ohneulen.ui.login.LoginAppBar
import com.goodchoice.android.ohneulen.ui.login.LoginViewModel
import com.goodchoice.android.ohneulen.util.OnBackPressedListener
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class MyPage : Fragment(),OnBackPressedListener {

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
            if (!it) {
                //로그아웃이나 로그인이 풀렸을경우
                binding.mypageNickName.visibility = View.GONE
                binding.mypageEmail.visibility = View.GONE
                binding.mypageLogin.visibility = View.VISIBLE
            }
//            else {
//                //로그인했을때 정보 가져오기 -> 로그인 뷰모델에서 처리
////                loginViewModel.getMemberInfo()
//            }
        })

        loginViewModel.memberInfo.observe(viewLifecycleOwner, Observer {
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
        val animation = AlphaAnimation(0f, 1f)
        MainActivity.bottomNav.visibility = View.VISIBLE
        MainActivity.bottomNav.animation = animation
    }



    override fun onDestroy() {
        super.onDestroy()
        //바텀바 숨기기
        val animation = AlphaAnimation(0f, 1f)
        MainActivity.bottomNav.visibility = View.GONE
        MainActivity.bottomNav.animation = animation

    }


    fun onInfoClick(view: View) {
        val fragmentManager = MainActivity.supportFragmentManager.beginTransaction()
        fragmentManager.setCustomAnimations(
            R.anim.enter_right_to_left,
            R.anim.exit_right_to_left,
            R.anim.enter_left_to_right,
            R.anim.exit_left_to_right
        )
        if (!LoginViewModel.isLogin.value!!) {
            fragmentManager.addToBackStack(null)
//            fragmentManager.replace(R.id.appbar_frameLayout, LoginAppBar.newInstance())
            fragmentManager.replace(
                R.id.main_frameLayout,
                Login.newInstance(bottomNavVisibility = true)
            )
            fragmentManager.commit()
            return
        }
        fragmentManager.addToBackStack(null)
//        fragmentManager.replace(R.id.appbar_frameLayout,MyPageInfoAppBar.newInstance())
        fragmentManager.replace(R.id.main_frameLayout,MyPageInfo.newInstance())
        fragmentManager.commit()

    }

    fun onLikeClick(view: View) {
        val fragmentManager = MainActivity.supportFragmentManager.beginTransaction()
        fragmentManager.setCustomAnimations(
            R.anim.enter_right_to_left,
            R.anim.exit_right_to_left,
            R.anim.enter_left_to_right,
            R.anim.exit_left_to_right
        )
        if (!LoginViewModel.isLogin.value!!) {
            fragmentManager.addToBackStack(null)
//            fragmentManager.replace(R.id.appbar_frameLayout, LoginAppBar.newInstance())
            fragmentManager.replace(
                R.id.main_frameLayout,
                Login.newInstance(bottomNavVisibility = true)
            )
            fragmentManager.commit()
            return
        }
        MainActivity.bottomNav.selectedItemId = R.id.menu_bottom_nav_like
    }

    fun onRecentClick(view: View) {
        val fragmentManager = MainActivity.supportFragmentManager.beginTransaction()
        fragmentManager.setCustomAnimations(
            R.anim.enter_right_to_left,
            R.anim.exit_right_to_left,
            R.anim.enter_left_to_right,
            R.anim.exit_left_to_right
        )
        fragmentManager.addToBackStack(null)
        if (!LoginViewModel.isLogin.value!!) {
            fragmentManager.replace(
                R.id.main_frameLayout,
                Login.newInstance(bottomNavVisibility = true)
            )
            fragmentManager.commit()
            return
        }
        fragmentManager.replace(R.id.main_frameLayout, MyPageRecent.newInstance())
    }

    fun onReviewClick(view: View) {
        val fragmentManager = MainActivity.supportFragmentManager.beginTransaction()
        fragmentManager.setCustomAnimations(
            R.anim.enter_right_to_left,
            R.anim.exit_right_to_left,
            R.anim.enter_left_to_right,
            R.anim.exit_left_to_right
        )
        fragmentManager.addToBackStack(null)
        if (!LoginViewModel.isLogin.value!!) {
            fragmentManager.replace(
                R.id.main_frameLayout,
                Login.newInstance(bottomNavVisibility = true)
            )
            fragmentManager.commit()
            return
        }
        fragmentManager.replace(R.id.main_frameLayout, MyPageReview.newInstance())
        fragmentManager.commit()
    }

    fun onInquireClick(view: View) {
        val fragmentManager = MainActivity.supportFragmentManager.beginTransaction()
        fragmentManager.setCustomAnimations(
            R.anim.enter_right_to_left,
            R.anim.exit_right_to_left,
            R.anim.enter_left_to_right,
            R.anim.exit_left_to_right
        )
        if (!LoginViewModel.isLogin.value!!) {
            fragmentManager.addToBackStack(null)
            fragmentManager.replace(
                R.id.main_frameLayout,
                Login.newInstance(bottomNavVisibility = true)
            )
            fragmentManager.commit()
            return
        }
        fragmentManager.replace(R.id.main_frameLayout, MyPageInquire.newInstance())
        fragmentManager.addToBackStack(null)
        fragmentManager.commit()
    }

    fun onFAQClick(view: View) {
        val fragmentManager = MainActivity.supportFragmentManager.beginTransaction()
        fragmentManager.setCustomAnimations(
            R.anim.enter_right_to_left,
            R.anim.exit_right_to_left,
            R.anim.enter_left_to_right,
            R.anim.exit_left_to_right
        )
        fragmentManager.replace(R.id.main_frameLayout, MyPageFAQ.newInstance())
        fragmentManager.addToBackStack(null)
        fragmentManager.commit()
    }

    fun onTermsClick(view: View) {
        val fragmentManager = MainActivity.supportFragmentManager.beginTransaction()
        fragmentManager.setCustomAnimations(
            R.anim.enter_right_to_left,
            R.anim.exit_right_to_left,
            R.anim.enter_left_to_right,
            R.anim.exit_left_to_right
        )
        fragmentManager.replace(R.id.main_frameLayout, MyPageTerms.newInstance())
        fragmentManager.addToBackStack(null)
        fragmentManager.commit()
    }

    fun onTerm2Click(view: View) {
        val fragmentManager = MainActivity.supportFragmentManager.beginTransaction()
        fragmentManager.setCustomAnimations(
            R.anim.enter_right_to_left,
            R.anim.exit_right_to_left,
            R.anim.enter_left_to_right,
            R.anim.exit_left_to_right
        )
        fragmentManager.replace(R.id.main_frameLayout, MyPageTerms2.newInstance())
        fragmentManager.addToBackStack(null)
        fragmentManager.commit()
    }

    fun onCompanyClick(view: View) {
        val fragmentManager = MainActivity.supportFragmentManager.beginTransaction()
        fragmentManager.setCustomAnimations(
            R.anim.enter_right_to_left,
            R.anim.exit_right_to_left,
            R.anim.enter_left_to_right,
            R.anim.exit_left_to_right
        )
        fragmentManager.replace(R.id.main_frameLayout, MyPageCompany.newInstance())
        fragmentManager.addToBackStack(null)
        fragmentManager.commit()
    }

    fun onContactUsClick(view: View) {
        val fragmentManager = MainActivity.supportFragmentManager.beginTransaction()
        fragmentManager.setCustomAnimations(
            R.anim.enter_right_to_left,
            R.anim.exit_right_to_left,
            R.anim.enter_left_to_right,
            R.anim.exit_left_to_right
        )
        fragmentManager.replace(R.id.main_frameLayout, MyPageContactus.newInstance())
        fragmentManager.addToBackStack(null)
        fragmentManager.commit()
    }

    fun onLogoutClick(view:View){
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.logout_dialog)
        dialog.findViewById<Button>(R.id.logout_dialog_cancel).setOnClickListener {
            dialog.dismiss()
        }
        dialog.findViewById<Button>(R.id.logout_dialog_ok).setOnClickListener {
            loginViewModel.logout()
            dialog.dismiss()

        }
        dialog.show()
    }
    override fun onBackPressed() {
        MainActivity.bottomNav.selectedItemId = R.id.menu_bottom_nav_home
    }


}