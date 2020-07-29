package com.goodchoice.android.ohneulen.ui.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.MypageContactusAppbarBinding
import com.goodchoice.android.ohneulen.util.replaceAppbarFragment
import com.goodchoice.android.ohneulen.util.replaceMainFragment

class MyPageContactusAppBar : Fragment() {
    companion object {
        fun newInstance() = MyPageContactusAppBar()
    }

    private lateinit var binding: MypageContactusAppbarBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.mypage_contactus_appbar,
            container,
            false
        )
        binding.fragment=this
        return binding.root
    }

    fun backClick(view:View){
        replaceAppbarFragment(MyPageAppBar.newInstance())
        replaceMainFragment(MyPage.newInstance())
    }
}