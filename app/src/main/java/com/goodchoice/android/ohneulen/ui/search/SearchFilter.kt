package com.goodchoice.android.ohneulen.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.TextView
import android.widget.ToggleButton
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.SearchFilterBinding
import com.goodchoice.android.ohneulen.ui.MainActivity
import com.goodchoice.android.ohneulen.util.dp
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import kotlin.collections.HashMap


class SearchFilter : Fragment() {
    companion object {
        fun newInstance() = SearchFilter()
    }

    private val searchViewModel: SearchViewModel by viewModel()
    private lateinit var binding: SearchFilterBinding
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
            R.layout.search_filter,
            container,
            false
        )
        binding.apply {
            lifecycleOwner = this@SearchFilter
            fragment = this@SearchFilter
            viewModel = searchViewModel
        }


        filterViewHashMap = searchViewModel.filterViewHashMap
        filterMainPositionHashMap = searchViewModel.filterMainPositionHashMap
        filterSubPositionHashMap = searchViewModel.filterSubPositionHashMap

        val reverse = filterViewHashMap.keys.reversed()
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

        MainActivity.bottomNav.visibility = View.GONE
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
                    if (filterViewHashMap.isNullOrEmpty()) {
                        binding.searchFilterTv1.visibility = View.GONE
                    } else {
                        binding.searchFilterTv1.visibility = View.VISIBLE
                    }
                    return@Observer
                }
                val index = searchViewModel.subCategoryPosition
                val filterCode = searchViewModel.subCategory.value!![index].minorCode
                val filterName = searchViewModel.subCategory.value!![index].minorName
                makeSelectView(filterCode, filterName, index)

            })

        //초기화 클릭
//        binding.searchFilterReset.setOnClickListener {
//            val categoryList = searchViewModel.categoryList.value!!
//            Timber.e(filterViewHashMap.keys.size.toString())
//            for (i in filterViewHashMap.keys) {
//                categoryList[filterMainPositionHashMap[i]!!].subCategoryList[filterSubPositionHashMap[i]!!].check =
//                    false
//            }
//            searchViewModel.categoryList.postValue(categoryList)
//            filterViewHashMap.clear()
//            filterMainPositionHashMap.clear()
//            filterSubPositionHashMap.clear()
//            binding.searchFilterSelect.removeAllViews()
//        }

        //선택완료 클릭
        binding.searchFilterSubmit.setOnClickListener {
            Timber.e(filterViewHashMap.keys.toString())
        }

        //뒤에 레이아웃 터치 안먹게 하기
        binding.searchFilter.setOnTouchListener { v, event -> true }


        //클릭시 배경바꾸기


        //options 편의바 생성
        //sample
        val sampleConvenienceList = listOf<String>(
            "영업중", "주차 가능", "예약 가능", "배달 가능", "포장 가능", "반려동물",
            "비건 식당", "놀이방", "와이파이"
        )
        for (i in sampleConvenienceList.iterator()) {
            binding.searchFilterConvenience.addView(toggleButtonGenerate(i))
        }

        //영업일 생성
        //sample
        val sampleOpenList = listOf<String>(
            "연중무휴", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일", "일요일"
        )
        for (i in sampleOpenList.iterator()) {
            binding.searchFilterOpen.addView(toggleButtonGenerate(i))
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        searchViewModel.filterViewHashMap = filterViewHashMap
        searchViewModel.filterMainPositionHashMap = filterMainPositionHashMap
        searchViewModel.filterSubPositionHashMap = filterSubPositionHashMap
        binding.searchFilterSelect.removeAllViewsInLayout()
        MainActivity.bottomNav.visibility = View.VISIBLE
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
        //2개가 필요
        if (filterViewHashMap.isNullOrEmpty()) {
            binding.searchFilterTv1.visibility = View.GONE
        } else {
            binding.searchFilterTv1.visibility = View.VISIBLE
        }


    }

    fun onFoodClick(view: View) {
        binding.searchFilterFoodCon.visibility = View.VISIBLE
        binding.searchFilterOptionsCon.visibility = View.GONE
        binding.searchFilterFood.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.colorOhneulen
            )
        )
        binding.searchFilterFood.background = ContextCompat.getDrawable(
            requireContext(),
            R.drawable.background_rounding_filter_select
        )
        binding.searchFilterOptions.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.colorCGrey
            )
        )
        binding.searchFilterOptions.background = null
    }

    fun onOptionsClick(view: View) {
        binding.searchFilterFoodCon.visibility = View.GONE
        binding.searchFilterOptionsCon.visibility = View.VISIBLE
        binding.searchFilterOptions.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.colorOhneulen
            )
        )
        binding.searchFilterOptions.background = ContextCompat.getDrawable(
            requireContext(),
            R.drawable.background_rounding_filter_select
        )
        binding.searchFilterFood.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.colorCGrey
            )
        )
        binding.searchFilterFood.background = null
    }

    private fun toggleButtonGenerate(text: String): ToggleButton {

        // convenience list를 가져와서 갯수만큼 뷰를 추가

        val param = GridLayout.LayoutParams()
        param.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
        param.height = 32.dp()


        val tb = ToggleButton(requireContext())
        tb.layoutParams = param
//        tb.gravity = Gravity.CENTER
//        tb.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
        tb.background = ContextCompat.getDrawable(requireContext(), R.drawable.background_rounding)
        tb.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorBlack))
        tb.text = text
        tb.textOff = text
        tb.textOn = text
        tb.setPadding(0, 0, 0, 0)
        tb.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                tb.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                tb.background = ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.background_rounding_ohneulen
                )
            } else {
                tb.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.background_rounding)
                tb.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorBlack))
            }

        }

        return tb
//        binding.searchFilterConvenience.addView()
    }

    fun sortButtonClick(view: View) {
        if (view == binding.searchFilterRecent) {
            binding.apply {
                searchFilterRecent.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.white
                    )
                )
                searchFilterRecent.background = ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.background_rounding_ohneulen
                )

                searchFilterRating.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.background_rounding)
                searchFilterRating.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.colorBlack
                    )
                )
            }
        } else {
            binding.apply {
                searchFilterRating.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.white
                    )
                )
                searchFilterRating.background = ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.background_rounding_ohneulen
                )

                searchFilterRecent.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.background_rounding)
                searchFilterRecent.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.colorBlack
                    )
                )
            }

        }
    }


}