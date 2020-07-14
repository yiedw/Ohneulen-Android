package com.goodchoice.android.ohneulen.ui.search

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.goodchoice.android.ohneulen.ui.MainViewModel
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.SearchFragmentBinding
import com.goodchoice.android.ohneulen.util.*
import com.goodchoice.android.ohneulen.util.constant.ConstList
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    companion object {
        fun newInstance() = SearchFragment()
    }

    private var switchOn = false
    private lateinit var binding: SearchFragmentBinding
    private val searchViewModel: SearchViewModel by viewModel()
    private val mainViewModel: MainViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.search_fragment,
                container,
                false
            )
        //mvm 을 이용해서 데이터 받아오기
        searchViewModel.searchEditText = mainViewModel.searchEditText

        //검색어기반
        if (searchViewModel.searchEditText != ConstList.CURRENT_LOCATION) {
            binding.searchEditText.setText(mainViewModel.searchEditText)
            if (!binding.searchEditText.text.toString().isBlank()) {
                //검색어를 이용해서 지도정보 불러오기
                searchViewModel.searchMapData()
            } else {
                Toast.makeText(requireContext(), "검색어를 입력해주세요", Toast.LENGTH_SHORT).show()
            }
        }


        //바인딩
        binding.apply {
            lifecycleOwner = this@SearchFragment
            binding.fragment = this@SearchFragment
            viewModel = searchViewModel
        }
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //맵 (삭제, 추가)
        val searchMapFragment = SearchMapFragment.newInstance()
        childFragmentManager.beginTransaction()
            .replace(R.id.search_map, searchMapFragment).commit()


        //검색어 없을시 토스트 띄우기
        searchViewModel.toastMessage.observe(
            viewLifecycleOwner, Observer {
                if (searchViewModel.toastMessage.value!!) {
                    Toast.makeText(requireContext(), "검색결과가 없습니다", Toast.LENGTH_SHORT).show()
                    searchViewModel.toastMessage = MutableLiveData(false)
                }
            }
        )

        binding.searchEditText.setOnEditorActionListener { v, actionId, event ->
            if (v!!.id == R.id.search_editText && actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (!binding.searchEditText.text.toString().isBlank()) {
                    val imm: InputMethodManager =
                        requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(binding.searchEditText.windowToken, 0)
                    searchViewModel.searchEditText = binding.searchEditText.text.toString()
                    mainViewModel.searchEditText = binding.searchEditText.text.toString()
                    searchViewModel.searchMapData()
                } else {
                    Toast.makeText(requireContext(), "검색어를 입력해주세요", Toast.LENGTH_LONG)
                        .show()
                }
            }
            return@setOnEditorActionListener false
        }


    }


    fun searchClick(view: View) {
        if (!binding.searchEditText.text.toString().isBlank()) {
            searchViewModel.searchEditText = binding.searchEditText.text.toString()
            mainViewModel.searchEditText = binding.searchEditText.text.toString()
            searchViewModel.searchMapData()
        } else {
            Toast.makeText(requireContext(), "검색어를 입력해주세요", Toast.LENGTH_LONG).show()
        }


    }

    fun switchClick(view: View) {
        if (!switchOn)
            binding.searchMap.visibility = View.GONE
        else
            binding.searchMap.visibility = View.VISIBLE

        switchOn = !switchOn
    }

    fun filterClick(view: View) {
        replaceAppbarFragment(SearchFilterAppbarFragment.newInstance())
        addMainFragment(SearchFilterFragment.newInstance(), true)
    }

//    override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
//        if (v!!.id == R.id.search_editText && actionId == EditorInfo.IME_ACTION_SEARCH) {
//            if (!binding.searchEditText.text.toString().isBlank()) {
//                val imm:InputMethodManager=requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//                imm.hideSoftInputFromWindow(et)
//                searchViewModel.searchEditText = binding.searchEditText.text.toString()
//                mainViewModel.searchEditText = binding.searchEditText.text.toString()
//                searchViewModel.searchMapData()
//            } else {
//                Toast.makeText(requireContext(), "검색어를 입력해주세요", Toast.LENGTH_LONG).show()
//            }
//        }
//        return false
//    }


}