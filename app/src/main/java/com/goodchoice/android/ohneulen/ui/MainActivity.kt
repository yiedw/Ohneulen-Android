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
import com.goodchoice.android.ohneulen.ui.search.SearchAppBarFragment
import com.goodchoice.android.ohneulen.ui.search.SearchFragment
import com.goodchoice.android.ohneulen.ui.store.StoreAppBarFragment
import com.goodchoice.android.ohneulen.ui.store.StoreFragment
import com.goodchoice.android.ohneulen.util.addAppbarFragment
import com.goodchoice.android.ohneulen.util.addMainFragment
import com.goodchoice.android.ohneulen.util.replaceAppbarFragment
import com.goodchoice.android.ohneulen.util.replaceMainFragment
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.google.firebase.dynamiclinks.PendingDynamicLinkData
import com.google.firebase.dynamiclinks.ShortDynamicLink
import com.google.firebase.dynamiclinks.ktx.*
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.main_activity.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.net.URI

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

        //데이터 받아오기
        initData

        //deepLink
//        val intent = intent
//        val data: Uri? = intent.data
//        if (data != null) {
//            if (data.host == getString(R.string.kakaolink_host)) {
//                mainViewModel.searchEditText = "강남역"
//                replaceAppbarFragment(SearchAppBarFragment.newInstance())
//                replaceMainFragment(SearchFragment.newInstance(), name = "search")
//                addMainFragment(StoreFragment.newInstance(), true)
//                addAppbarFragment(StoreAppBarFragment.newInstance(), true)
//            }
//        } else {
//            replaceAppbarFragment(HomeAppBarFragment.newInstance())
//            replaceMainFragment(HomeFragment.newInstance())
//        }


        val dynamicLink = Firebase.dynamicLinks.shortLinkAsync {
            link = Uri.parse("https://www.ohneulen.com")
            domainUriPrefix = "https://ohneulen.page.link"
            androidParameters { "com.ohneulen.android" }
            iosParameters("com.ohneulen.ios") {}
//            buildShortDynamicLink(ShortDynamicLink.Suffix.SHORT)
        }


        dynamicLink.addOnSuccessListener {
            Timber.e(dynamicLink.result!!.shortLink.toString())

        }

        Firebase.dynamicLinks
            .getDynamicLink(Intent())
            .addOnSuccessListener(this) {
                var deepLink: Uri? = null
                if (it != null) {
                    deepLink = it.link
                    replaceAppbarFragment(StoreAppBarFragment.newInstance(), name = "storeAppBar")
                    addMainFragment(StoreFragment.newInstance())
                }
                else{
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
