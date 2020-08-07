package com.goodchoice.android.ohneulen.ui.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.MypageTerm2AppbarBinding
import com.goodchoice.android.ohneulen.ui.MainActivity
import com.goodchoice.android.ohneulen.util.OnBackPressedListener
import com.goodchoice.android.ohneulen.util.constant.ConstList
import com.goodchoice.android.ohneulen.util.replaceAppbarFragment
import com.goodchoice.android.ohneulen.util.replaceMainFragment

class MyPageTerm2AppBar(private val back: Int) : Fragment(), OnBackPressedListener {

    companion object {
        fun newInstance(back: Int = ConstList.MYPAGE) = MyPageTerm2AppBar(back)
    }

    private lateinit var binding: MypageTerm2AppbarBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.mypage_term2_appbar,
            container,
            false
        )
        binding.fragment = this
        return binding.root
    }

    fun onBackClick(view: View) {
        if (back == ConstList.MYPAGE) {
            replaceAppbarFragment(MyPageAppBar.newInstance())
            replaceMainFragment(MyPage.newInstance())
        } else if (back == ConstList.MYPAGE_CONTACTUS) {
            replaceAppbarFragment(MyPageContactusAppBar.newInstance())
            MainActivity.supportFragmentManager.popBackStack()
        }
    }

    override fun onBackPressed() {
        if (back == ConstList.MYPAGE) {
            replaceAppbarFragment(MyPageAppBar.newInstance())
            replaceMainFragment(MyPage.newInstance())
        } else if (back == ConstList.MYPAGE_CONTACTUS) {
            replaceAppbarFragment(MyPageContactusAppBar.newInstance())
            MainActivity.supportFragmentManager.popBackStack()
        }
    }
}