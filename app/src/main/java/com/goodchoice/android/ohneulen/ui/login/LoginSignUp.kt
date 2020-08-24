package com.goodchoice.android.ohneulen.ui.login

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.LoginSignUpBinding
import com.goodchoice.android.ohneulen.ui.MainActivity
import com.goodchoice.android.ohneulen.ui.mypage.MyPageAppBar
import com.goodchoice.android.ohneulen.util.OnBackPressedListener
import com.goodchoice.android.ohneulen.util.constant.BaseUrl
import com.goodchoice.android.ohneulen.util.constant.ConstList
import com.goodchoice.android.ohneulen.util.replaceAppbarFragment
import com.goodchoice.android.ohneulen.util.replaceMainFragment
import timber.log.Timber
import java.util.*

class LoginSignUp : Fragment() ,OnBackPressedListener{
    companion object {
        fun newInstance() = LoginSignUp()
    }

    private lateinit var binding: LoginSignUpBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        MainActivity.appbarFrameLayout.visibility = View.GONE
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
        binding.fragment = this
        return binding.root
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val setting=binding.loginSignUpWebView.settings
        setting.javaScriptEnabled=true
        setting.setSupportMultipleWindows(true)
//        setting.userAgentString="Android"
        binding.loginSignUpWebView.loadUrl(BaseUrl.OHNEULEN_SIGN_UP)

//        binding.loginSignUpWebView.addJavascriptInterface(object :Object(){
//            @JavascriptInterface
//            fun justDoIt(keyword:String){
//
//            }
//        },"android_ohneulen")
    }

    override fun onDestroy() {
        super.onDestroy()
        MainActivity.appbarFrameLayout.visibility = View.VISIBLE
    }

    override fun onBackPressed() {
        replaceAppbarFragment(LoginAppBar.newInstance())
        replaceMainFragment(Login.newInstance())
    }

}