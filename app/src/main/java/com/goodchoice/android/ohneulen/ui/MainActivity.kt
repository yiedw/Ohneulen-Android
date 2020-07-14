package com.goodchoice.android.ohneulen.ui

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import androidx.fragment.app.FragmentManager
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.data.repository.InitData
import com.goodchoice.android.ohneulen.ui.home.HomeFragment
import com.goodchoice.android.ohneulen.ui.home.HomeAppBarFragment
import com.goodchoice.android.ohneulen.ui.store.StoreAppBarFragment
import com.goodchoice.android.ohneulen.ui.store.StoreFragment
import com.goodchoice.android.ohneulen.util.addMainFragment
import com.goodchoice.android.ohneulen.util.constant.ConstList
import com.goodchoice.android.ohneulen.util.replaceAppbarFragment
import com.goodchoice.android.ohneulen.util.replaceMainFragment
import com.google.firebase.dynamiclinks.ktx.*
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.main_activity.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    companion object {
        lateinit var supportFragmentManager: FragmentManager
        lateinit var appbarFrameLayout: FrameLayout
        lateinit var mainFrameLayout: FrameLayout
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
//        if (savedInstanceState == null) {
//            replaceAppbarFragment(HomeAppBarFragment.newInstance())
//            replaceMainFragment(HomeFragment.newInstance())
//
//        }

        //초기 데이터 받아오기
        initData

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
                            val code = deepLink.getQueryParameter(ConstList.CODE)
                            Timber.e(code)
                            replaceAppbarFragment(
                                StoreAppBarFragment.newInstance(),
                                tag = "storeAppBar"
                            )
                            addMainFragment(StoreFragment.newInstance())

                        }
                    }
                } else {
                    //일반적으로 앱을 실행 했을때
                    replaceAppbarFragment(HomeAppBarFragment.newInstance())
                    replaceMainFragment(HomeFragment.newInstance())
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

    private fun storeDetailDeepLink() {
        Firebase.dynamicLinks.dynamicLink {
            link = Uri.parse("https://www.ohneulen.com")
            domainUriPrefix = "https://ohneulen.page.link"
            androidParameters { }
            iosParameters("com.ohneulen.ios") {}
        }
    }


}
