package com.goodchoice.android.ohneulen.ui.store.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.marginStart
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.data.model.StoreDetail
import com.goodchoice.android.ohneulen.databinding.StoreHomeBinding
import com.goodchoice.android.ohneulen.ui.MainActivity
import com.goodchoice.android.ohneulen.ui.store.StoreViewModel
import com.goodchoice.android.ohneulen.util.*
import com.goodchoice.android.ohneulen.util.constant.BaseUrl
import com.goodchoice.android.ohneulen.util.constant.ConstList
import org.koin.androidx.viewmodel.ext.android.viewModel

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        storeViewModel.storeDetail.observe(viewLifecycleOwner, Observer {
            //영업일 뷰 생성
            openDayGenerate(it)
            //휴무일 뷰 생성
            closeDayGenerate(it)
            //개업일 업데이트
            storeUpdate(it)
            //옵션 뷰 생성
            optionGenerate(it)
            //키워드 뷰 생성
            keywordsGenerate(it)
        })

    }

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
        val store = storeDetail.storeInfo.store
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
        binding.storeHomeOpenTv2.text = "$openDate\n$modifyDate"
    }

    private fun optionGenerate(storeDetail: StoreDetail) {
        binding.storeHomeOptions.removeAllViews()
        for (i in storeDetail.optionList) {
            //옵션뷰 생성
            //옵션 리니어레이아웃 생성
            val linearLayout = LinearLayout(requireContext())
            linearLayout.orientation = LinearLayout.HORIZONTAL
            val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 20.dp())
            linearLayout.layoutParams = params

            //옵션이름
            val tv1 = TextView(requireContext())
            val params1 = LinearLayout.LayoutParams(
                50.dp(),
                LinearLayout.LayoutParams.MATCH_PARENT
            )
            tv1.layoutParams = params1
            tv1.text = i.option_name
            tv1.setTextColor(requireContext().getColor(R.color.colorBlack))

            //옵션 여부
            val tv2 = TextView(requireContext())
            val params2 = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
            tv2.layoutParams = params2
            tv2.text = i.option_kind_name
            tv2.setTextColor(requireContext().getColor(R.color.colorGrey88))


            linearLayout.addView(tv1)
            linearLayout.addView(tv2)
            binding.storeHomeOptions.addView(linearLayout)
        }

    }

    //키워드 뷰 생성
    private fun keywordsGenerate(storeDetail: StoreDetail) {
        binding.storeHomeKeywords.removeAllViews()
        for (i in storeDetail.keywordList) {
            val linearLayout = LinearLayout(requireContext())
            linearLayout.orientation = LinearLayout.HORIZONTAL
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            linearLayout.layoutParams = params

            //이모티콘
            val iv = ImageView(requireContext())
            val params1 = LinearLayout.LayoutParams(26.dp(), 26.dp())
//            iv.setPadding(0,0,15.dp(),0)
            iv.layoutParams = params1
            iv.scaleType=ImageView.ScaleType.FIT_CENTER
            Glide.with(requireContext()).load("${BaseUrl.Ohneulen}${i.icon}").into(iv)


            //설명
            val tv = TextView(requireContext())
            val params2 = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
            params2.marginStart=24.dp()
            tv.gravity = Gravity.CENTER
            tv.layoutParams = params2
            tv.setTextColor(requireContext().getColor(R.color.colorGrey88))
            tv.text = i.keyword

            linearLayout.addView(iv)
            linearLayout.addView(tv)
            binding.storeHomeKeywords.addView(linearLayout)
        }
    }


    fun reportClick(view: View) {
//        MainActivity.mainFrameLayout.layoutParams = MainActivity.initMainFrameLayout
        replaceAppbarFragment(StoreHomeReportAppBar.newInstance())
        popupFragment(StoreHomeReport.newInstance(storeViewModel.storeDetail.value!!.storeInfo.store.storeName))
//        addMainFragment(
//            StoreHomeReport.newInstance(storeViewModel.storeDetail.value!!.storeInfo.store.storeName),
//            true
//        )
    }

}