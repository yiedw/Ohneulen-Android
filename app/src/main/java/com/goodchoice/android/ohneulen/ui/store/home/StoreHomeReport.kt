package com.goodchoice.android.ohneulen.ui.store.home

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.StoreHomeReportBinding
import com.goodchoice.android.ohneulen.ui.MainActivity
import com.goodchoice.android.ohneulen.util.textColor

class StoreHomeReport(private val storeName: String) : Fragment() {
    companion object {
        fun newInstance(storeName: String) = StoreHomeReport(storeName)
    }

    private lateinit var binding: StoreHomeReportBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.store_home_report,
            container,
            false
        )
        binding.fragment = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        binding.storeHomeReport.translationY=MainActivity.appbarFrameLayout.height.toFloat()
        reportTitle()
    }

    private fun reportTitle() {
        val mStoreName = TextUtils.concat(
            textColor(
                storeName,
                0,
                storeName.length,
                ContextCompat.getColor(requireContext(), R.color.colorOhneulen)
            ), "에서 \n정정해야 할 내용이 있나요?"
        )
        binding.storeHomeReportTitle.text = mStoreName

    }

    fun onClick(view: View) {

    }
}