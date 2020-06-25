package com.goodchoice.android.ohneulen.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListAdapter
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.goodchoice.android.ohneulen.App
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.SearchFilterFragmentBinding
import com.goodchoice.android.ohneulen.ui.MainViewModel
import kotlinx.android.synthetic.main.filter_selecter.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber


class SearchFilterFragment : Fragment() {
    companion object {
        fun newInstance() = SearchFilterFragment()
    }

    private val searchViewModel: SearchViewModel by viewModel()
    private val mainViewModel: MainViewModel by viewModel()
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
            lifecycleOwner = this@SearchFilterFragment
            fragment = this@SearchFilterFragment
            viewModel = searchViewModel
        }


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        App.categorySwitch.observe(
            viewLifecycleOwner, Observer {
            }
        )
        //메인카테고리 클릭시 서브카테고리 변경
        searchViewModel.mainCategoryPosition.observe(viewLifecycleOwner,
            Observer {
                searchViewModel.subCategory.postValue(searchViewModel.categoryList[it].subCategoryList)
            })

        //서브카테고리 클릭시 데이터 쌓기+ View 생성
        var observerCheck = false
        searchViewModel.subCategoryPosition.observe(viewLifecycleOwner,
            Observer {
                if (observerCheck) {
                    val filterName = searchViewModel.subCategory.value!![it].minorName
                    Timber.e(filterName)
                    val layoutInflater = this.layoutInflater
                    val view = layoutInflater.inflate(R.layout.filter_selecter, null)
                    view.findViewById<TextView>(R.id.filter_select_title).text = filterName
                    binding.searchFilterSelect.addView(view)
                }
                observerCheck = true
            })

        //뒤에 레이아웃 터치 안먹게 하기
        binding.searchFilter.setOnTouchListener { v, event -> true }
    }

}