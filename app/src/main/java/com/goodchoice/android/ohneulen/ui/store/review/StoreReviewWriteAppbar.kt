package com.goodchoice.android.ohneulen.ui.store.review

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.StoreReviewWriteAppbarBinding
import com.goodchoice.android.ohneulen.ui.MainActivity
import com.goodchoice.android.ohneulen.util.OnBackPressedListener

class StoreReviewWriteAppbar:Fragment(),OnBackPressedListener {
    companion object{
        fun newInstance()=
            StoreReviewWriteAppbar()
    }
    private lateinit var binding:StoreReviewWriteAppbarBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=DataBindingUtil.inflate(
            inflater,
            R.layout.store_review_write_appbar,
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

    override fun onBackPressed() {
        MainActivity.supportFragmentManager.popBackStack()
        MainActivity.supportFragmentManager.popBackStack()
    }
}