package com.goodchoice.android.ohneulen.ui.noti

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.NotiFragmentBinding

class NotiFragment :Fragment(){

    companion object{
        fun newInstance()=NotiFragment()
    }

    private lateinit var binding:NotiFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=DataBindingUtil.inflate(
            inflater,
            R.layout.noti_fragment,
            container,
            false
        )

        return binding.root
    }
}