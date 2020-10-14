package com.goodchoice.android.ohneulen.ui.search

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.SearchAppbarBinding
import com.goodchoice.android.ohneulen.ui.MainActivity
import com.goodchoice.android.ohneulen.ui.MainViewModel
import com.goodchoice.android.ohneulen.ui.store.StoreAppBar
import com.goodchoice.android.ohneulen.util.OnBackPressedListener
import com.goodchoice.android.ohneulen.util.replaceAppbarFragment
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchAppBar(private var back: Boolean) : Fragment(), OnBackPressedListener {

    companion object {
        fun newInstance(back: Boolean = false) = SearchAppBar(back)
    }

    private lateinit var binding: SearchAppbarBinding
    private val searchViewModel: SearchViewModel by inject()
    private val mainViewModel: MainViewModel by viewModel()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        //store stat 설정
        StoreAppBar.stat = 1
    }


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

        if (!mainViewModel.currentLocationSearch) {
            if (searchViewModel.searchAppbarFirst || mainViewModel.searchCheck) {
                if (mainViewModel.searchEditText.isEmpty()) {
                    mainViewModel.searchEditText = "강남역"
                }
                searchViewModel.searchEditText = mainViewModel.searchEditText
                searchViewModel.getSearchMapData()
                searchViewModel.searchAppbarFirst = false
                mainViewModel.searchCheck = false

            }
            binding.searchAppbarEt.setText(mainViewModel.searchEditText)
        }

        searchViewModel.searchAppbarFirst=false

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
                searchViewModel.searchEditText = binding.searchAppbarEt.text.toString()
                searchViewModel.getSearchMapData()
            } else {
                Toast.makeText(requireContext(), "검색어를 입력해주세요", Toast.LENGTH_LONG)
                    .show()
            }
            return@setOnEditorActionListener false
        }

        binding.searchAppbarEt.addTextChangedListener(object : TextWatcher {
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
        if (!binding.searchAppbarEt.text.toString().isBlank()) {
            val imm: InputMethodManager =
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(binding.searchAppbarEt.windowToken, 0)
            mainViewModel.searchEditText = binding.searchAppbarEt.text.toString()
            searchViewModel.searchEditText = binding.searchAppbarEt.text.toString()
            searchViewModel.getSearchMapData()
        } else {
            Toast.makeText(requireContext(), "검색어를 입력해주세요", Toast.LENGTH_LONG)
                .show()
        }

    }

    fun clearClick(view: View) {
        binding.searchAppbarEt.text.clear()
    }

    fun filterClick(view: View) {
        mainViewModel.searchEditText = binding.searchAppbarEt.text.toString()
        searchViewModel.searchEditText = binding.searchAppbarEt.text.toString()
        val imm =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
        MainActivity.supportFragmentManager.beginTransaction().setCustomAnimations(
            R.anim.enter_right_to_left,
            R.anim.exit_left_to_right,
            R.anim.exit_left_to_right,
            R.anim.exit_left_to_right
        ).add(MainActivity.mainFrameLayout.id, SearchFilter.newInstance())
            .addToBackStack(null)
            .commit()
        replaceAppbarFragment(SearchFilterAppbar.newInstance())
    }

    override fun onBackPressed() {
        MainActivity.bottomNav.selectedItemId = R.id.menu_bottom_nav_home
    }

}