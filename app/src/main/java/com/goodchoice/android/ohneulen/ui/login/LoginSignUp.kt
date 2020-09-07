package com.goodchoice.android.ohneulen.ui.login

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.LoginSignUpBinding
import com.goodchoice.android.ohneulen.ui.MainActivity
import com.goodchoice.android.ohneulen.util.OnBackPressedListener
import com.goodchoice.android.ohneulen.util.constant.BaseUrl
import com.goodchoice.android.ohneulen.util.replaceAppbarFragment
import com.goodchoice.android.ohneulen.util.replaceMainFragment
import timber.log.Timber

class LoginSignUp : Fragment(), OnBackPressedListener {
    companion object {
        fun newInstance() = LoginSignUp()
    }

    private lateinit var binding: LoginSignUpBinding
    override fun onAttach(context: Context) {
        super.onAttach(context)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.login_sign_up,
            container,
            false
        )
        return binding.root
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        WebView.setWebContentsDebuggingEnabled(false)
        val setting = binding.loginSignUpWebView.settings
        setting.javaScriptEnabled = true
        setting.setSupportMultipleWindows(true)
//        setting.userAgentString = "Android_app"
        //본인인증 받기위해 필요
        setting.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        val cookieManager: CookieManager = CookieManager.getInstance()
        cookieManager.setAcceptCookie(true)
        cookieManager.setAcceptThirdPartyCookies(binding.loginSignUpWebView, true)
        binding.loginSignUpWebView.loadUrl(BaseUrl.OHNEULEN_SIGN_UP)
        binding.loginSignUpWebView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                return false
            }
        }

        binding.loginSignUpWebView.addJavascriptInterface(object : Object() {
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
                 binding.loginSignUpWebView.post {
                    if (binding.loginSignUpWebView.canGoBack()) {
                        binding.loginSignUpWebView.goBack()
                    } else {
                        replaceAppbarFragment(LoginAppBar.newInstance(LoginAppBar.backFragmentAppBar))
                        MainActivity.supportFragmentManager.popBackStack()
                    }
                }
            }

            @JavascriptInterface
            fun alert(message: String) {
                Handler().post {
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                }
            }
        }, "android")

    }

    override fun onBackPressed() {
        replaceAppbarFragment(LoginAppBar.newInstance(LoginAppBar.backFragmentAppBar))
        MainActivity.supportFragmentManager.popBackStack()

    }


}