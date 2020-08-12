package com.goodchoice.android.ohneulen.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.SearchFilterAppbarBinding
import com.goodchoice.android.ohneulen.ui.MainActivity
import com.goodchoice.android.ohneulen.util.OnBackPressedListener
import com.goodchoice.android.ohneulen.util.replaceAppbarFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFilterAppbar : Fragment(), OnBackPressedListener {

    companion object {
        fun newInstance() = SearchFilterAppbar()
    }

    private lateinit var binding: SearchFilterAppbarBinding
    private val searchViewModel: SearchViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.search_filter_appbar,
            container,
            false
        )
        binding.fragment = this
        return binding.root
    }

    fun closeOnClick(view: View) {
//        MainActivity.supportFragmentManager.popBackStack()
        replaceAppbarFragment(SearchAppBar.newInstance())
        MainActivity.supportFragmentManager.popBackStack()
    }

    override fun onBackPressed() {
        replaceAppbarFragment(SearchAppBar.newInstance())
        MainActivity.supportFragmentManager.popBackStack()
    }


}