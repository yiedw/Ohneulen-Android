package com.goodchoice.android.ohneulen.ui.home.noti

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.NotiSetAppbarFragmentBinding
import com.goodchoice.android.ohneulen.util.replaceAppbarFragment
import com.goodchoice.android.ohneulen.util.replaceMainFragment

class NotiSetAppbarFragment :Fragment(){

    companion object{
        fun newInstance()=NotiSetAppbarFragment()
    }

    private lateinit var binding:NotiSetAppbarFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=DataBindingUtil.inflate(
            inflater,
            R.layout.noti_set_appbar_fragment,
            container,
            false
        )

        binding.fragment=this
        return binding.root
    }

    fun backClick(view:View){
        replaceAppbarFragment(NotiAppBarFragment.newInstance())
        replaceMainFragment(NotiFragment.newInstance())
    }
}