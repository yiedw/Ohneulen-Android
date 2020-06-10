package com.goodchoice.android.ohneulen.ui.partner

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.RadarChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.RadarData
import com.github.mikephil.charting.data.RadarDataSet
import com.github.mikephil.charting.data.RadarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.PartnerReviewFragmentBinding
import com.goodchoice.android.ohneulen.util.replaceMainFragment
import timber.log.Timber

class PartnerReviewFragment : Fragment() {
    companion object {
        fun newInstance() = PartnerReviewFragment()
    }

    private lateinit var binding: PartnerReviewFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.partner_review_fragment,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val chart = binding.partnerReviewRadarChart

        val dataValue1 = ArrayList<RadarEntry>().apply {
            this.add(RadarEntry(2f))
            this.add(RadarEntry(2f))
            this.add(RadarEntry(2f))
            this.add(RadarEntry(2f))
            this.add(RadarEntry(2f))
        }

        val dataValue2 = ArrayList<RadarEntry>().apply {
            this.add(RadarEntry(4.toFloat()))
            this.add(RadarEntry(4f))
            this.add(RadarEntry(3f))
            this.add(RadarEntry(4f))
            this.add(RadarEntry(5f))
        }
        val dataSet1 = RadarDataSet(dataValue1, "업체 평균")
        val dataSet2 = RadarDataSet(dataValue2, "이 업체")
        dataSet1.color = Color.GRAY
        dataSet1.setDrawValues(false)
        dataSet2.color = ContextCompat.getColor(activity!!, R.color.colorSquash60)
        dataSet2.setDrawFilled(true)
        dataSet2.fillColor = ContextCompat.getColor(activity!!, R.color.colorSquash60)
        dataSet2.fillAlpha = 170
        dataSet2.setDrawValues(false)

        val data = RadarData()
        data.setDrawValues(false)
        data.addDataSet(dataSet2)
        data.addDataSet(dataSet1)

        val legend=chart.legend
        legend.isEnabled=false

        val chartList = listOf<String>("맛", "직원 친절도", "분위기", "가격", "청결도")
        val xAxis = chart.xAxis
        xAxis.valueFormatter = object : IndexAxisValueFormatter(chartList) {
        }
        val yAxis = chart.yAxis
        yAxis.axisMinimum=0.toFloat()
        yAxis.axisMaximum=5f
        yAxis.setLabelCount(0,true)
        //라벨제거
        yAxis.setDrawLabels(false)
        yAxis.setDrawGridLines(false)

        //설명 캔슬
        chart.description.isEnabled=false
        //고정
        chart.setTouchEnabled(false)
        //데이터 세팅
        chart.data = data
        chart.invalidate()
    }
    fun reviewWriteClick(view:View){
        replaceMainFragment(PartnerReviewWriteFragment.newInstance())
        Timber.e("asdf")
    }
}