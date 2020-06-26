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
import java.util.*
import kotlin.collections.HashMap


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
//        searchViewModel.categoryList.observe(viewLifecycleOwner,
//            Observer {
//
//            })


//        //메인카테고리 클릭시 서브카테고리 변경
        searchViewModel.mainCategoryPosition.observe(viewLifecycleOwner,
            Observer {
                searchViewModel.subCategory.postValue(searchViewModel.categoryList.value!![it].subCategoryList)
            })
        //체크리스트 저장
        val filterViewHashMap = HashMap<String, View>()
        val filterMainPositionHashMap=HashMap<String,Int>()
        val filterSubPositionHashMap=HashMap<String,Int>()
        var check=true
        searchViewModel.categoryList.observe(viewLifecycleOwner,
            Observer {
                searchViewModel.subCategory.value =
                    searchViewModel.categoryList.value!![searchViewModel.mainCategoryPosition.value!!].subCategoryList
                if(!check){
                    check=true
                    return@Observer
                }
                val index = searchViewModel.subCategoryPosition
                val filterName = searchViewModel.subCategory.value!![index].minorName
                val filterCode = searchViewModel.subCategory.value!![index].minorCode
                if (searchViewModel.subCategory.value!![index].check) {
                    val layoutInflater = this.layoutInflater
                    val selectView = layoutInflater.inflate(R.layout.filter_selecter, null)
                    selectView.findViewById<TextView>(R.id.filter_select_title).text =
                        filterName
                    filterMainPositionHashMap[filterCode]=searchViewModel.mainCategoryPosition.value!!
                    filterSubPositionHashMap[filterCode]=searchViewModel.subCategoryPosition
                    selectView.setOnClickListener {
                        check=false
                        val categoryList = searchViewModel.categoryList.value
                        categoryList!![filterMainPositionHashMap[filterCode]!!].subCategoryList[filterSubPositionHashMap[filterCode]!!].check=false
//                        Timber.e(filterMainPositionHashMap[filterCode].toString()+","+filterSubPositionHashMap[filterCode])
                        filterMainPositionHashMap.remove(filterCode)
                        filterSubPositionHashMap.remove(filterCode)
                        binding.searchFilterSelect.removeView(selectView)
                        searchViewModel.categoryList.value = categoryList
                    }
                    binding.searchFilterSelect.addView(selectView)
                    filterViewHashMap[filterCode] = selectView
                } else {
                    if (filterViewHashMap[filterCode] != null) {
                        binding.searchFilterSelect.removeView(filterViewHashMap[filterCode])
                        filterMainPositionHashMap.remove(filterCode)
                        filterSubPositionHashMap.remove(filterCode)
                        filterViewHashMap.remove(filterCode)
                    }
                }
            })
//        searchViewModel.subCategoryPosition.observe(viewLifecycleOwner,
//            Observer {
//                val filterName = searchViewModel.subCategory.value!![it].minorName
//                val filterCode = searchViewModel.subCategory.value!![it].minorCode
//                if (searchViewModel.categoryList[searchViewModel.mainCategoryPosition.value!!].subCategoryList[searchViewModel.subCategoryPosition.value!!].check) {
//
//                } else {
//
//                }
//            })
//
//        //서브카테고리 클릭시 데이터 쌓기+ View 생성
//        var observerCheck = false
//        val filterViewHashMap=HashMap<String,View>()
//        searchViewModel.subCategoryPosition.observe(viewLifecycleOwner,
//            Observer {
//                if (observerCheck) {
//                    val filterName = searchViewModel.subCategory.value!![it].minorName
//                    val filterCode = searchViewModel.subCategory.value!![it].minorCode
//
//                    if(filterViewHashMap[filterCode]==null) {
//                        Timber.e(filterViewHashMap[filterCode].toString())
//                        val layoutInflater = this.layoutInflater
//                        val selectView = layoutInflater.inflate(R.layout.filter_selecter, null)
//                        selectView.findViewById<TextView>(R.id.filter_select_title).text =
//                            filterName
//
//                        //뷰 클릭시 삭제
//                        selectView.setOnClickListener {
//                            filterViewHashMap.remove(filterCode)
//                            binding.searchFilterSelect.removeView(selectView)
//                        }
//                        binding.searchFilterSelect.addView(selectView)
//                        filterViewHashMap[filterCode]=selectView
//                    }
//                    else{
//                        Timber.e(filterViewHashMap[filterCode].toString())
//                        binding.searchFilterSelect.removeView(filterViewHashMap[filterCode])
//                    }
//                }
//                observerCheck = true
//            })

        //뒤에 레이아웃 터치 안먹게 하기
        binding.searchFilter.setOnTouchListener { v, event -> true }
    }

}