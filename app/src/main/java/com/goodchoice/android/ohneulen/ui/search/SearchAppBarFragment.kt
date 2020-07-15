package com.goodchoice.android.ohneulen.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.SearchAppbarFragmentBinding
import com.goodchoice.android.ohneulen.ui.home.HomeAppBarFragment
import com.goodchoice.android.ohneulen.ui.home.HomeFragment
import com.goodchoice.android.ohneulen.util.OnBackPressedListener
import com.goodchoice.android.ohneulen.util.replaceAppbarFragment
import com.goodchoice.android.ohneulen.util.replaceMainFragment

class SearchAppBarFragment : Fragment() ,OnBackPressedListener{

    companion object {
        fun newInstance() = SearchAppBarFragment()
    }

    private lateinit var binding: SearchAppbarFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.search_appbar_fragment,
            container,
            false
        )
        binding.fragment = this
        return binding.root
    }

    fun backClick(view: View) {
        replaceAppbarFragment(HomeAppBarFragment.newInstance())
        replaceMainFragment(HomeFragment.newInstance())
    }

    override fun onBackPressed() {
        replaceAppbarFragment(HomeAppBarFragment.newInstance())
        replaceMainFragment(HomeFragment.newInstance())
    }
}