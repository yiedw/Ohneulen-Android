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
import com.goodchoice.android.ohneulen.ui.MainActivity
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

        //바인딩
        binding.apply {
            lifecycleOwner = this@Search
            binding.fragment = this@Search
            viewModel = searchViewModel
        }
        binding
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility", "SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //맵 (삭제, 추가)
        val searchMapFragment = SearchMap.newInstance()
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

        searchViewModel.searchStoreList.observe(viewLifecycleOwner, Observer {
            binding.searchStoreAmount.text = "매장 ${it.size}"
        })
        MainActivity.bottomNav.visibility = View.VISIBLE

    }

    override fun onResume() {
        super.onResume()
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