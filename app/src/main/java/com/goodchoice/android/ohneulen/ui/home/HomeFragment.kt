package com.goodchoice.android.ohneulen.ui.home

import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.ui.MainViewModel
import com.goodchoice.android.ohneulen.databinding.HomeFragmentBinding
import com.goodchoice.android.ohneulen.ui.search.SearchAppBarFragment
import com.goodchoice.android.ohneulen.ui.search.SearchFragment
import com.goodchoice.android.ohneulen.util.constant.ConstList
import com.goodchoice.android.ohneulen.util.replaceAppbarFragment
import com.goodchoice.android.ohneulen.util.replaceMainFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class HomeFragment() : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private val mainViewModel: MainViewModel by viewModel()
    private lateinit var binding: HomeFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.home_fragment,
            container,
            false
        )
        binding.fragment = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.homeEditText.setOnEditorActionListener { v, actionId, _ ->
            if (v!!.id == R.id.home_editText && actionId == EditorInfo.IME_ACTION_SEARCH) {
                mainViewModel.searchEditText = binding.homeEditText.text.toString()
                replaceAppbarFragment(SearchAppBarFragment.newInstance())
                replaceMainFragment(SearchFragment.newInstance(), name = "search")
            }

            return@setOnEditorActionListener false
        }
    }

    fun searchOnClick(view: View) {
        mainViewModel.searchEditText = binding.homeEditText.text.toString()
        replaceAppbarFragment(SearchAppBarFragment.newInstance())
        replaceMainFragment(SearchFragment.newInstance(), name = "search")
    }

    fun currentLocationClick(view: View) {
        mainViewModel.searchEditText = ConstList.CURRENT_LOCATION
        replaceAppbarFragment(SearchAppBarFragment.newInstance())
        replaceMainFragment(SearchFragment.newInstance())

    }


}
