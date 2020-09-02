package com.goodchoice.android.ohneulen.ui

import android.animation.Animator
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewAnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.goodchoice.android.ohneulen.R
import kotlin.math.hypot

class Splash : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash)

    }

    override fun onResume() {
        super.onResume()
        val intent = Intent(this, MainActivity::class.java)
        showReveal()
        startActivity(intent)
        finish()
    }


    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
    }

    private fun showReveal() {
        val logo = findViewById<ImageView>(R.id.splash_logo)
        val layout = findViewById<ConstraintLayout>(R.id.spalsh)
        val centerX = logo.x + logo.width / 2
        val centerY = logo.y + logo.height / 2
        val radius = hypot(layout.width.toDouble(), layout.height.toDouble())

        val revealAnimator = ViewAnimationUtils.createCircularReveal(
            layout,
            centerX.toInt(), centerY.toInt(), radius.toFloat(), 0f
        )
        revealAnimator.addListener(animatorListener)
        revealAnimator.duration = 500
        revealAnimator.start()
    }

    val animatorListener = object : Animator.AnimatorListener {
        override fun onAnimationRepeat(animation: Animator?) {
        }

        override fun onAnimationEnd(animation: Animator?) {
            findViewById<ConstraintLayout>(R.id.spalsh).visibility = View.INVISIBLE
        }

        override fun onAnimationCancel(animation: Animator?) {
        }

        override fun onAnimationStart(animation: Animator?) {
        }

    }
}