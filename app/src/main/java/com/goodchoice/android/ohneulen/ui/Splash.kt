package com.goodchoice.android.ohneulen.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.ConnectivityManager.TYPE_WIFI
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.animation.AccelerateInterpolator
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.SplashBinding
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase
import timber.log.Timber
import java.util.*
import kotlin.concurrent.schedule


class Splash : AppCompatActivity() {

    private lateinit var binding: SplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.splash)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        val view = binding.splashLogo
        if (view.width != 0) {
            if (!networkCheck(this)) {
                Toast.makeText(this, "네트워크를 연결해 주세요", Toast.LENGTH_SHORT).show()
                Timer().schedule(2000L){
                    finish()
                }
            } else {
                Thread.sleep(200)
                startRevealActivity(view)
                // 스플래시화면이 완전히 로딩된 이후에 배경색을 흰 색으로 바꿔줌
                // -> mapView가 destory될때 뒤에 배경이 보이므로 그때 흰배경을 띄우기위해
                Timer().schedule(3000L) {
                    binding.splash.setBackgroundColor(ContextCompat.getColor(this@Splash, R.color.white))

                }
            }
        }
    }


    private fun startRevealActivity(v: View) {
        val revealX = (v.x + v.width / 2).toInt()
        val revealY = (v.y + v.height / 2).toInt()

        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra(RevealAnimation.EXTRA_CIRCULAR_REVEAL_X, revealX)
        intent.putExtra(RevealAnimation.EXTRA_CIRCULAR_REVEAL_Y, revealY)
        startActivity(intent)
        overridePendingTransition(0, 0)
    }

    private fun networkCheck(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val new = connectivityManager.activeNetwork ?: return false
        val actNw = connectivityManager.getNetworkCapabilities(new) ?: return false
        return true
    }


}

class RevealAnimation(private val mView: View, intent: Intent, private val mActivity: Activity) {
    private var revealX = 0
    private var revealY = 0
    fun revealActivity(x: Int, y: Int) {
        val finalRadius =
            (mView.width.coerceAtLeast(mView.height) * 1.5).toFloat()

        // create the animator for this view (the start radius is zero)
        val circularReveal =
            ViewAnimationUtils.createCircularReveal(mView, x, y, 10f, finalRadius)
        circularReveal.duration = 850
        circularReveal.interpolator = AccelerateInterpolator()

        // make the view visible and start the animation
        mView.visibility = View.VISIBLE
        circularReveal.start()
    }

    fun unRevealActivity() {
        val finalRadius =
            (Math.max(mView.width, mView.height) * 1.1).toFloat()
        val circularReveal =
            ViewAnimationUtils.createCircularReveal(
                mView, revealX, revealY, finalRadius, 0f
            )
        circularReveal.duration = 300
        circularReveal.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                mView.visibility = View.INVISIBLE
                mActivity.finish()
                mActivity.overridePendingTransition(0, 0)
            }
        })
        circularReveal.start()
    }

    companion object {
        const val EXTRA_CIRCULAR_REVEAL_X = "EXTRA_CIRCULAR_REVEAL_X"
        const val EXTRA_CIRCULAR_REVEAL_Y = "EXTRA_CIRCULAR_REVEAL_Y"
    }

    init {
        //when you're android version is at leat Lollipop it starts the reveal activity
        mView.visibility = View.INVISIBLE
        revealX = intent.getIntExtra(EXTRA_CIRCULAR_REVEAL_X, 0)
        revealY = intent.getIntExtra(EXTRA_CIRCULAR_REVEAL_Y, 0)
        val viewTreeObserver = mView.viewTreeObserver
        if (viewTreeObserver.isAlive) {
            viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    revealActivity(revealX, revealY)
                    mView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
            })
        }
    }
}