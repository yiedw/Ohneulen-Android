package com.goodchoice.android.ohneulen.ui.search

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.TextView
import android.widget.Toast
import android.widget.ToggleButton
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.data.model.OhneulenData
import com.goodchoice.android.ohneulen.data.repository.InitData
import com.goodchoice.android.ohneulen.databinding.SearchFilterBinding
import com.goodchoice.android.ohneulen.ui.MainActivity
import com.goodchoice.android.ohneulen.util.dp
import kotlinx.android.synthetic.main.search_filter.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber


class SearchFilter : Fragment() {
    companion object {
        fun newInstance() = SearchFilter()
    }

    private val searchViewModel: SearchViewModel by viewModel()
    private lateinit var binding: SearchFilterBinding
    var checkFood = true

    var position = 0
    private var previousPosition = 0

    private var checkRecent = false
    private var checkRating = false


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

        MainActivity.bottomNav.visibility = View.GONE
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //메인카테고리 클릭시 서브 카테고리 변경
        searchViewModel.mainCategoryPosition.observe(viewLifecycleOwner, Observer {
            searchViewModel.subCategoryPosition = 0
            searchViewModel.subCategory.postValue(searchViewModel.subCategoryList[it])
        })

        //음식 선택시 아래 뷰 생성
        foodFilterGenerate()
        //음식 메인카테고리 리스트 생성
        bottomViewGenerate()


        //뒤에 레이아웃 터치 안먹게 하기
        binding.searchFilter.setOnTouchListener { v, event -> true }

        //convenience
        convenienceGenerate()

        //timeDay
        timeDayGenerate()

    }

    override fun onDestroy() {
        super.onDestroy()
        MainActivity.bottomNav.visibility = View.VISIBLE
    }


    //아래 체크뷰 생성
    private fun bottomViewGenerate() {
        searchViewModel.subCategory.observe(viewLifecycleOwner, Observer {
            if (searchViewModel.filterHashMap.size == 1) {
                binding.searchFilterTv1.visibility = View.VISIBLE
            } else if (searchViewModel.filterHashMap.isNullOrEmpty()) {
                binding.searchFilterTv1.visibility = View.GONE
                binding.searchFilterSelect.removeAllViews()
            }

//                Timber.e("${searchViewModel.mainCategoryPosition.value!!} ${searchViewModel.subCategoryPosition}")
            if (!searchViewModel.filterHashMap.isNullOrEmpty() && !searchViewModel.subCategoryList[searchViewModel.mainCategoryPosition.value!!].isNullOrEmpty()) {
                val filterName =
                    searchViewModel.subCategoryList[searchViewModel.mainCategoryPosition.value!!][searchViewModel.subCategoryPosition].minorName
                val layoutInflater = this.layoutInflater
                val selectView = layoutInflater.inflate(R.layout.filter_selector, null)
                selectView.findViewById<TextView>(R.id.filter_select_title).text =
                    filterName

                //클릭할시 뷰 삭제
                var removeIndex = 0
                selectView.setOnClickListener {
                    //바텀뷰 삭제
                    for (i in 0 until binding.searchFilterSelect.childCount) {
                        if (binding.searchFilterSelect[i].findViewById<TextView>(R.id.filter_select_title).text == filterName) {
                            removeIndex = i
                            break
                        }
                    }
                    binding.searchFilterSelect.removeViewAt(removeIndex)

                    //해시맵삭제
                    for (i in searchViewModel.filterHashMap.keys) {
                        val mainFilterPosition = i / 10
                        val subFilterPosition = i % 10
                        if (searchViewModel.subCategoryList[mainFilterPosition][subFilterPosition].minorName == filterName) {
                            searchViewModel.filterHashMap.remove(i)
                            searchViewModel.subCategoryList[mainFilterPosition][subFilterPosition].check =
                                false
                            searchViewModel.subCategory.postValue(searchViewModel.subCategoryList[searchViewModel.mainCategoryPosition.value!!])
                            break
                        }
                    }


                }

                //체크된거 클릭
                if (searchViewModel.subCategoryList[searchViewModel.mainCategoryPosition.value!!][searchViewModel.subCategoryPosition].check) {
                    binding.searchFilterSelect.addView(selectView, 0)
                } else {
                    //체크된거 삭제
                    for (i in 0 until binding.searchFilterSelect.childCount) {
                        if (binding.searchFilterSelect[i].findViewById<TextView>(R.id.filter_select_title).text == filterName) {
                            removeIndex = i
                            binding.searchFilterSelect.removeViewAt(removeIndex)
                            break
                        }
                    }
                }
            }
        })


    }

    private fun foodFilterGenerate() {
        val mainCategory = searchViewModel.mainCategory
        for (i in mainCategory.indices) {
            val inflater =
                requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            inflater.inflate(R.layout.search_filter_item, binding.searchFilterMain, true)
            val childView = binding.searchFilterMain.getChildAt(i)
            childView.findViewById<TextView>(R.id.filter_category).text = mainCategory[i].minorName

            if (i == 0) {
                childView.findViewById<TextView>(R.id.filter_category).setTextColor(
                    ContextCompat.getColor(requireContext(), R.color.white)
                )
                childView.setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.colorOhneulen
                    )
                )
            }
            //클릭했을때
            childView.setOnClickListener {
                previousPosition = position
                position = i
                val previousView = binding.searchFilterMain.getChildAt(previousPosition)
                previousView.findViewById<TextView>(R.id.filter_category).setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.colorGrey88
                    )
                )
                previousView.setBackgroundColor(
                    Color.parseColor("#f6f6f6")
                )
                childView.findViewById<TextView>(R.id.filter_category).setTextColor(
                    ContextCompat.getColor(requireContext(), R.color.white)
                )
                childView.setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.colorOhneulen
                    )
                )
                searchViewModel.mainCategoryPosition.postValue(position)

            }

        }
    }

    private fun toggleButtonGenerate(
        gridLayout: GridLayout,
        mutableList: MutableList<OhneulenData>
    ) {

        // list를 가져와서 갯수만큼 뷰를 추가
        for (i in mutableList.indices) {
            val param = GridLayout.LayoutParams()
            param.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
            param.height = 32.dp()
            val tb = ToggleButton(requireContext())
            tb.layoutParams = param
            tb.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.background_rounding)
            tb.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorBlack))
            tb.text = mutableList[i].minorName
            tb.textOff = mutableList[i].minorName
            tb.textOn = mutableList[i].minorName
            tb.setPadding(0, 0, 0, 0)
            tb.setOnCheckedChangeListener { buttonView, isChecked ->
                mutableList[i].check = isChecked
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
            gridLayout.addView(tb)
        }


