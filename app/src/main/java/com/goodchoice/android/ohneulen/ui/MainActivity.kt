package com.goodchoice.android.ohneulen.ui

import android.animation.Animator
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.MainActivityBinding
import com.goodchoice.android.ohneulen.ui.dialog.LoadingDialog
import com.goodchoice.android.ohneulen.ui.home.Home
import com.goodchoice.android.ohneulen.ui.home.HomeAppBar
import com.goodchoice.android.ohneulen.ui.like.Like
import com.goodchoice.android.ohneulen.ui.like.LikeAppBar
import com.goodchoice.android.ohneulen.ui.login.LoginViewModel
import com.goodchoice.android.ohneulen.ui.mypage.MyPage
import com.goodchoice.android.ohneulen.ui.mypage.MyPageAppBar
import com.goodchoice.android.ohneulen.ui.search.Search
import com.goodchoice.android.ohneulen.ui.search.SearchAppBar
import com.goodchoice.android.ohneulen.ui.store.StoreAppBar
import com.goodchoice.android.ohneulen.ui.store.StoreFragment
import com.goodchoice.android.ohneulen.util.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.dynamiclinks.ktx.*
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.main_activity.*
import org.koin.android.ext.android.inject
import timber.log.Timber
import java.util.*
import kotlin.concurrent.timer
import kotlin.math.hypot

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    /**
     * static 변수
     * supportFragmentManager = SupportFragmentManager
     * appbarFrameLayout = 헤더뷰
     * mainFrameLayout = 메인뷰
     * bottomNav = 바텀네비게이션
     * initMainFrame =
     */
    companion object {
        lateinit var supportFragmentManager: FragmentManager
//        lateinit var appbarFrameLayout: FrameLayout
        lateinit var mainFrameLayout: FrameLayout
        lateinit var bottomNav: BottomNavigationView
        lateinit var initMainFrameLayout: ViewGroup.LayoutParams
    }

    private lateinit var binding: MainActivityBinding
    private val loginViewModel: LoginViewModel by inject()
//    private val searchViewModel: SearchViewModel by inject()


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        val intent = this.intent
        binding = DataBindingUtil.setContentView(this, R.layout.main_activity)
        RevealAnimation(binding.mainActivity, intent, this)

//        searchViewModel.subCategoryList

        Companion.supportFragmentManager = supportFragmentManager
        mainFrameLayout = main_frameLayout
//        appbarFrameLayout = appbar_frameLayout
//        appbarFrameLayout.bringToFront()
        initMainFrameLayout = mainFrameLayout.layoutParams
        bottomNav = main_bottom_nav


        //네비게이션 연결
        main_bottom_nav.setOnNavigationItemSelectedListener(this)
        bottomNav.selectedItemId = R.id.menu_bottom_nav_home

        //다이나믹 링크

    }

    override fun onResume() {
        super.onResume()
        //우선 10분마다 로그인체크
        timer(period = 600000) {
            loginViewModel.loginCheck()
        }
    }

    override fun onStart() {
        super.onStart()
        Firebase.dynamicLinks
            .getDynamicLink(Intent())
            .addOnSuccessListener(this) {
                var deepLink: Uri? = null
                if (it != null) {

                    //딥링크로 받아서 들어올때
                    val dialog = LoadingDialog.newInstance("매장 들어가는 중...")
                    dialog.show(MainActivity.supportFragmentManager, "loading")
                    deepLink = it.link
                    Timber.e(deepLink.toString())
                    val storeSeq = deepLink!!.lastPathSegment
                    StoreFragment.storeSeq = storeSeq!!
                    StoreAppBar.stat = 0
//                    replaceAppbarFragment(StoreAppBar.newInstance())
//                    replaceMainFragment(StoreFragment.newInstance(), true)
                    val fragmentManager = MainActivity.supportFragmentManager.beginTransaction()
//                    fragmentManager.replace(R.id.appbar_frameLayout, StoreAppBar.newInstance())
                    fragmentManager.replace(R.id.main_frameLayout, StoreFragment.newInstance())
                    fragmentManager.addToBackStack(null)
                    fragmentManager.commit()

//                    when (segment) {
//                        ConstList.SEGMENT_STORE -> {
//                            val seq = deepLink.getQueryParameter(ConstList.SEQ)
//                            StoreFragment.storeSeq = seq!!
//                            addAppbarFragment(StoreAppBar.newInstance())
//                            addMainFragment(StoreFragment.newInstance())
//
//                        }
//                    }
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
            supportFragmentManager.fragments.reversed() //가장 최근순부터 검색할수 있게
        Timber.e(fragmentList.toString())
        for (fragment in fragmentList) {
            if (fragment is OnBackPressedListener) {
                (fragment as OnBackPressedListener).onBackPressed()
                //onBackPressedListener 가장 최근꺼 하나만 가져오기
                break
            }
        }
    }

    //바텀 네비게이션 설정
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        backStackClear()
        bottomNav.visibility = View.VISIBLE
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

            R.id.menu_bottom_nav_like -> {
                if (!LoginViewModel.isLogin.value!!) {
                    loginDialog(
                        this,
                        true
                    )
                    return false
                }
                replaceAppbarFragment(LikeAppBar.newInstance())
                replaceMainFragment(Like.newInstance())
                return true
            }


            R.id.menu_bottom_nav_more -> {
                replaceAppbarFragment(MyPageAppBar.newInstance())
                replaceMainFragment(MyPage.newInstance())
                return true
            }
        }
        return false
    }

    private fun backStackClear() {
        for (i in 0 until supportFragmentManager.backStackEntryCount) {
            supportFragmentManager.popBackStack()
        }
    }

    private fun showReveal() {
        val centerX = binding.mainActivityCenter.x
        val centerY = binding.mainActivityCenter.y
        val radius =
            hypot(binding.mainActivity.width.toDouble(), binding.mainActivity.height.toDouble())

        val revealAnimator = ViewAnimationUtils.createCircularReveal(
            binding.mainActivity,
            centerX.toInt(), centerY.toInt(), 0f, radius.toFloat()
        )
        revealAnimator.interpolator = object : AccelerateDecelerateInterpolator() {

        }
        revealAnimator.duration = 1000
        revealAnimator.start()
        revealAnimator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {
            }

        })
    }


}
