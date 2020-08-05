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
import com.bumptech.glide.Glide
import com.goodchoice.android.ohneulen.ui.MainViewModel
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.SearchBinding
import com.goodchoice.android.ohneulen.util.*
import com.goodchoice.android.ohneulen.util.constant.ConstList
import org.koin.androidx.viewmodel.ext.android.viewModel

class Search : Fragment() {

    companion object {
        fun newInstance() = Search()
    }

    private var switchOn = false
    private lateinit var binding: SearchBinding
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
                R.layout.search,
                container,
                false
            )
        //mvm 을 이용해서 데이터 받아오기
//        searchViewModel.searchEditText = mainViewModel.searchEditText
//
//        검색어기반
//        if (searchViewModel.searchEditText != ConstList.CURRENT_LOCATION) {
//            binding.searchEditText.setText(mainViewModel.searchEditText)
//            if (!binding.searchEditText.text.toString().isBlank()) {
//                //검색어를 이용해서 지도정보 불러오기
//                searchViewModel.searchMapData()
//            } else {
//                Toast.makeText(requireContext(), "검색어를 입력해주세요", Toast.LENGTH_SHORT).show()
//            }
//        }


        //바인딩
        binding.apply {
            lifecycleOwner = this@Search
            binding.fragment = this@Search
            viewModel = searchViewModel
        }
        //맵 (삭제, 추가)
        val searchMapFragment = SearchMap.newInstance()
        childFragmentManager.beginTransaction()
            .replace(R.id.search_map, searchMapFragment).commit()
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility", "SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)





        //검색어 없을시 토스트 띄우기
        searchViewModel.toastMessage.observe(
            viewLifecycleOwner, Observer {
                if (searchViewModel.toastMessage.value!!) {
                    Toast.makeText(requireContext(), "검색결과가 없습니다", Toast.LENGTH_SHORT).show()
                    searchViewModel.toastMessage = MutableLiveData(false)
                }
            }
        )

        searchViewModel.searchStoreList.observe(viewLifecycleOwner, Observer {
            binding.searchStoreAmount.text="매장 ${it.size}"
        })
    }


    fun switchClick(view: View) {
        if (!switchOn) {
            binding.searchMap.visibility = View.GONE
            Glide.with(requireContext()).load(R.drawable.open).into(binding.searchSwitch)
        } else {
            binding.searchMap.visibility = View.VISIBLE
            Glide.with(requireContext()).load(R.drawable.close).into(binding.searchSwitch)
        }
        switchOn = !switchOn
    }


}