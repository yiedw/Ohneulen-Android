package com.goodchoice.android.ohneulen.ui.like

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.LikeAppbarBinding

class LikeAppBar : Fragment() {

    companion object{
        fun newInstance()=LikeAppBar()
    }

    private lateinit var binding: LikeAppbarBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.like_appbar,
            container,
            false
        )
        return binding.root
    }
}