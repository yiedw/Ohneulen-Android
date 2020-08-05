package com.goodchoice.android.ohneulen.ui.store.review

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.RatingBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.data.model.StoreDetail
import com.goodchoice.android.ohneulen.databinding.StoreReviewBinding
import com.goodchoice.android.ohneulen.ui.login.LoginViewModel
import com.goodchoice.android.ohneulen.ui.store.StoreFragment
import com.goodchoice.android.ohneulen.ui.store.StoreViewModel
import com.goodchoice.android.ohneulen.util.addAppbarFragment
import com.goodchoice.android.ohneulen.util.addMainFragment
import com.goodchoice.android.ohneulen.util.replaceAppbarFragment
import com.goodchoice.android.ohneulen.util.textColor
import org.koin.android.ext.android.bind
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class StoreReview : Fragment() {
    companion object {
        fun newInstance() =
            StoreReview()
    }

    private lateinit var binding: StoreReviewBinding
    private val storeViewModel: StoreViewModel by viewModel()
    private val loginViewModel: LoginViewModel by viewModel()


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
        storeViewModel.storeDetail.observe(viewLifecycleOwner, Observer {
            reviewCnt(it)
            //리뷰가 0개일때
            if (it.reviewCnt == 0) {
                reviewEmpty()
            } else {
                reviewNotEmpty()
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun reviewCnt(storeDetail: StoreDetail) {
        binding.storeReviewTv1.text = "${storeDetail.reviewCnt}개의 후기가 있습니다"
    }

    private fun reviewEmpty() {
        binding.storeReviewEmpty.visibility = View.VISIBLE
        binding.storeReviewRatingbar.visibility = View.GONE
        binding.storeReviewRatingScore.visibility = View.GONE
        binding.storeReviewTv1.visibility = View.GONE
        binding.storeReviewChart.visibility = View.GONE
        binding.storeReviewRv.visibility = View.GONE
        val text = TextUtils.concat(
            "아직 작성된 후기가 없어요\n",
            "지금 나만의 ",
            textColor("맛집", 0, 2, ContextCompat.getColor(requireContext(), R.color.colorOhneulen)),
            "을 공유하시려면\n",
            "상단의 ",
            textColor(
                "후기 작성하기",
                0,
                7,
                ContextCompat.getColor(requireContext(), R.color.colorOhneulen)
            ),
            "버튼을 클릭해 주세요!"
        )
        binding.storeReviewEmptyTv.text = text
    }

    private fun reviewNotEmpty() {
        binding.storeReviewEmpty.visibility = View.GONE
        binding.storeReviewRatingbar.visibility = View.VISIBLE
        binding.storeReviewRatingScore.visibility = View.VISIBLE
        binding.storeReviewTv1.visibility = View.VISIBLE
        binding.storeReviewChart.visibility = View.VISIBLE
        binding.storeReviewRv.visibility = View.VISIBLE
    }


    fun reviewWriteClick(view: View) {
        if (!loginViewModel.isLogin.value!!) {
            val dialog = Dialog(requireContext())
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.logout_dialog)
            dialog.findViewById<TextView>(R.id.logout_dialog_tv2).text =
                requireContext().getString(R.string.require_login)
            dialog.findViewById<Button>(R.id.logout_dialog_cancel).setOnClickListener {
                dialog.dismiss()
            }
            dialog.findViewById<Button>(R.id.logout_dialog_ok).setOnClickListener {
                loginViewModel.logout()
                dialog.dismiss()

            }
            dialog.show()
        } else {
            replaceAppbarFragment(StoreReviewWriteAppbar.newInstance())
            addMainFragment(StoreReviewWrite.newInstance(), true)

        }
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