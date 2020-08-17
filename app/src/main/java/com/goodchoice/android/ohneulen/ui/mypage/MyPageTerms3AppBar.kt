package com.goodchoice.android.ohneulen.ui.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.MypageTerms2AppbarBinding
import com.goodchoice.android.ohneulen.databinding.MypageTerms3AppbarBinding
import com.goodchoice.android.ohneulen.ui.MainActivity
import com.goodchoice.android.ohneulen.util.OnBackPressedListener
import com.goodchoice.android.ohneulen.util.constant.ConstList
import com.goodchoice.android.ohneulen.util.replaceAppbarFragment
import com.goodchoice.android.ohneulen.util.replaceMainFragment

class MyPageTerms3AppBar() : Fragment(), OnBackPressedListener {

    companion object {
        fun newInstance() = MyPageTerms3AppBar()
    }

    private lateinit var binding: MypageTerms3AppbarBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.mypage_terms3_appbar,
            container,
            false
        )
        binding.fragment = this
        return binding.root
    }

    fun onBackClick(view: View) {
        replaceAppbarFragment(MyPageContactusAppBar.newInstance())
        MainActivity.supportFragmentManager.popBackStack()
    }

    override fun onBackPressed() {
        replaceAppbarFragment(MyPageContactusAppBar.newInstance())
        MainActivity.supportFragmentManager.popBackStack()
    }
}