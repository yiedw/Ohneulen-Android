package com.goodchoice.android.ohneulen.ui.search

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.goodchoice.android.ohneulen.ui.MainViewModel
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.SearchFragmentBinding
import com.goodchoice.android.ohneulen.ui.MainActivity
import com.goodchoice.android.ohneulen.util.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

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
        Timber.e(searchViewModel.toString())

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
        var searchMapFragment = SearchMapFragment()
        MainActivity.searchMapView.observe(viewLifecycleOwner,
            Observer {
                if (it) {
                    CoroutineScope(Dispatchers.Main).launch {
                        searchMapFragment = SearchMapFragment()
                        childFragmentManager.beginTransaction()
                            .replace(R.id.search_map, searchMapFragment).commit()
                    }
                } else {
                    childFragmentManager.beginTransaction()
                        .remove(searchMapFragment).commit()
                }
            })


        //검색어 없을시 토스트 띄우기
        searchViewModel.toastMessage.observe(
            viewLifecycleOwner, Observer {
                if (searchViewModel.toastMessage.value!!) {
                    Toast.makeText(requireContext(), "검색결과가 없습니다", Toast.LENGTH_SHORT).show()
                    searchViewModel.toastMessage = MutableLiveData(false)
                }
            }
        )


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
        addAppbarFragment(SearchFilterAppbarFragment.newInstance(), true)
        addMainFragment(SearchFilterFragment.newInstance(), true)
    }


}