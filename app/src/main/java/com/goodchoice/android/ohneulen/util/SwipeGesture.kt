package com.goodchoice.android.ohneulen.util

import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import kotlin.math.abs

class SwipeGesture(view: View) : GestureDetector.OnGestureListener {
    private val SWIPE_THRESHOLD = 100
    private val SWIPE_VELOCITY_THRESHOLD = 100
    val view = view

    override fun onFling(
        e1: MotionEvent?,
        e2: MotionEvent?,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        var result = false
        try{
            val diffY=e2!!.y-e1!!.y
            val diffX=e2.x-e1.x
            if(abs(diffX) > abs(diffY)){
                abs(diffX)>
            }
        }
    }

    override fun onShowPress(e: MotionEvent?) {
    }

    override fun onSingleTapUp(e: MotionEvent?): Boolean {
        return true
    }

    override fun onDown(e: MotionEvent?): Boolean {
        return true
    }


    override fun onScroll(
        e1: MotionEvent?,
        e2: MotionEvent?,
        distanceX: Float,
        distanceY: Float
    ): Boolean {
        return true
    }

    override fun onLongPress(e: MotionEvent?) {
    }
}