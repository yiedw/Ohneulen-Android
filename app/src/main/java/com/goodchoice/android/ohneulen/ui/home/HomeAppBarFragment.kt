package com.goodchoice.android.ohneulen.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.goodchoice.android.ohneulen.R

class HomeAppBarFragment :Fragment(){

    companion object{
        fun newInstance()=HomeAppBarFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.home_appbar_fragment,container,false)
        return view
    }
}