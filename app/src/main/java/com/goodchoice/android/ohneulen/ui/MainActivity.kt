package com.goodchoice.android.ohneulen.ui

import android.animation.Animator
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.net.Network
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.data.repository.InitData
import com.goodchoice.android.ohneulen.data.service.NetworkService
import com.goodchoice.android.ohneulen.databinding.MainActivityBinding
import com.goodchoice.android.ohneulen.ui.home.Home
import com.goodchoice.android.ohneulen.ui.home.HomeAppBar
import com.goodchoice.android.ohneulen.ui.like.Like
import com.goodchoice.android.ohneulen.ui.like.LikeAppBar
import com.goodchoice.android.ohneulen.ui.login.Login
import com.goodchoice.android.ohneulen.ui.login.LoginViewModel
import com.goodchoice.android.ohneulen.ui.mypage.MyPage
import com.goodchoice.android.ohneulen.ui.mypage.MyPageAppBar
import com.goodchoice.android.ohneulen.ui.search.Search
import com.goodchoice.android.ohneulen.ui.search.SearchAppBar
import com.goodchoice.android.ohneulen.ui.search.SearchViewModel
import com.goodchoice.android.ohneulen.ui.store.StoreAppBar
import com.goodchoice.android.ohneulen.ui.store.StoreFragment
import com.goodchoice.android.ohneulen.util.*
import com.goodchoice.android.ohneulen.util.constant.ConstList
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.dynamiclinks.ktx.*
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.main_activity.*
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.lang.Exception
import java.lang.RuntimeException
import kotlin.math.hypot

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    companion object {
        lateinit var supportFragmentManager: FragmentManager
        lateinit var appbarFrameLayout: FrameLayout
        lateinit var mainFrameLayout: FrameLayout
        lateinit var bottomNav: BottomNavigationView
        lateinit var initMainFrameLayout: ViewGroup.LayoutParams
    }

    private lateinit var binding: MainActivityBinding
//    private val searchViewModel: SearchViewModel by inject()


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        val intent=this.intent
        binding = DataBindingUtil.setContentView(this, R.layout.main_activity)
        RevealAnimation(binding.mainActivity,intent,this)

        Timber.e("asdf")
//        searchViewModel.subCategoryList

        Companion.supportFragmentManager = supportFragmentManager
        appbarFrameLayout = appbar_frameLayout
        mainFrameLayout = main_frameLayout
//        appbarFrameLayout.bringToFront()
        initMainFrameLayout = mainFrameLayout.layoutParams
        bottomNav = main_bottom_nav


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
                            StoreFragment.storeSeq = seq!!
//                            replaceAppbarFragment(
//                                StoreAppBar.newInstance(),
//                                tag = "storeAppBar"
//                            )
//                            appbarFrameLayout.setBackgroundColor(getColor(R.color.colorHeader))
                            addAppbarFragment(StoreAppBar.newInstance())
                            addMainFragment(StoreFragment.newInstance())

                        }
                    }
                } else {
                    //일반적으로 앱을 실행 했을때
                    bottomNav.selectedItemId = R.id.menu_bottom_nav_home
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
                        supportFragmentManager.findFragmentById(R.id.appbar_frameLayout)!!,
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
        revealAnimator.addListener(object :Animator.AnimatorListener{
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
