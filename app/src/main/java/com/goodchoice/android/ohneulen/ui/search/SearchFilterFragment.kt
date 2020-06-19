package com.goodchoice.android.ohneulen.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.SearchFilterFragmentBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class SearchFilterFragment : Fragment() {
    companion object {
        fun newInstance() = SearchFilterFragment()
    }

    private val searchViewModel: SearchViewModel by viewModel()
    private lateinit var binding: SearchFilterFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.search_filter_fragment,
            container,
            false
        )
        binding.apply {
            lifecycleOwner=this@SearchFilterFragment
            fragment = this@SearchFilterFragment
            viewModel = searchViewModel
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}