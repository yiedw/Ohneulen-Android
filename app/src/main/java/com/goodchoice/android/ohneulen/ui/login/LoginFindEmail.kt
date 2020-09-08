package com.goodchoice.android.ohneulen.ui.login

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.LoginFindEmailBinding
import com.goodchoice.android.ohneulen.ui.MainActivity
import com.goodchoice.android.ohneulen.util.OnBackPressedListener
import com.goodchoice.android.ohneulen.util.constant.BaseUrl
import com.goodchoice.android.ohneulen.util.replaceAppbarFragment
import com.goodchoice.android.ohneulen.util.replaceMainFragment
import timber.log.Timber

class LoginFindEmail : Fragment(), OnBackPressedListener {
    companion object {
        fun newInstance() = LoginFindEmail()
    }

    private lateinit var binding: LoginFindEmailBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.login_find_email,
            container,
            false
        )
        return binding.root
    }

    @SuppressLint("SetJavaScriptEnabled", "JavascriptInterface")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        WebView.setWebContentsDebuggingEnabled(false)
        val setting = binding.loginFindEmailWebView.settings
        setting.javaScriptEnabled = true
        setting.setSupportMultipleWindows(true)
        setting.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        val cookieManager = CookieManager.getInstance()
        cookieManager.setAcceptCookie(true)
        cookieManager.setAcceptThirdPartyCookies(binding.loginFindEmailWebView, true)
        binding.loginFindEmailWebView.loadUrl(BaseUrl.OHNEULEN_FIND_ID)
        binding.loginFindEmailWebView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                return false
            }

            override fun onFormResubmission(
                view: WebView?,
                dontResend: Message?,
                resend: Message?
            ) {
                super.onFormResubmission(view, dontResend, resend)
                resend!!.sendToTarget()
            }
        }

        binding.loginFindEmailWebView.addJavascriptInterface(object : Object() {
            @JavascriptInterface
            fun signUpSubmit(stat: Int) {
                //0 success 1 fail
                if (stat == 0) {
                    replaceAppbarFragment(LoginAppBar.newInstance(LoginAppBar.backFragmentAppBar))
                    MainActivity.supportFragmentManager.popBackStack()
                    Toast.makeText(
                        MainActivity.mainFrameLayout.context,
                        "회원가입이 완료되었습니다",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Handler().post {
                        Toast.makeText(
                            requireContext(),
                            "회원가입에 실패하였습니다\n 아이디,비밀번호를 확인해주세요",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }

            @JavascriptInterface
            fun onBackClick() {
                binding.loginFindEmailWebView.post {
                    if (binding.loginFindEmailWebView.canGoBack()) {
                        binding.loginFindEmailWebView.goBack()
                    } else {
                        replaceAppbarFragment(LoginAppBar.newInstance(LoginAppBar.backFragmentAppBar))
                        MainActivity.supportFragmentManager.popBackStack()
                    }
                }
            }

            @JavascriptInterface
            fun onLoginClick() {
                replaceAppbarFragment(LoginAppBar.newInstance(LoginAppBar.backFragmentAppBar))
                MainActivity.supportFragmentManager.popBackStack()
            }
        }, "android")


    }

    override fun onBackPressed() {
        if (binding.loginFindEmailWebView.canGoBack()) {
            binding.loginFindEmailWebView.goBack()
        } else {
            replaceAppbarFragment(LoginAppBar.newInstance(LoginAppBar.backFragmentAppBar))
            MainActivity.supportFragmentManager.popBackStack()

        }
    }

}