package com.goodchoice.android.ohneulen.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.goodchoice.android.ohneulen.MainActivity
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.MainViewModel
import com.goodchoice.android.ohneulen.ui.search.SearchAppBarFragment
import com.goodchoice.android.ohneulen.ui.search.SearchMainFragment
import kotlinx.android.synthetic.main.home_main_fragment.*

class HomeMainFragment() : Fragment() {

    companion object {
        fun newInstance() = HomeMainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.home_main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        home_main_fragment_search.setOnClickListener {
            MainActivity.supportFragmentManager.beginTransaction()
                .replace(R.id.appbar_fragment, SearchAppBarFragment.newInstance()).commitNow()
            MainActivity.supportFragmentManager.beginTransaction()
                .replace(R.id.main_fragment, SearchMainFragment.newInstance()).commitNow()
        }
    }

}
