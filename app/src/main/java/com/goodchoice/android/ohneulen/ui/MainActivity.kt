package com.goodchoice.android.ohneulen.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.ui.home.HomeFragment
import com.goodchoice.android.ohneulen.ui.home.HomeAppBarFragment
import com.goodchoice.android.ohneulen.ui.login.LoginViewModel
import com.goodchoice.android.ohneulen.util.replaceAppbarFragment
import com.goodchoice.android.ohneulen.util.replaceMainFragment
import kotlinx.android.synthetic.main.main_activity.*
import kotlinx.coroutines.delay
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    companion object {
        lateinit var supportFragmentManager: FragmentManager
        lateinit var appbarFrameLayout: FrameLayout
        lateinit var mainFrameLayout: FrameLayout
    }

    private val mainViewModel: MainViewModel by viewModel()


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        Companion.supportFragmentManager = supportFragmentManager
        appbarFrameLayout = appbar_frameLayout
        mainFrameLayout = main_frameLayout
        appbarFrameLayout.bringToFront()
        if (savedInstanceState == null) {
            replaceAppbarFragment(HomeAppBarFragment.newInstance())
            replaceMainFragment(HomeFragment.newInstance())

        }
        //테스트
        val loginViewModel: LoginViewModel by viewModel()
        loginViewModel.a().let {
            loginViewModel.mainCategory.observe(this@MainActivity, Observer {
                loginViewModel.test()
                mainViewModel.mainCategory=loginViewModel.mainCategory
                mainViewModel.subCategory=loginViewModel.subCategory
            })
        }
    }
}
