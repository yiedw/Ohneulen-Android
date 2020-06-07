package com.goodchoice.android.ohneulen.ui

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.view.ViewTreeObserver
import android.widget.ScrollView
import androidx.core.content.ContextCompat
import com.goodchoice.android.ohneulen.MainActivity
import com.goodchoice.android.ohneulen.R
import timber.log.Timber

class NewScrollView : ScrollView, ViewTreeObserver.OnGlobalLayoutListener {

    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attr: AttributeSet?) : this(context, attr, 0)
    constructor(context: Context, attr: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attr,
        defStyleAttr
    ) {
        overScrollMode = OVER_SCROLL_NEVER
        viewTreeObserver.addOnGlobalLayoutListener(this)
    }

    var header: View? = null
        set(value) {
            field = value
            field?.let {
                it.translationZ = 1f
//                it.setOnClickListener { _ ->
//                    //클릭 시, 헤더뷰가 최상단으로 오게 스크롤 이동
//                    this.smoothScrollTo(scrollX, it.top)
//                    callStickListener()
//                }
            }
        }

    var stickListener: (View) -> Unit = {}
    var freeListener: (View) -> Unit = {}

    private var mIsHeaderSticky = false

    private var mHeaderInitPosition = 0f

    override fun onGlobalLayout() {
        mHeaderInitPosition = header?.top?.toFloat() ?: 0f
    }

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)

        val scrolly = t
        if (scrolly > mHeaderInitPosition-MainActivity.appbarFrameLayout.height) {
            stickHeader()
        } else {
            freeHeader()
        }
    }

    private fun stickHeader() {
        header?.translationY=scrollY.toFloat()+MainActivity.appbarFrameLayout.height-mHeaderInitPosition
//        Timber.e(scrollY.toString())
//        Timber.e(header?.y.toString())
//        Timber.e(MainActivity.appbarFrameLayout.height.toString())
        callStickListener()
    }

    private fun callStickListener() {
        if (!mIsHeaderSticky) {
            stickListener(header ?: return)
            mIsHeaderSticky = true
        }
    }

    private fun freeHeader() {
        header?.translationY = 0f
        callFreeListener()
    }

    private fun callFreeListener() {
        if (mIsHeaderSticky) {
            freeListener(header ?: return)
            mIsHeaderSticky = false
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        viewTreeObserver.removeOnGlobalLayoutListener(this)
    }

}