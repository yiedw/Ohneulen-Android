package com.goodchoice.android.ohneulen.ui.store.review

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.StoreReviewBinding
import com.goodchoice.android.ohneulen.ui.store.StoreViewModel
import com.goodchoice.android.ohneulen.util.addAppbarFragment
import com.goodchoice.android.ohneulen.util.addMainFragment
import org.koin.android.ext.android.bind
import org.koin.androidx.viewmodel.ext.android.viewModel

class StoreReview : Fragment() {
    companion object {
        fun newInstance() =
            StoreReview()
    }

    private lateinit var binding: StoreReviewBinding
    private val storeViewModel: StoreViewModel by viewModel()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.store_review,
            container,
            false
        )
        binding.fragment = this
        binding.lifecycleOwner = this
        binding.viewModel = storeViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        chartSetting()
    }

    fun reviewWriteClick(view: View) {
        addAppbarFragment(StoreReviewWriteAppbar.newInstance(), true)
        addMainFragment(StoreReviewWrite.newInstance(), true)
    }

//    private fun chartSetting() {
//        val chart = binding.storeReviewChart
//
//        //데이터
//        chart.setDrawValueAboveBar(false)
//        chart.setTouchEnabled(false)
//        chart.legend.isEnabled=false
//        chart.description.isEnabled = false
//        chart.setDrawGridBackground(true)
//        chart.setFitBars(true)
//
//        val xAxis = chart.xAxis
////        xAxis.setDrawLabels(false)
//        xAxis.setDrawGridLines(false)
//        xAxis.setDrawAxisLine(false)
//        val chartList = listOf("분위기","청결도","친절도", "맛", "가격")
//        xAxis.valueFormatter = IndexAxisValueFormatter(chartList)
//        val ratingList = arrayListOf<Int>(70, 1, 100, 20, 45)
//        setData(ratingList)
//        chart.invalidate()
//    }
//
//    private fun setData(ratingList: ArrayList<Int>) {
//        val yVals = arrayListOf<BarEntry>()
//        for (i in 0 until ratingList.size) {
//            yVals.add(BarEntry(i.toFloat(), ratingList[i].toFloat()))
//        }
//        val set1 = BarDataSet(yVals, "")
//        set1.color = ContextCompat.getColor(requireContext(), R.color.colorOhneulen)
//        set1.setDrawValues(false)
//        val data = BarData(set1)
//        binding.storeReviewChart.data = data
//
//
//    }

//    private fun sampleChartSetting(){
//        val chart = binding.storeReviewRadarChart
//        val dataValue1 = ArrayList<RadarEntry>().apply {
//            this.add(RadarEntry(2f))
//            this.add(RadarEntry(2f))
//            this.add(RadarEntry(2f))
//            this.add(RadarEntry(2f))
//            this.add(RadarEntry(2f))
//        }
//
//        val dataValue2 = ArrayList<RadarEntry>().apply {
//            this.add(RadarEntry(4.toFloat()))
//            this.add(RadarEntry(4f))
//            this.add(RadarEntry(3f))
//            this.add(RadarEntry(4f))
//            this.add(RadarEntry(5f))
//        }
//        val dataSet1 = RadarDataSet(dataValue1, "업체 평균")
//        val dataSet2 = RadarDataSet(dataValue2, "이 업체")
//        dataSet1.color = Color.GRAY
//        dataSet1.setDrawValues(false)
//        dataSet2.color = ContextCompat.getColor(requireActivity(), R.color.colorSquash60)
//        dataSet2.setDrawFilled(true)
//        dataSet2.fillColor = ContextCompat.getColor(requireActivity(), R.color.colorSquash60)
//        dataSet2.fillAlpha = 170
//        dataSet2.setDrawValues(false)
//
//        val data = RadarData()
//        data.setDrawValues(false)
//        data.addDataSet(dataSet2)
//        data.addDataSet(dataSet1)
//
//        val legend=chart.legend
//        legend.isEnabled=false
//
//        val chartList = listOf<String>("맛", "직원 친절도", "분위기", "가격", "청결도")
//        val xAxis = chart.xAxis
//        xAxis.valueFormatter = object : IndexAxisValueFormatter(chartList) {
//        }
//        val yAxis = chart.yAxis
//        yAxis.axisMinimum=0.toFloat()
//        yAxis.axisMaximum=5f
//        yAxis.setLabelCount(0,true)
//        //라벨제거
//        yAxis.setDrawLabels(false)
//        yAxis.setDrawGridLines(false)
//
//        //설명 캔슬
//        chart.description.isEnabled=false
//        //고정
//        chart.setTouchEnabled(false)
//        //데이터 세팅
//        chart.data = data
//        chart.invalidate()
//    }
}