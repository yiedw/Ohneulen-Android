package com.goodchoice.android.ohneulen.ui.store

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.StoreReviewWriteAppbarFragmentBinding
import com.goodchoice.android.ohneulen.ui.MainActivity
import com.goodchoice.android.ohneulen.util.addAppbarFragment
import com.goodchoice.android.ohneulen.util.replaceAppbarFragment

class StoreReviewWriteAppbarFragment:Fragment() {
    companion object{
        fun newInstance()=StoreReviewWriteAppbarFragment()
    }
    private lateinit var binding:StoreReviewWriteAppbarFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=DataBindingUtil.inflate(
            inflater,
            R.layout.store_review_write_appbar_fragment,
            container,
            false
        )
        binding.fragment=this
        return binding.root
    }

    fun closeOnClick(view:View){
        MainActivity.supportFragmentManager.popBackStack()
        MainActivity.supportFragmentManager.popBackStack()
    }
}