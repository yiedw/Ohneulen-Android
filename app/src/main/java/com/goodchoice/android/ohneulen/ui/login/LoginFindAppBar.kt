package com.goodchoice.android.ohneulen.ui.login

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.LoginFindAppbarBinding
import com.goodchoice.android.ohneulen.ui.MainActivity
import com.goodchoice.android.ohneulen.util.OnBackPressedListener
import com.goodchoice.android.ohneulen.util.replaceAppbarFragment
import com.goodchoice.android.ohneulen.util.replaceMainFragment

class LoginFindAppBar : Fragment() {

    companion object {
        fun newInstance() = LoginFindAppBar()
    }

    private lateinit var binding: LoginFindAppbarBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        MainActivity.appbarFrameLayout.visibility=View.GONE
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.login_find_appbar,
            container,
            false
        )
        binding.fragment=this
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        MainActivity.appbarFrameLayout.visibility=View.VISIBLE
    }

//    fun backClick(view: View){
//        replaceAppbarFragment(LoginAppBar.newInstance())
//        MainActivity.supportFragmentManager.popBackStack()
//    }


}