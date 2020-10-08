package com.goodchoice.android.ohneulen.ui.store.home

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.data.model.StoreDetail
import com.goodchoice.android.ohneulen.databinding.StoreHomeBinding
import com.goodchoice.android.ohneulen.ui.login.LoginViewModel
import com.goodchoice.android.ohneulen.ui.store.StoreAppBar
import com.goodchoice.android.ohneulen.ui.store.StoreFragment
import com.goodchoice.android.ohneulen.ui.store.StoreViewModel
import com.goodchoice.android.ohneulen.util.*
import com.goodchoice.android.ohneulen.util.constant.ConstList
import com.google.android.material.tabs.TabLayout
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.lang.reflect.Type

class StoreHome : Fragment() {
    companion object {
        fun newInstance() =
            StoreHome()
    }

    private lateinit var binding: StoreHomeBinding
    private val storeViewModel: StoreViewModel by viewModel()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.store_home,
            container,
            false
        )
        binding.fragment = this
        binding.viewModel = storeViewModel
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        storeViewModel.storeDetail.observe(viewLifecycleOwner, Observer {

            //주소 + 상세주소
            binding.storeHomeAddress.text =
                "${it.storeInfo.storeFull.addrRoad1} ${it.storeInfo.storeFull.addrRoad2}"

            //전화번호 없으면 숨기기
            if (it.storeInfo.storeFull.bizTel.isEmpty()) {
                binding.storeHomeBizTel.visibility = View.GONE
            } else {
                binding.storeHomeBizTel.visibility = View.VISIBLE
            }

            //영업일 뷰 생성
            openDayGenerate(it)
            //휴무일 뷰 생성 ( 옵션에서 같이해결)
//            closeDayGenerate(it)
            //개업일 업데이트
            storeUpdate(it)
            //옵션 뷰 생성
            optionGenerate(it)
            //키워드 뷰 생성
            keywordsGenerate(it)

            //설명 없으면 숨기기
            if (it.storeInfo.storeFull.contents.isEmpty()) {
                binding.storeHomeContents.visibility = View.GONE
            } else {
                binding.storeHomeContents.visibility = View.VISIBLE
            }

            //공간이 남으면 아래로 스크롤 못하게 세팅
            //탭바 높이 + 남는공간
//            binding.storeHome.post {
//                val parentFragment = (parentFragment as StoreFragment).view
//                //parentFragment toolbar minheight 를 직접 설정
//                val minHeight =
//                    parentFragment?.findViewById<TabLayout>(R.id.store_tab)!!.height + parentFragment.findViewById<ViewPager2>(
//                        R.id.store_fragment_viewPager2
//                    )!!.height - binding.storeHome.height
//
//                Timber.e(minHeight.toString())
//                parentFragment.findViewById<Toolbar>(R.id.store_toolbar)?.minimumHeight = minHeight
//                parentFragment.findViewById<Toolbar>(R.id.store_toolbar)?.requestLayout()
//                Timber.e(parentFragment.findViewById<Toolbar>(R.id.store_toolbar)?.minimumHeight.toString())
//            }
        })
    }

    override fun onStart() {
        super.onStart()

    }

    override fun onResume() {
        super.onResume()
    }


    @SuppressLint("SetTextI18n")
    private fun openDayGenerate(storeDetail: StoreDetail) {
        for (i in storeDetail.storeTime.open.indices) {
            val time = storeDetail.storeTime.open[i]
            val params = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            val tv = TextView(requireContext())
            tv.setTextColor(requireContext().getColor(R.color.colorGrey88))
            tv.text =
                "${time.day_name}  ${time.startTime}:${time.startMin} ~ ${time.endTime}:${time.endMin}"
            tv.layoutParams = params
            //글씨체 light로 변경
            tv.typeface = Typeface.create(
                ResourcesCompat.getFont(requireContext(), R.font.notosanskr_light),
                Typeface.NORMAL
            )
            tv.includeFontPadding = false
            binding.storeHomeOpenDay.addView(tv)
        }
        for (i in storeDetail.storeTime.close.indices) {
            if (storeDetail.storeTime.close[i].kind == "002") {
                val time = storeDetail.storeTime.close[i]
                val params = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                val tv = TextView(requireContext())
                tv.setTextColor(requireContext().getColor(R.color.colorGrey88))
                val text =
                    "${time.kind_name} ${time.day_name} ${time.startTime}:${time.startMin} ~ ${time.endTime}:${time.endMin}"
                tv.text = textColor(
                    text,
                    0,
                    time.kind_name.length,
                    requireContext().getColor(R.color.colorRed)
                )
                tv.layoutParams = params
                tv.typeface = Typeface.create(
                    ResourcesCompat.getFont(
                        requireContext(),
                        R.font.notosanskr_light
                    ), Typeface.NORMAL
                )
                tv.includeFontPadding = false
                binding.storeHomeOpenDay.addView(tv)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun closeDayGenerate(storeDetail: StoreDetail) {
        //브레이크 타임 제외
        for (i in storeDetail.storeTime.close.indices) {
            if (storeDetail.storeTime.close[i].kind != "002") {
                val time = storeDetail.storeTime.open[i]
                val params = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                val tv = TextView(requireContext())
                tv.setTextColor(requireContext().getColor(R.color.colorGrey88))
                tv.text =
                    "${time.day_name}  ${time.startTime}:${time.startMin} ~ ${time.endTime}:${time.endMin}"
                tv.layoutParams = params
                binding.storeHomeOpenDay.addView(tv)
            }
        }

    }

    //개업일 업데이트
    @SuppressLint("SetTextI18n")
    private fun storeUpdate(storeDetail: StoreDetail) {
        val store = storeDetail.storeInfo.storeFull
        //추후변경
//        val openDate = store.openDate.substring(0, 4) + "." + store.openDate.substring(
//            4,
//            6
//        ) + "." + store.openDate.substring(6)
//        val modifyDate = store.modifyDate.substring(0, 4) + "." + store.modifyDate.substring(
//            5,
//            7
//        ) + "." + store.modifyDate.substring(8, 10)
        val openDate = store.openDate
        val modifyDate = store.modifyDate.substring(0, 10)
        if (store.openDate.isEmpty()) {
            binding.storeHomeOpenTv1.text = "업데이트"
            binding.storeHomeOpenTv2.text = modifyDate
        } else {
            binding.storeHomeOpenTv1.text = "개업일\n업데이트"
            binding.storeHomeOpenTv2.text = "$openDate\n$modifyDate"
        }
    }

    @SuppressLint("SetTextI18n")
    private fun optionGenerate(storeDetail: StoreDetail) {
        binding.storeHomeOptions.removeAllViews()
        for (i in storeDetail.optionList) {
            //좌석수 0이면 생성x
            if (i.option_kind_name.isNullOrEmpty() && i.optionText.isNullOrEmpty()) {
                continue
            }

            //옵션뷰 생성
            //옵션 리니어레이아웃 생성
            val linearLayout = LinearLayout(requireContext())
            linearLayout.orientation = LinearLayout.HORIZONTAL
            val params =
                LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 20.dpToPx())
            params.setMargins(0, 5.dpToPx(), 0, 0)
            linearLayout.layoutParams = params

            //옵션이름
            val tv1 = TextView(requireContext())
            val params1 = LinearLayout.LayoutParams(
                50.dpToPx(),
                LinearLayout.LayoutParams.MATCH_PARENT
            )
            tv1.layoutParams = params1
            tv1.text = i.option_name
            tv1.setTextColor(requireContext().getColor(R.color.colorBlack))

            //옵션 여부
            val tv2 = TextView(requireContext())
            val params2 = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            tv2.layoutParams = params2
            tv2.typeface = Typeface.create(
                ResourcesCompat.getFont(requireContext(), R.font.notosanskr_light),
                Typeface.NORMAL
            )
            tv2.includeFontPadding = false
            if (i.kind == ConstList.OPTION_HOLIDAY) {
                tv2.text = i.optionText
            } else if (i.kind == ConstList.OPTION_SEAT_COUNT) {
                tv2.text = "${i.optionText} 석"
            } else {

                tv2.text = i.option_kind_name
            }
            tv2.setTextColor(requireContext().getColor(R.color.colorGrey88))


            if (i.kind == ConstList.OPTION_HOLIDAY) {
                tv2.text = i.optionText
//                binding.storeHomeCloseDay.removeAllViews()
                binding.storeHomeCloseDay.addView(tv2)
                continue
            }
            linearLayout.addView(tv1)
            linearLayout.addView(tv2)
            binding.storeHomeOptions.addView(linearLayout)
        }

    }

    //키워드 뷰 생성
    private fun keywordsGenerate(storeDetail: StoreDetail) {
        binding.storeHomeKeywords.removeAllViews()
        //키워드 뷰 없으면 숨기기
        if (storeDetail.keywordList.isEmpty()) {
            binding.storeHomeKeywords.visibility = View.GONE
            return
        }
        binding.storeHomeKeywords.visibility = View.VISIBLE
        for (i in storeDetail.keywordList) {
            val linearLayout = LinearLayout(requireContext())
            linearLayout.orientation = LinearLayout.HORIZONTAL
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(0, 0, 0, 5.dpToPx())
            linearLayout.layoutParams = params

            //아이콘
            val iv = ImageView(requireContext())
            val params1 = LinearLayout.LayoutParams(26.dpToPx(), 26.dpToPx())
//            iv.setPadding(0,0,15.dp(),0)
            iv.layoutParams = params1
//            iv.scaleType = ImageView.ScaleType.FIT_CENTER
            if (i.seq == "1") {
                Glide.with(requireContext())
                    .load(ContextCompat.getDrawable(requireContext(), R.drawable.store_home_wifi))
                    .into(iv)
            } else if (i.seq == "2") {
                Glide.with(requireContext())
                    .load(ContextCompat.getDrawable(requireContext(), R.drawable.store_home_animal))
                    .into(iv)
            } else if (i.seq == "3") {
                Glide.with(requireContext())
                    .load(ContextCompat.getDrawable(requireContext(), R.drawable.store_home_vegan))
                    .into(iv)
            }

            //설명
            val tv = TextView(requireContext())
            val params2 = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
            params2.marginStart = 24.dpToPx()
            tv.gravity = Gravity.CENTER
            tv.layoutParams = params2
            tv.setTextColor(requireContext().getColor(R.color.colorGrey88))
            tv.text = i.keyword
            tv.typeface = Typeface.create(
                ResourcesCompat.getFont(requireContext(), R.font.notosanskr_light),
                Typeface.NORMAL
            )
            tv.includeFontPadding = false

            linearLayout.addView(iv)
            linearLayout.addView(tv)
            binding.storeHomeKeywords.addView(linearLayout)
        }
    }


    fun reportClick(view: View) {
        if (!LoginViewModel.isLogin.value!!) {
            loginDialog(requireContext(), StoreAppBar.newInstance(), false)
            return
        }
        replaceAppbarFragment(StoreHomeReportAppBar.newInstance())
        popupFragment(StoreHomeReport.newInstance(storeViewModel.storeDetail.value!!.storeInfo.storeFull.storeName))
    }

}