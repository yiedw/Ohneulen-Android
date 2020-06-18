package com.goodchoice.android.ohneulen.ui.partner

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewTreeObserver
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import com.goodchoice.android.ohneulen.ui.MainActivity
import com.goodchoice.android.ohneulen.R

class PartnerScrollView : NestedScrollView, ViewTreeObserver.OnGlobalLayoutListener {

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
        if (scrolly > mHeaderInitPosition - MainActivity.appbarFrameLayout.height) {
            stickHeader()
        } else {
            freeHeader()
        }
    }

    private fun stickHeader() {
        if (PartnerFragment.state != 3)
            header?.translationY =
                scrollY.toFloat() + MainActivity.appbarFrameLayout.height - mHeaderInitPosition
        else
            header?.translationY =
                scrollY.toFloat() - mHeaderInitPosition
        MainActivity.appbarFrameLayout.background = ContextCompat.getDrawable(
            MainActivity.appbarFrameLayout.context,
            R.color.colorWhite
        )
        callStickListener()
    }

    private fun callStickListener() {
        if (!mIsHeaderSticky) {
            stickListener(header ?: return)
            mIsHeaderSticky = true
        }
    }

    fun freeHeader() {
        header?.translationY = 0f

        MainActivity.appbarFrameLayout.background = ContextCompat.getDrawable(
            MainActivity.appbarFrameLayout.context,
            R.color.colorTransparent
        )
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