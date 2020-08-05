package com.goodchoice.android.ohneulen.ui.search

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.SearchAppbarBinding
import com.goodchoice.android.ohneulen.ui.MainViewModel
import com.goodchoice.android.ohneulen.ui.home.HomeAppBar
import com.goodchoice.android.ohneulen.ui.home.Home
import com.goodchoice.android.ohneulen.util.OnBackPressedListener
import com.goodchoice.android.ohneulen.util.addMainFragment
import com.goodchoice.android.ohneulen.util.constant.ConstList
import com.goodchoice.android.ohneulen.util.replaceAppbarFragment
import com.goodchoice.android.ohneulen.util.replaceMainFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchAppBar : Fragment() {

    companion object {
        fun newInstance() = SearchAppBar()
    }

    private lateinit var binding: SearchAppbarBinding
    private val searchViewModel: SearchViewModel by viewModel()
    private val mainViewModel: MainViewModel by viewModel()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.search_appbar,
            container,
            false
        )
        if (mainViewModel.searchEditText != ConstList.CURRENT_LOCATION) {
            if (mainViewModel.searchEditText.isBlank())
                mainViewModel.searchEditText = "강남역"

            searchViewModel.searchEditText = mainViewModel.searchEditText
            searchViewModel.searchMapData()
            binding.searchAppbarEt.setText(mainViewModel.searchEditText)
        }
        binding.fragment = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchAppbarEt.setOnEditorActionListener { v, actionId, event ->
            if (!binding.searchAppbarEt.text.toString().isBlank()) {
                val imm: InputMethodManager =
                    requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.searchAppbarEt.windowToken, 0)
                mainViewModel.searchEditText = binding.searchAppbarEt.text.toString()
                searchViewModel.searchMapData()
            } else {
                Toast.makeText(requireContext(), "검색어를 입력해주세요", Toast.LENGTH_LONG)
                    .show()
            }
            return@setOnEditorActionListener false
        }

        binding.searchAppbarEt.addTextChangedListener(object:TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                if (binding.searchAppbarEt.text.toString().isEmpty()) {
                    binding.searchAppbarClear.visibility = View.GONE
                } else {
                    binding.searchAppbarClear.visibility = View.VISIBLE
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }

    fun submitClick(view: View) {
        searchViewModel.searchEditText = binding.searchAppbarEt.text.toString()
        searchViewModel.searchMapData()
    }

    fun clearClick(view: View) {
        binding.searchAppbarEt.text.clear()
    }

    fun filterClick(view: View) {
        val imm=requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken,0)
        replaceAppbarFragment(SearchFilterAppbar.newInstance())
        addMainFragment(SearchFilter.newInstance(), true)
    }

}