package com.goodchoice.android.ohneulen.ui.home.noti

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.NotiSetAppbarBinding
import com.goodchoice.android.ohneulen.util.OnBackPressedListener
import com.goodchoice.android.ohneulen.util.replaceAppbarFragment
import com.goodchoice.android.ohneulen.util.replaceMainFragment

class NotiSetAppbar :Fragment(),OnBackPressedListener{

    companion object{
        fun newInstance()=NotiSetAppbar()
    }

    private lateinit var binding:NotiSetAppbarBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=DataBindingUtil.inflate(
            inflater,
            R.layout.noti_set_appbar,
            container,
            false
        )

        binding.fragment=this
        return binding.root
    }

    fun backClick(view:View){
        replaceAppbarFragment(NotiAppBar.newInstance())
        replaceMainFragment(Noti.newInstance())
    }

    override fun onBackPressed() {
        replaceAppbarFragment(NotiAppBar.newInstance())
        replaceMainFragment(Noti.newInstance())
    }
}