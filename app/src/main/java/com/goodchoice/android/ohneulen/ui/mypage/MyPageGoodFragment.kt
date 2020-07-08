package com.goodchoice.android.ohneulen.ui.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.MypageGoodFragmentBinding
import com.goodchoice.android.ohneulen.ui.search.SearchAppBarFragment
import com.goodchoice.android.ohneulen.ui.search.SearchFragment
import com.goodchoice.android.ohneulen.util.replaceAppbarFragment
import com.goodchoice.android.ohneulen.util.replaceMainFragment

class MyPageGoodFragment :Fragment(){
    companion object{
        fun newInstance()=MyPageGoodFragment()
    }
    private lateinit var binding:MypageGoodFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=DataBindingUtil.inflate(
            inflater,
            R.layout.mypage_good_fragment,
            container,
            false
        )
        binding.fragment=this
        return binding.root
    }
    fun moveSearch(view:View){
        replaceMainFragment(SearchFragment.newInstance())
        replaceAppbarFragment(SearchAppBarFragment.newInstance())
    }
}