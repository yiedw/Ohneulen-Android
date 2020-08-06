package com.goodchoice.android.ohneulen.ui.store.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.data.model.StoreDetail
import com.goodchoice.android.ohneulen.databinding.StoreHomeBinding
import com.goodchoice.android.ohneulen.ui.store.StoreViewModel
import com.goodchoice.android.ohneulen.util.addMainFragment
import com.goodchoice.android.ohneulen.util.replaceAppbarFragment
import com.goodchoice.android.ohneulen.util.textColor
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
        val openDate = store.openDate.substring(0, 4) + "-" + store.openDate.substring(
            4,
            6
        ) + "-" + store.openDate.substring(6)
        val modifyDate = store.modifyDate.substring(0, 10)
        binding.storeHomeOpenTv2.text = "$openDate\n$modifyDate"
    }

    fun reportClick(view: View) {
//        MainActivity.mainFrameLayout.layoutParams = MainActivity.initMainFrameLayout
        replaceAppbarFragment(StoreHomeReportAppBar.newInstance())
        addMainFragment(
            StoreHomeReport.newInstance(storeViewModel.storeDetail.value!!.storeInfo.store.storeName),
            true
        )
    }

}