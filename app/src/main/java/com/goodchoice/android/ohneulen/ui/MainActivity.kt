package com.goodchoice.android.ohneulen.ui

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.data.repository.InitData
import com.goodchoice.android.ohneulen.ui.home.HomeFragment
import com.goodchoice.android.ohneulen.ui.home.HomeAppBarFragment
import com.goodchoice.android.ohneulen.ui.login.LoginViewModel
import com.goodchoice.android.ohneulen.ui.search.SearchAppBarFragment
import com.goodchoice.android.ohneulen.ui.search.SearchFragment
import com.goodchoice.android.ohneulen.ui.store.StoreAppBarFragment
import com.goodchoice.android.ohneulen.ui.store.StoreFragment
import com.goodchoice.android.ohneulen.util.addAppbarFragment
import com.goodchoice.android.ohneulen.util.addMainFragment
import com.goodchoice.android.ohneulen.util.replaceAppbarFragment
import com.goodchoice.android.ohneulen.util.replaceMainFragment
import kotlinx.android.synthetic.main.main_activity.*
import kotlinx.coroutines.delay
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    companion object {
        lateinit var supportFragmentManager: FragmentManager
        lateinit var appbarFrameLayout: FrameLayout
        lateinit var mainFrameLayout: FrameLayout
        lateinit var initMainFrameLayout:ViewGroup.LayoutParams
    }

    private val initData:InitData by inject()
    private val mainViewModel:MainViewModel by viewModel()


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        Companion.supportFragmentManager = supportFragmentManager
        appbarFrameLayout = appbar_frameLayout
        mainFrameLayout = main_frameLayout
        appbarFrameLayout.bringToFront()
        initMainFrameLayout=mainFrameLayout.layoutParams
//        if (savedInstanceState == null) {
//            replaceAppbarFragment(HomeAppBarFragment.newInstance())
//            replaceMainFragment(HomeFragment.newInstance())
//
//        }

        //데이터 받아오기
        initData

        //deepLink
        val intent=intent
        val data: Uri? =intent.data
        if(data!=null){
            if(data.host==getString(R.string.kakaolink_host)){
                mainViewModel.searchEditText = "강남역"
                replaceAppbarFragment(SearchAppBarFragment.newInstance())
                replaceMainFragment(SearchFragment.newInstance(), name = "search")
                addMainFragment(StoreFragment.newInstance(), true)
                addAppbarFragment(StoreAppBarFragment.newInstance(), true)
            }
        }
        else{
            replaceAppbarFragment(HomeAppBarFragment.newInstance())
            replaceMainFragment(HomeFragment.newInstance())
        }
    }
}
