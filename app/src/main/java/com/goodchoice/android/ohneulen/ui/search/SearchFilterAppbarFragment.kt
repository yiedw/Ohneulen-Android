package com.goodchoice.android.ohneulen.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.SearchFilterAppbarFragmentBinding
import com.goodchoice.android.ohneulen.ui.MainActivity
import com.goodchoice.android.ohneulen.util.replaceAppbarFragment
import com.goodchoice.android.ohneulen.util.replaceMainFragment

class SearchFilterAppbarFragment :Fragment(){

    companion object{
        fun newInstance()=SearchFilterAppbarFragment()
    }
    private lateinit var binding:SearchFilterAppbarFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=DataBindingUtil.inflate(
            inflater,
            R.layout.search_filter_appbar_fragment,
            container,
            false
        )
        binding.fragment=this
        return binding.root
    }

    fun closeOnClick(view:View){
//        Timber.e(MainActivity.supportFragmentManager.beginTransaction().isAddToBackStackAllowed.toString())
        replaceAppbarFragment(SearchAppBarFragment.newInstance())
        replaceMainFragment(SearchFragment.newInstance())
    }

}