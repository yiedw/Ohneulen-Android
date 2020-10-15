package com.goodchoice.android.ohneulen.ui.search

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.data.repository.InitData
import com.goodchoice.android.ohneulen.databinding.SearchFilterAppbarBinding
import com.goodchoice.android.ohneulen.ui.MainActivity
import com.goodchoice.android.ohneulen.util.OnBackPressedListener
import com.goodchoice.android.ohneulen.util.replaceAppbarFragment
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class SearchFilterAppbar : Fragment(), OnBackPressedListener {

    companion object {
        fun newInstance() = SearchFilterAppbar()
    }

    private lateinit var binding: SearchFilterAppbarBinding
    private val searchViewModel: SearchViewModel by viewModel()
    private val initData: InitData by inject()
    private var tempCate = mutableListOf<String>()
    private var tempOption = mutableListOf<String>()
    private var tempOpenTime = mutableListOf<String>()
    private var tempSort = mutableListOf<String>()
    private var tempRating = false
    private var tempRecent = false


    override fun onAttach(context: Context) {
        super.onAttach(context)
        dataSave()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.search_filter_appbar,
            container,
            false
        )
        binding.fragment = this
        return binding.root
    }

    fun closeOnClick(view: View) {
//        MainActivity.supportFragmentManager.popBackStack()
        dataRevert()
        //전에 검색화면을 남겨둬야함
        replaceAppbarFragment(SearchAppBar.newInstance())
        MainActivity.supportFragmentManager.popBackStack()
        MainActivity.bottomNav.visibility = View.VISIBLE
    }

    override fun onBackPressed() {
        dataRevert()
        replaceAppbarFragment(SearchAppBar.newInstance())
        MainActivity.supportFragmentManager.popBackStack()
        MainActivity.bottomNav.visibility = View.VISIBLE
    }

    private fun dataSave() {
        tempCate = deepCopy(searchViewModel.cate)
        tempOption = deepCopy(searchViewModel.option)
        tempOpenTime = deepCopy(searchViewModel.openTime)
        tempSort = deepCopy(searchViewModel.sort)
        tempRating = searchViewModel.checkSortRating
        tempRecent = searchViewModel.checkSortRecent

    }

    private fun dataRevert() {
//        Timber.e(searchViewModel.option.toString())
//        Timber.e(tempCate.toString())
        searchViewModel.cate = tempCate
        searchViewModel.option = tempOption
        searchViewModel.openTime = tempOpenTime
        searchViewModel.sort = tempSort
        searchViewModel.tempCateOhneulenData.clear()

        //카테고리(음식)
        for (i in initData.subCategoryList.indices) {
            for (j in initData.subCategoryList[i].indices) {
                for (k in tempCate.indices) {
                    if (initData.subCategoryList[i][j].majorCode + initData.subCategoryList[i][j].minorCode == tempCate[k]) {
                        initData.subCategoryList[i][j].check = true
                        searchViewModel.tempCateOhneulenData.add(initData.subCategoryList[i][j])
                        break
                    } else if (k == tempCate.size - 1) {
                        initData.subCategoryList[i][j].check = false
                    }
                }
                if (tempCate.size == 0) {
                    initData.subCategoryList[i][j].check = false
                }
            }
        }

        //옵션
        for (i in initData.mainOptionKind.indices) {
            for (j in searchViewModel.option.indices) {
                if (initData.mainOptionKind[i].minorCode == searchViewModel.option[j]) {
                    initData.mainOptionKind[i].check = true
                    break
                } else if (j == searchViewModel.option.size - 1) {
                    initData.mainOptionKind[i].check = false
                }
            }
            if (searchViewModel.option.size == 0) {
                initData.mainOptionKind[i].check = false
            }
        }

        //영업일
        for (i in initData.timeDay.indices) {
            for (j in searchViewModel.openTime.indices) {
                if (initData.timeDay[i].minorCode == searchViewModel.openTime[j]) {
                    initData.timeDay[i].check = true
                    break
                } else if (j == searchViewModel.openTime.size - 1) {
                    initData.timeDay[i].check = false
                }
            }
            if (searchViewModel.openTime.size == 0) {
                initData.timeDay[i].check = false
            }
        }

        //정렬
        searchViewModel.checkSortRating = tempRating
        searchViewModel.checkSortRecent = tempRecent


    }

    private fun deepCopy(list: MutableList<String>): MutableList<String> {
        var tempList = mutableListOf<String>()
        tempList = list.toMutableList()
        return tempList
    }
}