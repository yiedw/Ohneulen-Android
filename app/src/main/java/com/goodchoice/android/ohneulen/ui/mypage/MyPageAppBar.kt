package com.goodchoice.android.ohneulen.ui.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.MypageAppbarBinding
import com.goodchoice.android.ohneulen.ui.MainActivity
import com.goodchoice.android.ohneulen.ui.home.HomeAppBar
import com.goodchoice.android.ohneulen.ui.home.Home
import com.goodchoice.android.ohneulen.ui.login.LoginViewModel
import com.goodchoice.android.ohneulen.util.OnBackPressedListener
import com.goodchoice.android.ohneulen.util.replaceAppbarFragment
import com.goodchoice.android.ohneulen.util.replaceMainFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class MyPageAppBar : Fragment() ,OnBackPressedListener{
    companion object {
        fun newInstance() = MyPageAppBar()
    }

    private lateinit var binding: MypageAppbarBinding
    private val loginViewModel:LoginViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.mypage_appbar,
            container,
            false
        )
        binding.fragment = this
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginViewModel.isLogin.observe(viewLifecycleOwner, Observer {
            if(it)
                binding.mypageAppbarLogout.visibility=View.VISIBLE
            else
                binding.mypageAppbarLogout.visibility=View.GONE
        })

    }
    fun backClick(view: View){
        MainActivity.bottomNav.visibility=View.VISIBLE
        MainActivity.bottomNav.selectedItemId=R.id.menu_bottom_nav_home
//        replaceAppbarFragment(HomeAppBar.newInstance())
//        replaceMainFragment(Home.newInstance())
    }
    fun logoutClick(view:View){
        loginViewModel.logoutTest()
    }

    override fun onBackPressed() {
        replaceAppbarFragment(HomeAppBar.newInstance())
        replaceMainFragment(Home.newInstance())
    }
}