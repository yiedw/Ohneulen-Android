package com.goodchoice.android.ohneulen.util

import android.annotation.SuppressLint
import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import timber.log.Timber
import java.lang.Exception
import kotlin.math.abs

open class OnSwipeGesture(private val context: Context) : View.OnTouchListener {
    private val SWIPE_THRESHOLD = 100
    private val SWIPE_VELOCITY_THRESHOLD = 100

    private val gestureDetector: GestureDetector


    init {
        gestureDetector = GestureDetector(context, GestureListener())
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        try {
            if (event != null) {
                val temp=gestureDetector.onTouchEvent(event)
//                Timber.e(temp.toString())
                return temp
            }
        } catch (e: Exception) {
            
            Timber.e(e)
        }
        return false
    }

    private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent?): Boolean {
//            Timber.e("onDown")
            return true
        }

        //스와이프가 아닌 한번 클릭햇을때
        override fun onSingleTapUp(e: MotionEvent?): Boolean {
            if (e != null) {
                onSingleTab(e)
//                Timber.e("Asdf")
                return true
            }
            return false
        }

        override fun onFling(
            e1: MotionEvent?,
            e2: MotionEvent?,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            var result = false
            try {
                val diffY = e2!!.y - e1!!.y
                val diffX = e2.x - e1.x
//                Timber.e(diffY.toString())
//                Timber.e(diffX.toString())
//                Timber.e(velocityX.toString())
//                Timber.e(velocityY.toString())
                if (abs(diffX) > abs(diffY)) {
                    if (abs(diffX) > SWIPE_THRESHOLD && abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            onSwipeRight()
                        } else {
                            onSwipeLeft()
                        }
                        result = true
                    }
                } else if (abs(diffY) > SWIPE_THRESHOLD && abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffY > 0) {
                        onSwipeBottom()
                    } else {
                        onSwipeTop()
                    }
                    result = true
                }
            } catch (e: Exception) {
                Timber.e(e)
            }
//            Timber.e("fling")
            return result
        }

    }

    open fun onSwipeRight() {}
    open fun onSwipeLeft() {}
    open fun onSwipeTop() {}
    open fun onSwipeBottom() {}
    open fun onSingleTab(motionEvent: MotionEvent) {}

}