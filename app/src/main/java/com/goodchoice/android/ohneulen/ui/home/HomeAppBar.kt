package com.goodchoice.android.ohneulen.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.HomeAppbarFragmentBinding
import com.goodchoice.android.ohneulen.ui.mypage.MyPageAppBar
import com.goodchoice.android.ohneulen.ui.mypage.MyPage
import com.goodchoice.android.ohneulen.ui.home.noti.NotiAppBar
import com.goodchoice.android.ohneulen.ui.home.noti.Noti
import com.goodchoice.android.ohneulen.util.replaceAppbarFragment
import com.goodchoice.android.ohneulen.util.replaceMainFragment

class HomeAppBar :Fragment(){

    companion object{
        fun newInstance()=HomeAppBar()
    }

    private lateinit var binding:HomeAppbarFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=DataBindingUtil.inflate(
            inflater,
            R.layout.home_appbar_fragment,
            container,
            false
        )
        binding.fragment=this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    fun myClick(view: View){
        replaceAppbarFragment(MyPageAppBar.newInstance())
        replaceMainFragment(MyPage.newInstance())
    }

    fun notiClick(view:View){
        replaceAppbarFragment(NotiAppBar.newInstance())
        replaceMainFragment(Noti.newInstance())
    }
}