//        binding.searchFilterConvenience.addView()
    }

    private fun convenienceGenerate() {
        val convenience = searchViewModel.mainOptionKind
        toggleButtonGenerate(binding.searchFilterConvenience, convenience)
    }

    private fun timeDayGenerate() {
        val timeDay = searchViewModel.timeDay
        toggleButtonGenerate(binding.searchFilterTimeDay, timeDay)
    }

    fun onFoodClick(view: View) {
        binding.searchFilterFoodCon.visibility = View.VISIBLE
        binding.searchFilterOptionsView.visibility = View.GONE
        binding.searchFilterResetBorder.visibility = View.GONE
        binding.searchFilterReset.setBackgroundColor(Color.parseColor("#f6f6f6"))
        checkFood=true
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
        binding.searchFilterOptionsView.visibility = View.VISIBLE
        binding.searchFilterResetBorder.visibility = View.VISIBLE
        binding.searchFilterReset.setBackgroundColor(requireContext().getColor(R.color.white))
        checkFood=true
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

    fun sortButtonClick(view: View) {
        if (view == binding.searchFilterRecent) {
            checkRecent = true
            checkRating = false
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
            checkRecent = false
            checkRating = true
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


    fun resetClick(view: View) {
        //음식선택일때
        if (checkFood) {
            if (searchViewModel.filterHashMap.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "음식을 종류를 선택해 주세요", Toast.LENGTH_SHORT).show()
                return
            }
            for (i in searchViewModel.filterHashMap.keys) {
                val mainFilterPosition = i / 10
                val subFilterPosition = i % 10
                searchViewModel.subCategoryList[mainFilterPosition][subFilterPosition].check = false
            }
            searchViewModel.filterHashMap.clear()
            searchViewModel.subCategory.postValue(searchViewModel.subCategoryList[searchViewModel.mainCategoryPosition.value!!])
        }

        //옵션선택일때
        else {
//            if(!checkRating )
        }

    }

    fun submitClick(view: View) {
        //음식 선택일때
        if (checkFood) {
            if (searchViewModel.filterHashMap.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "음식을 종류를 선택해 주세요", Toast.LENGTH_SHORT).show()
                return
            }
        }
        else{

        }
    }


}