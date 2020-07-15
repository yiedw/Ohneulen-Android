package com.goodchoice.android.ohneulen.ui.store.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.StoreHomeReportAppbarFragmentBinding
import com.goodchoice.android.ohneulen.ui.MainActivity
import com.goodchoice.android.ohneulen.ui.store.StoreAppBar
import com.goodchoice.android.ohneulen.util.OnBackPressedListener
import com.goodchoice.android.ohneulen.util.replaceAppbarFragment

class StoreHomeReportAppBar :Fragment(),OnBackPressedListener{
    companion object{
        fun newInstance()=StoreHomeReportAppBar()
    }

    private lateinit var binding:StoreHomeReportAppbarFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=DataBindingUtil.inflate(
            inflater,
            R.layout.store_home_report_appbar_fragment,
            container,
            false
        )
        binding.fragment=this
        return binding.root
    }

    fun onCloseClick(view:View){
        replaceAppbarFragment(StoreAppBar.newInstance(), tag = "storeAppBar")
        MainActivity.supportFragmentManager.popBackStack()
//        Timber.e(MainActivity.supportFragmentManager.fragments.toString())
//        (MainActivity.supportFragmentManager.findFragmentByTag("storeAppBar") as StoreAppBarFragment).changeBlack()
    }

    override fun onBackPressed() {
        replaceAppbarFragment(StoreAppBar.newInstance(), tag = "storeAppBar")
        MainActivity.supportFragmentManager.popBackStack()
    }
}