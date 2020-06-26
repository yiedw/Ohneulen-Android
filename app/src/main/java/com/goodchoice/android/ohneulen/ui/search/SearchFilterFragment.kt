package com.goodchoice.android.ohneulen.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.SearchFilterFragmentBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import kotlin.collections.HashMap


class SearchFilterFragment : Fragment() {
    companion object {
        fun newInstance() = SearchFilterFragment()
    }

    private val searchViewModel: SearchViewModel by viewModel()
    private lateinit var binding: SearchFilterFragmentBinding
    private var filterViewHashMap = HashMap<String, View>()
    private var filterMainPositionHashMap = HashMap<String, Int>()
    private var filterSubPositionHashMap = HashMap<String, Int>()
    var check = false


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


            filterViewHashMap = searchViewModel.filterViewHashMap
            filterMainPositionHashMap = searchViewModel.filterMainPositionHashMap
            filterSubPositionHashMap = searchViewModel.filterSubPositionHashMap

            val reverse=filterViewHashMap.keys.reversed()
            for (i in reverse) {
                val filterMainPosition = filterMainPositionHashMap[i]
                val filterSubPosition = filterSubPositionHashMap[i]
                val filterName =
                    searchViewModel.categoryList.value!![filterMainPosition!!].subCategoryList[filterSubPosition!!].minorName

                val layoutInflater = this.layoutInflater
                val selectView = layoutInflater.inflate(R.layout.filter_selecter, null)
                selectView.findViewById<TextView>(R.id.filter_select_title).text =
                    filterName

                selectView.setOnClickListener {
                    check = false
                    val categoryList = searchViewModel.categoryList.value
                    categoryList!![filterMainPositionHashMap[i]!!].subCategoryList[filterSubPositionHashMap[i]!!].check =
                        false
//                        Timber.e(filterMainPositionHashMap[filterCode].toString()+","+filterSubPositionHashMap[filterCode])
                    filterMainPositionHashMap.remove(i)
                    filterSubPositionHashMap.remove(i)
                    filterViewHashMap.remove(i)
                    binding.searchFilterSelect.removeView(selectView)
                    searchViewModel.categoryList.value = categoryList
                }
                binding.searchFilterSelect.addView(selectView)
            }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        App.categorySwitch.observe(
//            viewLifecycleOwner, Observer {
//            }
//        )

//        //메인카테고리 클릭시 서브카테고리 변경
        searchViewModel.mainCategoryPosition.observe(viewLifecycleOwner,
            Observer {
                searchViewModel.subCategory.postValue(searchViewModel.categoryList.value!![it].subCategoryList)
            })
        //체크리스트 저장
        searchViewModel.categoryList.observe(viewLifecycleOwner,
            Observer {
                searchViewModel.subCategory.value =
                    searchViewModel.categoryList.value!![searchViewModel.mainCategoryPosition.value!!].subCategoryList
                if (!check) {
                    check = true
                    return@Observer
                }
                val index = searchViewModel.subCategoryPosition
                val filterCode = searchViewModel.subCategory.value!![index].minorCode
                val filterName = searchViewModel.subCategory.value!![index].minorName
                makeSelectView(filterCode, filterName, index)

            })

        //초기화 클릭
        binding.searchFilterReset.setOnClickListener {
            val categoryList = searchViewModel.categoryList.value!!
            for (i in filterViewHashMap.keys) {
                categoryList[filterMainPositionHashMap[i]!!].subCategoryList[filterSubPositionHashMap[i]!!].check =
                    false
            }
            searchViewModel.categoryList.postValue(categoryList)
            filterViewHashMap.clear()
            filterMainPositionHashMap.clear()
            filterSubPositionHashMap.clear()
            binding.searchFilterSelect.removeAllViews()
        }

        //선택완료 클릭
        binding.searchFilterComplete.setOnClickListener {
            Timber.e(filterViewHashMap.keys.toString())
        }

        //뒤에 레이아웃 터치 안먹게 하기
        binding.searchFilter.setOnTouchListener { v, event -> true }
    }

    override fun onDestroy() {
        super.onDestroy()
        searchViewModel.filterViewHashMap=filterViewHashMap
        searchViewModel.filterMainPositionHashMap=filterMainPositionHashMap
        searchViewModel.filterSubPositionHashMap=filterSubPositionHashMap
        binding.searchFilterSelect.removeAllViewsInLayout()

    }

    //체크리스트 뷰 만들기
    private fun makeSelectView(filterCode: String, filterName: String, index: Int) {

        if (searchViewModel.subCategory.value!![index].check) {
            val layoutInflater = this.layoutInflater
            val selectView = layoutInflater.inflate(R.layout.filter_selecter, null)
            selectView.findViewById<TextView>(R.id.filter_select_title).text =
                filterName
            filterMainPositionHashMap[filterCode] =
                searchViewModel.mainCategoryPosition.value!!
            filterSubPositionHashMap[filterCode] = searchViewModel.subCategoryPosition
            selectView.setOnClickListener {
                check = false
                val categoryList = searchViewModel.categoryList.value
                categoryList!![filterMainPositionHashMap[filterCode]!!].subCategoryList[filterSubPositionHashMap[filterCode]!!].check =
                    false
//                        Timber.e(filterMainPositionHashMap[filterCode].toString()+","+filterSubPositionHashMap[filterCode])
                filterMainPositionHashMap.remove(filterCode)
                filterSubPositionHashMap.remove(filterCode)
                filterViewHashMap.remove(filterCode)
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
    }

}