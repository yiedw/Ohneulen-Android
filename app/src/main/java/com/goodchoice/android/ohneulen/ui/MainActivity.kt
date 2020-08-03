package com.goodchoice.android.ohneulen.ui

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.MotionEvent
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.data.repository.InitData
import com.goodchoice.android.ohneulen.ui.home.Home
import com.goodchoice.android.ohneulen.ui.home.HomeAppBar
import com.goodchoice.android.ohneulen.ui.mypage.MyPage
import com.goodchoice.android.ohneulen.ui.mypage.MyPageAppBar
import com.goodchoice.android.ohneulen.ui.search.Search
import com.goodchoice.android.ohneulen.ui.search.SearchAppBar
import com.goodchoice.android.ohneulen.ui.store.StoreAppBar
import com.goodchoice.android.ohneulen.ui.store.StoreFragment
import com.goodchoice.android.ohneulen.util.OnBackPressedListener
import com.goodchoice.android.ohneulen.util.addMainFragment
import com.goodchoice.android.ohneulen.util.constant.ConstList
import com.goodchoice.android.ohneulen.util.replaceAppbarFragment
import com.goodchoice.android.ohneulen.util.replaceMainFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.dynamiclinks.ktx.*
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.main_activity.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    companion object {
        lateinit var supportFragmentManager: FragmentManager
        lateinit var appbarFrameLayout: FrameLayout
        lateinit var mainFrameLayout: FrameLayout
        lateinit var bottomNav: BottomNavigationView
        lateinit var initMainFrameLayout: ViewGroup.LayoutParams
    }

    private val initData: InitData by inject()
    private val mainViewModel: MainViewModel by viewModel()


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        Companion.supportFragmentManager = supportFragmentManager
        appbarFrameLayout = appbar_frameLayout
        mainFrameLayout = main_frameLayout
        appbarFrameLayout.bringToFront()
        initMainFrameLayout = mainFrameLayout.layoutParams
        bottomNav = main_bottom_nav

        //초기 데이터 받아오기
        initData

        //네비게이션 연결
        main_bottom_nav.setOnNavigationItemSelectedListener(this)

        //다이나믹 링크
        Firebase.dynamicLinks
            .getDynamicLink(Intent())
            .addOnSuccessListener(this) {
                var deepLink: Uri? = null
                if (it != null) {
                    //딥링크로 받아서 들어올때
                    deepLink = it.link
                    val segment = deepLink!!.lastPathSegment
                    when (segment) {
                        ConstList.SEGMENT_STORE -> {
                            val seq = deepLink.getQueryParameter(ConstList.SEQ)
                            StoreFragment.storeSeq=seq!!
                            replaceAppbarFragment(
                                StoreAppBar.newInstance(),
                                tag = "storeAppBar"
                            )
                            addMainFragment(StoreFragment.newInstance())

                        }
                    }
                } else {
                    //일반적으로 앱을 실행 했을때
                    bottomNav.selectedItemId=R.id.menu_bottom_nav_home
                }
            }


    }

    //다른곳 터치시 키보드 내리기
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        val focusView = currentFocus
        if (focusView != null) {
            val rect = Rect()
            focusView.getGlobalVisibleRect(rect)
            val x = ev!!.x.toInt()
            val y = ev.y.toInt()
            if (!rect.contains(x, y)) {
                val imm: InputMethodManager =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(focusView.windowToken, 0)
                focusView.clearFocus()
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    //각 fragment에서 뒤로가기 버튼 제어
    //뒤로가기 버튼 제어
    override fun onBackPressed() {
        val fragmentList: List<Fragment> =
            supportFragmentManager.fragments
        for (fragment in fragmentList) {
            if (fragment is OnBackPressedListener) {
                (fragment as OnBackPressedListener).onBackPressed()
            }
        }
    }

    //바텀 네비게이션 설정
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_bottom_nav_home -> {
                replaceAppbarFragment(HomeAppBar.newInstance())
                replaceMainFragment(Home.newInstance())
                return true
            }
            R.id.menu_bottom_nav_map -> {
                replaceAppbarFragment(SearchAppBar.newInstance())
                replaceMainFragment(Search.newInstance())
                return true
            }

//            R.id.menu_bottom_nav_like -> {
//                replaceAppbarFragment(MyPageLikeAppBar.newInstance())
//                replaceMainFragment(MyPageLike.newInstance())
//                return true
//            }


            R.id.menu_bottom_nav_more -> {
                replaceAppbarFragment(MyPageAppBar.newInstance())
                replaceMainFragment(MyPage.newInstance())
                return true
            }
        }
        return false
    }


}
