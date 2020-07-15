package com.goodchoice.android.ohneulen.ui.home.noti

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.NotiSetFragmentBinding

class NotiSet :Fragment(){
    companion object{
        fun newInstance()=NotiSet()
    }
    private lateinit var binding:NotiSetFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=DataBindingUtil.inflate(
            inflater,
            R.layout.noti_set_fragment,
            container,
            false
        )
        return binding.root
    }
}