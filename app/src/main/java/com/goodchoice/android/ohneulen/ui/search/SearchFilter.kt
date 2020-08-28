package com.goodchoice.android.ohneulen.ui.search

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.Typeface
import android.os.Bundle
import android.view.*
import android.view.animation.TranslateAnimation
import android.widget.GridLayout
import android.widget.TextView
import android.widget.Toast
import android.widget.ToggleButton
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.data.model.OhneulenData
import com.goodchoice.android.ohneulen.databinding.SearchFilterBinding
import com.goodchoice.android.ohneulen.ui.MainActivity
import com.goodchoice.android.ohneulen.util.dp
import com.goodchoice.android.ohneulen.util.replaceAppbarFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber


class SearchFilter : Fragment() {
    companion object {
        fun newInstance() = SearchFilter()
    }

    private val searchViewModel: SearchViewModel by viewModel()
    private lateinit var binding: SearchFilterBinding
    var checkFood = true
    var checkClick = false

    var position = 0
    private var previousPosition = 0


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

        //이전 체크해둔 목록 가져오기
        searchViewModel.tempCate=searchViewModel.cate
        searchViewModel.tempOption=searchViewModel.option
        searchViewModel.tempOpenTime=searchViewModel.openTime
        searchViewModel.tempSort=searchViewModel.sort



        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //정렬
        //최근순
        if (searchViewModel.checkSortRecent) {
            binding.searchFilterRecent.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.white
                )
            )
            binding.searchFilterRecent.background = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.background_rounding_ohneulen
            )
        } else {
            binding.searchFilterRecent.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.background_rounding)
            binding.searchFilterRecent.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorBlack
                )
            )
        }
        //평점순
        if (searchViewModel.checkSortRating) {
            binding.searchFilterRating.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.white
                )
            )
            binding.searchFilterRating.background = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.background_rounding_ohneulen
            )
        } else {
            binding.searchFilterRating.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.background_rounding)
            binding.searchFilterRating.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorBlack
                )
            )
        }

        //메인카테고리 클릭시 서브 카테고리 변경
        searchViewModel.mainCategoryPosition.observe(viewLifecycleOwner, Observer {
            searchViewModel.subCategoryPosition = 0
            searchViewModel.subCategory.postValue(searchViewModel.subCategoryList[it])
        })

        //음식 메인카테고리 리스트 생성
        foodFilterGenerate()


        //뒤에 레이아웃 터치 안먹게 하기
        binding.searchFilter.setOnTouchListener { v, event -> true }

        //음식 선택시 아래 뷰 생성
        bottomViewGenerate()

        //convenience
        convenienceGenerate()

        //timeDay
        timeDayGenerate()


    }

    override fun onResume() {
        super.onResume()
        MainActivity.bottomNav.visibility = View.GONE

    }


    override fun onDestroy() {
        super.onDestroy()
        MainActivity.bottomNav.visibility = View.VISIBLE
        searchViewModel.mainCategoryPosition.postValue(0)
    }


    //아래 체크뷰 생성
    private fun bottomViewGenerate() {
        if (searchViewModel.tempCateOhneulenData.size > 0) {
            binding.searchFilterTv1.visibility = View.VISIBLE
            binding.searchFilterGridLayout.visibility = View.VISIBLE
        } else if (searchViewModel.tempCateOhneulenData.size == 0) {
            binding.searchFilterTv1.visibility = View.GONE
            binding.searchFilterGridLayout.visibility = View.GONE
            binding.searchFilterGridLayout.removeAllViews()
        }

//        처음시작시 체크해둔 뷰 생성
        for (i in searchViewModel.tempCateOhneulenData) {
            val ohneulenData = i
            val layoutInflater = this.layoutInflater
            val param = GridLayout.LayoutParams()
//                param.setMargins(5.dp(),5.dp(),5.dp(),5.dp())
            //searchFilterGridLayout.width 길이를 가져올수 없어서 전체 길이를 가져옴
            val display = requireActivity().windowManager.defaultDisplay
            val size = Point()
            display.getSize(size)
            val width = size.x
            param.width =
                (width - binding.searchFilterGridLayout.paddingStart - binding.searchFilterGridLayout.paddingEnd) / 3
            val selectView = layoutInflater.inflate(R.layout.filter_selector, null)
//                selectView.background =
//                    requireContext().getDrawable(R.drawable.background_border_white)
            selectView.findViewById<TextView>(R.id.filter_select_title).text =
                ohneulenData.minorName.replace(" ", "")
            selectView.layoutParams = param
            //클릭할시 뷰 삭제
            selectView.setOnClickListener {
                //바텀뷰 삭제
                for (i in 0 until binding.searchFilterGridLayout.childCount) {
                    if (binding.searchFilterGridLayout[i].findViewById<TextView>(R.id.filter_select_title).text == ohneulenData.minorName.replace(
                            " ",
                            ""
                        )
                    ) {
                        loop@ for (j in searchViewModel.subCategoryList.indices) {
                            for (k in searchViewModel.subCategoryList[j].indices) {
                                if (searchViewModel.subCategoryList[j][k].minorName == ohneulenData.minorName) {
                                    searchViewModel.subCategoryList[j][k].check = false
                                    searchViewModel.subCategory.postValue(searchViewModel.subCategoryList[searchViewModel.mainCategoryPosition.value!!])
                                    break@loop
                                }
                            }
                        }
                        binding.searchFilterGridLayout.removeViewAt(i)
                        break
                    }
                }
                //tempCate child 삭제
                searchViewModel.tempCateOhneulenData.remove(ohneulenData)
            }

//            binding.searchFilterGridLayout.addView(selectView, 0)
            binding.searchFilterGridLayout.addView(selectView)
        }


        //체크 검사해서 바뀔때마다 확인
        searchViewModel.subCategory.observe(viewLifecycleOwner, Observer {
            if (searchViewModel.tempCateOhneulenData.size > 0) {
                binding.searchFilterTv1.visibility = View.VISIBLE
                binding.searchFilterGridLayout.visibility = View.VISIBLE
            } else if (searchViewModel.tempCateOhneulenData.size == 0) {
                binding.searchFilterTv1.visibility = View.GONE
                binding.searchFilterGridLayout.visibility = View.GONE
                binding.searchFilterGridLayout.removeAllViews()
            }

            if (!searchViewModel.subCategoryList[searchViewModel.mainCategoryPosition.value!!].isNullOrEmpty()) {
                val ohneulenData =
                    searchViewModel.subCategoryList[searchViewModel.mainCategoryPosition.value!!][searchViewModel.subCategoryPosition]
                val layoutInflater = this.layoutInflater
                val param = GridLayout.LayoutParams()
//                param.setMargins(5.dp(),5.dp(),5.dp(),5.dp())
                //searchFilterGridLayout.width 길이를 가져올수 없어서 전체 길이를 가져옴
                val display = requireActivity().windowManager.defaultDisplay
                val size = Point()
                display.getSize(size)
                val width = size.x
                param.width =
                    (width - binding.searchFilterGridLayout.paddingStart - binding.searchFilterGridLayout.paddingEnd) / 3
                val selectView = layoutInflater.inflate(R.layout.filter_selector, null)
//                selectView.background =
//                    requireContext().getDrawable(R.drawable.background_border_white)
                selectView.findViewById<TextView>(R.id.filter_select_title).text =
                    ohneulenData.minorName.replace(" ", "")
                selectView.layoutParams = param
                //클릭할시 뷰 삭제
                selectView.setOnClickListener {
                    checkClick = true
                    //바텀뷰 삭제
                    for (i in 0 until binding.searchFilterGridLayout.childCount) {
                        if (binding.searchFilterGridLayout[i].findViewById<TextView>(R.id.filter_select_title).text == ohneulenData.minorName.replace(
                                " ",
                                ""
                            )
                        ) {
                            loop@ for (j in searchViewModel.subCategoryList.indices) {
                                for (k in searchViewModel.subCategoryList[j].indices) {
                                    if (searchViewModel.subCategoryList[j][k].minorName == ohneulenData.minorName) {
                                        searchViewModel.subCategoryList[j][k].check = false
                                        searchViewModel.subCategory.postValue(searchViewModel.subCategoryList[searchViewModel.mainCategoryPosition.value!!])
//                                        searchViewModel.subCategory.value=searchViewModel.subCategoryList[searchViewModel.mainCategoryPosition.value!!]
                                        checkClick = true
                                        break@loop
                                    }
                                }
                            }
                            binding.searchFilterGridLayout.removeViewAt(i)
                            break
                        }
                    }

                    //tempCate child 삭제
                    searchViewModel.tempCateOhneulenData.remove(ohneulenData)
                }
                if (checkClick) {
                    checkClick = false
                    return@Observer
                }

                //위에 체크된 뷰 클릭
                if (searchViewModel.subCategoryList[searchViewModel.mainCategoryPosition.value!!][searchViewModel.subCategoryPosition].check) {
//                    selectView.setPadding(5.dp(), 5.dp(), 5.dp(), 5.dp())
                    var check = false
                    //중복체크해서 없으면 추가
                    for (i in binding.searchFilterGridLayout.children) {
                        if (i.findViewById<TextView>(R.id.filter_select_title).text
                            == selectView.findViewById<TextView>(R.id.filter_select_title).text
                        )
                            check = true
                    }
                    if (!check)
                        binding.searchFilterGridLayout.addView(selectView)
                } else {
                    for (i in 0 until binding.searchFilterGridLayout.childCount) {
                        if (binding.searchFilterGridLayout[i].findViewById<TextView>(R.id.filter_select_title).text == ohneulenData.minorName.replace(
                                " ",
                                ""
                            )
                        ) {
                            binding.searchFilterGridLayout.removeViewAt(i)
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
                childView.findViewById<TextView>(R.id.filter_category)
                    .setTypeface(null, Typeface.BOLD)
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
                previousView.findViewById<TextView>(R.id.filter_category)
                    .setTypeface(null, Typeface.NORMAL)
                childView.findViewById<TextView>(R.id.filter_category).setTextColor(
                    ContextCompat.getColor(requireContext(), R.color.white)
                )
                childView.setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.colorOhneulen
                    )
                )
                childView.findViewById<TextView>(R.id.filter_category)
                    .setTypeface(null, Typeface.BOLD)
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
            tb.isChecked = mutableList[i].check
            tb.stateListAnimator = null
//            tb.textSize= 14f
            //체크표시돼있을때
            if (mutableList[i].check) {
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
                    //옵션일때
                    if (gridLayout == binding.searchFilterConvenience) {
                        searchViewModel.tempOption.add(mutableList[i].minorCode)
                        searchViewModel.mainOptionKind[i].check = isChecked
                    }
                    //휴무일 일때
                    else if (gridLayout == binding.searchFilterTimeDay) {
                        searchViewModel.tempOpenTime.add(mutableList[i].minorCode)
                        searchViewModel.timeDay[i].check = isChecked
                    }

                } else {
                    tb.background =
                        ContextCompat.getDrawable(requireContext(), R.drawable.background_rounding)
                    tb.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorBlack))
                    if (gridLayout == binding.searchFilterConvenience) {
                        searchViewModel.tempOption.remove(mutableList[i].minorCode)
                        searchViewModel.mainOptionKind[i].check = isChecked
                    }
                    //휴무일 일때
                    else if (gridLayout == binding.searchFilterTimeDay) {
                        searchViewModel.tempOpenTime.remove(mutableList[i].minorCode)
                        searchViewModel.timeDay[i].check = isChecked
                    }

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

    private fun slideLeft() {
        val animate = TranslateAnimation(binding.searchFilterChoice.width.toFloat(), 0f, 0f, 0f)
        binding.searchFilterChoiceBackground.startAnimation(animate)
    }

    private fun slideRight() {
        Timber.e(binding.searchFilterFood.width.toFloat().toString())
        val animate = TranslateAnimation(0f, binding.searchFilterFood.width.toFloat(), 0f, 0f)
        binding.searchFilterChoiceBackground.startAnimation(animate)
    }

    private fun slide() {
        val animate = if (checkFood) {
            TranslateAnimation(binding.searchFilterFood.width.toFloat(), 0f, 0f, 0f)
        } else {
            TranslateAnimation(0f, binding.searchFilterFood.width.toFloat(), 0f, 0f)
        }
        animate.duration = 200
        animate.fillAfter = true
        binding.searchFilterChoiceBackground.startAnimation(animate)
    }


    fun filterClick(view: View) {
        if (view == binding.searchFilterFood && checkFood)
            return
        else if (view == binding.searchFilterOptions && !checkFood)
            return
        if (view == binding.searchFilterFood) {
            binding.searchFilterFoodCon.visibility = View.VISIBLE
            binding.searchFilterOptionsView.visibility = View.GONE
            binding.searchFilterResetBorder.visibility = View.GONE
            checkFood = true
        } else {
            binding.searchFilterFoodCon.visibility = View.GONE
            binding.searchFilterOptionsView.visibility = View.VISIBLE
            binding.searchFilterResetBorder.visibility = View.VISIBLE
            checkFood = false
        }
        slide()
    }


    fun sortButtonClick(view: View) {
        if (view == binding.searchFilterRecent) {
            //최근순 클릭
            searchViewModel.tempSort.add("date")
            searchViewModel.tempSort.remove("point")
            searchViewModel.checkSortRecent = true
            searchViewModel.checkSortRating = false
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
            //평점순 클릭
            searchViewModel.tempSort.add("point")
            searchViewModel.tempSort.remove("date")
            searchViewModel.checkSortRecent = false
            searchViewModel.checkSortRating = true
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
            if (searchViewModel.tempCateOhneulenData.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "음식의 종류를 선택해 주세요", Toast.LENGTH_SHORT).show()
                return
            }
//            }
            for (i in searchViewModel.subCategoryList.indices) {
                for (j in searchViewModel.subCategoryList[i].indices) {
                    searchViewModel.subCategoryList[i][j].check = false
                }
            }
            searchViewModel.tempCateOhneulenData.clear()
            searchViewModel.cate.clear()
            searchViewModel.tempCate.clear()
            searchViewModel.subCategory.postValue(searchViewModel.subCategoryList[searchViewModel.mainCategoryPosition.value!!])
        }

        //옵션선택일때
        else {
            if (!searchViewModel.checkSortRating && !searchViewModel.checkSortRecent && searchViewModel.tempOption.isEmpty()
                && searchViewModel.tempOpenTime.isEmpty() && searchViewModel.tempSort.isEmpty()
            ) {
                Toast.makeText(requireContext(), "옵션을 선택해 주세요", Toast.LENGTH_SHORT).show()
                return
            }

            binding.searchFilterRating.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.background_rounding)
            binding.searchFilterRating.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorBlack
                )
            )
            binding.searchFilterRecent.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.background_rounding)
            binding.searchFilterRecent.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorBlack
                )
            )
            searchViewModel.tempOption.clear()
            searchViewModel.tempOpenTime.clear()
            searchViewModel.tempSort.clear()
            //옵션
            for (i in 0 until binding.searchFilterConvenience.childCount) {
                (binding.searchFilterConvenience.getChildAt(i) as ToggleButton).isChecked = false
            }
            //요일
            for (i in 0 until binding.searchFilterTimeDay.childCount) {
                (binding.searchFilterTimeDay.getChildAt(i) as ToggleButton).isChecked = false
            }
        }

    }

    fun submitClick(view: View) {
        Toast.makeText(requireContext(), "적용되었습니다", Toast.LENGTH_SHORT).show()
        Timber.e(searchViewModel.openTime.toString())
        searchViewModel.filterSubmit()
        replaceAppbarFragment(SearchAppBar.newInstance())
        MainActivity.supportFragmentManager.popBackStack()
    }


}