package com.goodchoice.android.ohneulen.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.LoginAppbarBinding
import com.goodchoice.android.ohneulen.ui.MainActivity
import com.goodchoice.android.ohneulen.ui.mypage.MyPageAppBar
import com.goodchoice.android.ohneulen.ui.mypage.MyPage
import com.goodchoice.android.ohneulen.util.OnBackPressedListener
import com.goodchoice.android.ohneulen.util.addAppbarFragment
import com.goodchoice.android.ohneulen.util.replaceAppbarFragment
import com.goodchoice.android.ohneulen.util.replaceMainFragment
import timber.log.Timber

class LoginAppBar( private val fragment: Fragment) :
    Fragment(), OnBackPressedListener {
    companion object {
        fun newInstance(fragment: Fragment = MyPageAppBar.newInstance()): LoginAppBar {
                backFragmentAppBar=fragment
            return LoginAppBar( fragment)
        }

        var backFragmentAppBar=Fragment()
    }

    private lateinit var binding: LoginAppbarBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.login_appbar,
            container,
            false
        )
        binding.fragment = this
//        Timber.e("ASdf")
        return binding.root
    }

    fun backClick(view: View) {
        replaceAppbarFragment(fragment)
        MainActivity.supportFragmentManager.popBackStack()
    }

    override fun onBackPressed() {
        replaceAppbarFragment(fragment)
        MainActivity.supportFragmentManager.popBackStack()
    }
}