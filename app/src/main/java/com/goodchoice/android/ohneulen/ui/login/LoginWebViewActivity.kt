package com.goodchoice.android.ohneulen.ui.login

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Message
import android.webkit.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.WebviewActivityBinding
import com.goodchoice.android.ohneulen.util.constant.BaseUrl
import com.goodchoice.android.ohneulen.util.constant.ConstList

class LoginWebViewActivity : AppCompatActivity() {


    private lateinit var binding: WebviewActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.webview_activity)
        webViewSetting()    //웹뷰세팅
        webViewFunction()   //웹뷰기능

    }

    //웹뷰 세팅
    @SuppressLint("SetJavaScriptEnabled")
    fun webViewSetting() {
        WebView.setWebContentsDebuggingEnabled(false)
        val settings = binding.webViewActivityWebView.settings
        settings.javaScriptEnabled = true   //자바스크립트 사용 여부
        settings.setSupportMultipleWindows(true)    // 여러개의 윈도우 사용 여부
        settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW    //혼합컨텐츠 허용(HTTPS,HTTP)
        val cookieManager = CookieManager.getInstance()
        cookieManager.setAcceptCookie(true)
        cookieManager.setAcceptThirdPartyCookies(binding.webViewActivityWebView, true)  //쿠키허용
        val intent = intent
        when (intent.extras!!.getInt("stat")) {
            ConstList.WEBVIEW_SIGN_UP -> binding.webViewActivityWebView.loadUrl(BaseUrl.OHNEULEN_SIGN_UP)
            ConstList.WEBVIEW_FIND_EMAIL -> binding.webViewActivityWebView.loadUrl(BaseUrl.OHNEULEN_FIND_EMAIL)
            ConstList.WEBVIEW_FIND_PW -> binding.webViewActivityWebView.loadUrl(BaseUrl.OHNEULEN_FIND_PW)
        }
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

    //웹뷰에서 클릭했을때 반응하는 함수
    @SuppressLint("JavascriptInterface")
    fun webViewFunction() {
        binding.webViewActivityWebView.addJavascriptInterface(object : Object() {

            //아이디찾기 -> 회원가입 페이지로 이동했을때 사용하는 함수
            @JavascriptInterface
            fun signUpSubmit(stat: Int) {
                //0 success 1 fail
                if (stat == 0) {
                    finish()
                    Toast.makeText(this@LoginWebViewActivity, "회원가입이 완료되었습니다", Toast.LENGTH_LONG)
                        .show()
                } else {
                    Toast.makeText(
                        this@LoginWebViewActivity,
                        "회원가입에 실패하였습니다\n 아이디, 비밀번호를 확인해주세요",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }


            //뒤로가기 클릭 -> 웹뷰 내에서 뒤로 갈 곳이 있으면 뒤로가고 아니면 웹뷰 종료
            @JavascriptInterface
            fun onBackClick() {
//                binding.webViewActivityWebView.post {
//                        if (binding.webViewActivityWebView.canGoBack()) {
//                            binding.webViewActivityWebView.goBack()
//                        } else {
//                        }
//                }
                finish()
            }

            //로그인 클릭 -> 어플 로그인화면으로 이동
            @JavascriptInterface
            fun onLoginClick() {
                if (intent.extras!!.getInt("stat") == ConstList.WEBVIEW_FIND_PW) {
                    Toast.makeText(this@LoginWebViewActivity, "비밀번호가 재설정 되었습니다", Toast.LENGTH_LONG)
                        .show()
                }
                finish()
            }

        }, "android")
    }


}