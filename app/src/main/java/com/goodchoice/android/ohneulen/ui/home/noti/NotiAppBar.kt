package com.goodchoice.android.ohneulen.ui.home.noti

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.NotiAppbarBinding
import com.goodchoice.android.ohneulen.ui.home.HomeAppBar
import com.goodchoice.android.ohneulen.ui.home.Home
import com.goodchoice.android.ohneulen.util.OnBackPressedListener
import com.goodchoice.android.ohneulen.util.replaceAppbarFragment
import com.goodchoice.android.ohneulen.util.replaceMainFragment

class NotiAppBar :Fragment(),OnBackPressedListener{
    companion object{
        fun newInstance()=NotiAppBar()
    }

    private lateinit var binding: NotiAppbarBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=DataBindingUtil.inflate(
            inflater,
            R.layout.noti_appbar,
            container,
            false
        )
        binding.fragment=this
        return binding.root
    }

    fun backClick(view:View){
        replaceAppbarFragment(HomeAppBar.newInstance())
        replaceMainFragment(Home.newInstance())
    }

    fun setClick(view:View){
        replaceAppbarFragment(NotiSetAppbar.newInstance())
        replaceMainFragment(NotiSet.newInstance())
    }

    override fun onBackPressed() {
        replaceAppbarFragment(HomeAppBar.newInstance())
        replaceMainFragment(Home.newInstance())
    }
}
