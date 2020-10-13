package com.goodchoice.android.ohneulen.ui.mypage

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Message
import android.webkit.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.WebviewActivityBinding
import com.goodchoice.android.ohneulen.util.OnSwipeGesture
import com.goodchoice.android.ohneulen.util.constant.BaseUrl
import com.goodchoice.android.ohneulen.util.constant.ConstList
import kotlinx.android.synthetic.main.webview_activity.view.*

class MyPageInfoActivity : AppCompatActivity() {

    private lateinit var binding: WebviewActivityBinding

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.webview_activity)

        //스와이프 기능
        binding.webViewActivityWebView.setOnTouchListener(object : OnSwipeGesture(this) {
            override fun onSwipeRight() {
                super.onSwipeRight()
                finish()
        }
        })

        webViewSetting()
    }

    @SuppressLint("SetJavaScriptEnabled")
    fun webViewSetting() {
        WebView.setWebContentsDebuggingEnabled(false)
        val settings = binding.webViewActivityWebView.settings
        settings.javaScriptEnabled = true   //자바스크립트 사용 여부
        settings.setSupportMultipleWindows(true)    // 여러개의 윈도우 사용 여부
        settings.mixedContentMode =
            WebSettings.MIXED_CONTENT_ALWAYS_ALLOW    //혼합컨텐츠 허용(HTTPS,HTTP), 본인인증 받을때 필요
        val cookieManager = CookieManager.getInstance()
        cookieManager.setAcceptCookie(true)
        cookieManager.setAcceptThirdPartyCookies(binding.webViewActivityWebView, true)  //쿠키허용
        binding.webViewActivityWebView.loadUrl(BaseUrl.OHNEULEN_MYPAGE_INFO)
        binding.webViewActivityWebView.webViewClient = object : WebViewClient() {
            //현재 페이지의 url을 읽어옴
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                return false
            }

            //post 데이터가 포함된 페이지에서 post데이터를 다시 보내려고 하는 경우에 사용
            //뒤로가기가 안먹는 현상을 방지하고자 씀
            override fun onFormResubmission(
                view: WebView?,
                dontResend: Message?,
                resend: Message?
            ) {
                super.onFormResubmission(view, dontResend, resend)
                resend!!.sendToTarget()
            }
        }
    }
}